package com.softdesign.votingsystem.application.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdesign.business.domain.AnswerType;
import com.softdesign.business.domain.AssociatedSession;
import com.softdesign.business.domain.Session;
import com.softdesign.business.repository.AnswerTypeRepository;
import com.softdesign.business.repository.AssociatedSessionRepository;
import com.softdesign.votingsystem.application.messaging.VotingResultQueueSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Component
@EnableAsync
public class SessionScheduler {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    private AssociatedSessionRepository associatedSessionRepository;

    @Autowired
    private AnswerTypeRepository answerTypeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VotingResultQueueSender votingResultQueueSender;

    @Async
    public void scheduleSessionToPublish(Session session) {

        Long timer = Timestamp.valueOf(session.getTime()).getTime() - Timestamp.valueOf(LocalDateTime.now()).getTime();

        try {
            LOGGER.info(String.format("Inicializando Thread... Será executada daqui há %d segundos", timer / 1000));
            Thread.sleep(timer);
            HashMap<String, Object> votingResult = checkVoting(session);
            String votingResultMessage = objectMapper.writeValueAsString(votingResult);
            LOGGER.info("Enviando mensagem via RabbitMQ... " + votingResultMessage);
            votingResultQueueSender.send(votingResultMessage);
            LOGGER.info("Thread finalizada... ");
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro inesperado:", e);
            throw new RuntimeException("Ocorreu um erro inesperado:", e);
        }
    }

    private HashMap<String, Object> checkVoting(Session session) {

        List<AssociatedSession> associatedSessions = associatedSessionRepository.findBySession(session.getId())
                .collectList().block();

        HashMap<String, Integer> resultVotation = new HashMap<>();

        associatedSessions.forEach(associatedSession -> {

            Integer count = 0;

            if(resultVotation.containsKey(associatedSession.getAnswerType())) {
                count = resultVotation.get(associatedSession.getAnswerType());
                count++;
            } else {
                count = 1;
            }

            resultVotation.put(associatedSession.getAnswerType(), count);
        });

        return convertVoting(session, resultVotation);
    }

    private HashMap<String, Object> convertVoting(Session session, HashMap<String, Integer> resultVotation) {

        List<AnswerType> answerTypes = answerTypeRepository.findAll().collectList().block();
        HashMap<String, Object> votationConverted = new HashMap<>();

        answerTypes.forEach(answerType -> {
            if(resultVotation.containsKey(answerType.getId())) {
                resultVotation.put(answerType.getAnswer(), resultVotation.get(answerType.getId()));
                resultVotation.remove(answerType.getId());
            }
        });

        votationConverted.put(session.getId(), resultVotation);

        return votationConverted;
    }
}

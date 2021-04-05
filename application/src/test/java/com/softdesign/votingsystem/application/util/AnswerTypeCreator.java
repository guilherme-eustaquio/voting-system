package com.softdesign.votingsystem.application.util;

import com.softdesign.business.data.AnswerTypeData;
import com.softdesign.business.domain.AnswerType;
import com.softdesign.business.response.AnswerTypeResponse;

import java.time.LocalDateTime;

public class AnswerTypeCreator {

    public static AnswerType createDataAnswerType() {
        AnswerType answerType = new AnswerType();
        answerType.setCreatedAt(LocalDateTime.now());
        answerType.setId("606a4c8b75adf05a9009ae34");
        answerType.setCreatedAt(LocalDateTime.now());
        answerType.setAnswer("Pergunta teste");
        return answerType;
    }

    public static AnswerTypeResponse createDataAnswerTypeResponse() {
        AnswerTypeResponse answerTypeResponse = new AnswerTypeResponse();
        answerTypeResponse.setCreatedAt(LocalDateTime.now());
        answerTypeResponse.setId("606a4c8b75adf05a9009ae34");
        answerTypeResponse.setCreatedAt(LocalDateTime.now());
        answerTypeResponse.setAnswer("Pergunta teste");
        return answerTypeResponse;
    }

    public static AnswerTypeData createDataAnswerTypeData() {
        AnswerTypeData answerTypeData = new AnswerTypeData();
        answerTypeData.setAnswer("Pergunta teste");
        return answerTypeData;
    }
}

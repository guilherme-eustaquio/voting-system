package com.softdesign.votingsystem.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdesign.business.data.AnswerTypeData;
import com.softdesign.business.domain.AnswerType;
import com.softdesign.business.service.AnswerTypeService;
import com.softdesign.votingsystem.application.validation.AnswerTypeValidation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {AnswerTypeController.class, ObjectMapper.class})
@AutoConfigureMockMvc
public class AnswerTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnswerTypeService answerTypeService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private AnswerTypeController answerTypeController;

    @MockBean
    private AnswerTypeValidation answerTypeValidation;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void answerTypeTestFindAll() throws Exception {

        Mockito.doReturn(Flux.fromIterable(createListDataAnswerType())).when(answerTypeService).findAll();

        mockMvc.perform(get("/answer-types"))
                .andExpect(status().isOk());
    }

    @Test
    public void answerTypeTestFindById() throws Exception {

        AnswerType answerTypeMocked = createDataAnswerType();

        Mockito.doReturn(Mono.just(answerTypeMocked))
        .when(answerTypeService).findById(answerTypeMocked.getId());

        mockMvc.perform(get("/answer-types/{id}", answerTypeMocked.getId()))
            .andExpect(status().isOk());
    }

    @Test
    public void answerTypeTestFindByAnswer() throws Exception {

        AnswerType answerTypeMocked = createDataAnswerType();

        Mockito.doReturn(Mono.just(answerTypeMocked))
            .when(answerTypeService).findByAnswer(answerTypeMocked.getAnswer());

        mockMvc.perform(get("/answer-types/find-by-answer/{answer}", answerTypeMocked.getAnswer())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void answerTypeTestSave() throws Exception {

        AnswerType answerTypeMocked = createDataAnswerType();
        AnswerTypeData answerTypeData = new AnswerTypeData();
        answerTypeData.setAnswer(answerTypeMocked.getAnswer());

        //Mockito.doReturn(Mono.just(answerTypeMocked))
        //.when(modelMapper).map(answerTypeData, AnswerType.class);

        Mockito.doReturn(Mono.just(answerTypeMocked))
        .when(answerTypeService).save(answerTypeMocked);

        Mockito.doReturn(Mono.just(answerTypeMocked))
        .when(answerTypeValidation)
        .validateCreateAnswer(answerTypeMocked);

        mockMvc.perform(post("/answer-types")
                .accept(MediaType.ALL_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(answerTypeData)))
                .andExpect(status().isOk());
    }

    private AnswerType createDataAnswerType() {
        AnswerType answerType = new AnswerType();
        answerType.setId(UUID.randomUUID().toString());
        answerType.setCreatedAt(LocalDateTime.now());
        answerType.setAnswer("Pergunta teste");
        return answerType;
    }

    private List<AnswerType> createListDataAnswerType() {

        ArrayList<AnswerType> answerTypes = new ArrayList<>();
        int count = 0;

        while(count < 5) {
            AnswerType answerType = new AnswerType();
            answerType.setAnswer("Teste " + 1);
            answerType.setId(UUID.randomUUID().toString());
            answerType.setCreatedAt(LocalDateTime.now());
            answerTypes.add(answerType);
            count++;
        }

        return answerTypes;
    }

}

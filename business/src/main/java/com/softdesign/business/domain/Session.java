package com.softdesign.business.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "session")
@Getter
@Setter
public class Session {

    @MongoId
    private String id;
    private LocalDateTime time;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @DBRef(db="associated")
    private List<Associated> associates;

    @DBRef
    private AnswerType answerType;

    @DBRef
    private Theme theme;

}

package com.softdesign.business.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document(collection = "answer_type")
@Getter
@Setter
public class AnswerType {
    @MongoId
    private String id;
    private String answer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.softdesign.business.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "associated_session")
@Getter
@Setter
public class AssociatedSession {

    @Field("associated_id")
    private String associated;

    @Field("session_id")
    private String session;

    @Field("answer_type_id")
    private String answerType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

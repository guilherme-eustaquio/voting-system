package com.softdesign.business.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "session")
@Getter
@Setter
public class Session {

    @Id
    private String id;
    private LocalDateTime time;

    @Field("theme_id")
    private String theme;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

package com.softdesign.business.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "theme")
@Getter
@Setter
public class Theme {

    @Id
    private String id;
    private String question;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

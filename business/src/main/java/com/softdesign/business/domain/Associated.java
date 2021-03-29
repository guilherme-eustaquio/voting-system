package com.softdesign.business.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "associated")
@Getter
@Setter
public class Associated {

    @Id
    private String id;

    @DBRef(db="session")
    private List<Session> sessions;

    private String cpf;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
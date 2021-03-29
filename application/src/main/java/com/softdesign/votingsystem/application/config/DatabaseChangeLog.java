package com.softdesign.votingsystem.application.config;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;

import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.date;
import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.object;
import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.string;

@ChangeLog
public class DatabaseChangeLog {

    @ChangeSet(order = "001", id = "feedWithInitialCollections", author = "Guilherme")
    public void feedWithInitialCollections(MongockTemplate mongockTemplate) {
        mongockTemplate.createCollection("associated", getAssociatedOptions());
        mongockTemplate.createCollection("theme", getThemeOptions());
        mongockTemplate.createCollection("session", getSessionOptions());
        mongockTemplate.createCollection("associated_session", getAssociatedOptions());
        mongockTemplate.createCollection("answer_type", getAnswerType());
    }

    private static CollectionOptions getAssociatedOptions() {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("cpf", "name")
                .properties(
                        string("cpf").minLength(11).maxLength(11),
                        string("name").minLength(3).maxLength(100),
                        date("created_at"),
                        date("updated_at")
                ).build();
        return CollectionOptions.empty().schema(schema);
    }

    private static CollectionOptions getThemeOptions() {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("question")
                .properties(
                        string("question").minLength(3).maxLength(1000),
                        date("created_at"),
                        date("updated_at")
                ).build();
        return CollectionOptions.empty().schema(schema);
    }

    private static CollectionOptions getSessionOptions() {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("theme_id", "time")
                .properties(
                        object("theme_id"),
                        date("time"),
                        date("created_at"),
                        date("updated_at")
                ).build();
        return CollectionOptions.empty().schema(schema);
    }

    private static CollectionOptions getAssociatedSession() {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("associated_id", "session_id", "answer_type")
                .properties(
                        object("associated_id"),
                        object("session_id"),
                        object("answer_type"),
                        date("created_at"),
                        date("updated_at")
                ).build();
        return CollectionOptions.empty().schema(schema);
    }

    private static CollectionOptions getAnswerType() {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("answer")
                .properties(
                        string("question").minLength(3).maxLength(100),
                        date("created_at"),
                        date("updated_at")
                ).build();
        return CollectionOptions.empty().schema(schema);
    }
}


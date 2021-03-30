package com.softdesign.votingsystem.application.config;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.date;
import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.string;

@ChangeLog
public class DatabaseChangeLog {

    @ChangeSet(order = "001", id = "feedWithInitialCollections", author = "Guilherme")
    public void feedWithInitialCollections(MongockTemplate mongockTemplate) {
        mongockTemplate.createCollection("associated", getAssociatedOptions());
        mongockTemplate.createCollection("theme", getThemeOptions());
        mongockTemplate.createCollection("session", getSessionOptions());
        mongockTemplate.createCollection("associated_session", getAssociatedSessionOptions());
        mongockTemplate.createCollection("answer_type", getAnswerType());
    }

    @ChangeSet(order = "002", id = "feedWithInitialDocuments", author = "Guilherme")
    public void feedWithInitialDocuments(MongoDatabase mongoDatabase) {
        MongoCollection<Document> answerType = mongoDatabase.getCollection("answer_type");
        List<Document> documents = new ArrayList<>();
        documents.add(new Document("answer", "Sim"));
        documents.add(new Document("answer", "Não"));
        answerType.insertMany(documents);
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
                .required("theme_id")
                .properties(
                        string("theme_id"),
                        date("created_at"),
                        date("updated_at")
                ).build();
        return CollectionOptions.empty().schema(schema);
    }

    private static CollectionOptions getAssociatedSessionOptions() {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("associated_id", "session_id", "answer_type_id")
                .properties(
                        string("associated_id"),
                        string("session_id"),
                        string("answer_type_id"),
                        date("created_at"),
                        date("updated_at")
                ).build();
        return CollectionOptions.empty().schema(schema);
    }

    private static CollectionOptions getAnswerType() {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("answer")
                .properties(
                        string("answer").minLength(3).maxLength(100),
                        date("created_at"),
                        date("updated_at")
                ).build();
        return CollectionOptions.empty().schema(schema);
    }
}


package com.softdesign.votingsystem.application;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(
    basePackages = {
        "com.softdesign.business.domain"
    }
)
@EnableReactiveMongoRepositories(
    basePackages = {
        "com.softdesign.business.repository"
    }
)
@ComponentScan(
    basePackages = {
        "com.softdesign.business",
        "com.softdesign.votingsystem.application"
    }
)
@EnableMongock
public class VotingSystemApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo")));
    }

    public static void main(String[] args) {
        SpringApplication.run(VotingSystemApplication.class, args);
    }
}

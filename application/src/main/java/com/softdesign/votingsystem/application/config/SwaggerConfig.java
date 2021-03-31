package com.softdesign.votingsystem.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket geralApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.softdesign.votingsystem.application.controller"))
            .paths(PathSelectors.regex("/.*"))
            .build()
            .apiInfo(personalInfo());
    }

    private ApiInfo personalInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "Voting System - API REST",
                "API REST de gerenciamento de pautas.",
                "1.0",
                "",
                new Contact("Guilherme Eustáquio Moreira Santana", "https://www.linkedin.com/in/guilherme-moreira-4575711b2/",
                        "guilherme.eustaquio.moreira@gmail.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licesen.html", new ArrayList<VendorExtension>()
        );

        return apiInfo;
    }
}

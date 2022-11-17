package com.sap.pls.samples.lambda;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.lambda.LambdaClient;

@SpringBootApplication
public class Application {
    Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public LambdaClient lambdaClient(Config config) throws URISyntaxException {
        logger.info("Using Lambda endpoint URL {}", config.getEndpointUrl());

        return LambdaClient.builder()
                .endpointOverride(new URI(config.getEndpointUrl()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}

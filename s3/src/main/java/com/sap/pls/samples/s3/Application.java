package com.sap.pls.samples.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3control.S3ControlClient;

import java.net.URI;


@SpringBootApplication
public class Application {
    Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(name = "s3Bucket")
    public S3Client s3Client(Config config) {
        URI uri = URI.create("https://bucket." + config.getEndpointHostname());
        logger.info("Using custom S3 endpoint URL {}", uri);

        return S3Client.builder()
                .endpointOverride(uri)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public S3ControlClient s3ControlClient(Config config) {
        URI uri = URI.create("https://control." + config.getEndpointHostname());
        logger.info("Using custom S3 control endpoint URL {}", uri);

        return S3ControlClient.builder()
                .endpointOverride(uri)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean(name = "s3AccessPoint")
    public S3Client s3AccessPointClient(Config config) {
        URI uri = URI.create("https://accesspoint." + config.getEndpointHostname());
        logger.info("Using custom S3 access point endpoint URL {}", uri);

        return S3Client.builder()
                .endpointOverride(uri)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}

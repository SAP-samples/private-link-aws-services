package com.sap.pls.samples.lambda;

import io.pivotal.cfenv.core.CfCredentials;
import io.pivotal.cfenv.core.CfEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;

import javax.validation.Valid;
import java.net.URI;

@Validated
@SpringBootApplication
public class Application {

    private static final String USER_PROVIDED_LABEL = "user-provided";
    private static final String PRIVATELINK_LABEL = "privatelink";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public LambdaClient lambdaClient(Config config) {
        logger.info("Using Lambda endpoint URL {}", config.getEndpointUrl());

        AwsBasicCredentials credentials = AwsBasicCredentials.create(config.getAccessKeyId(), config.getSecretAccessKey());
        return LambdaClient.builder()
                .endpointOverride(URI.create(config.getEndpointUrl()))
                .region(Region.of(config.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    CfEnv cfEnv() {
        return new CfEnv();
    }

    @Bean
    @Valid
    Config config(CfEnv cfEnv) {
        CfCredentials snsServiceCredentials = cfEnv.findCredentialsByLabel(USER_PROVIDED_LABEL);

        Config config = new Config();
        config.setEndpointUrl(String.format("https://%s", cfEnv.findCredentialsByLabel(PRIVATELINK_LABEL).getHost()));
        config.setAccessKeyId(snsServiceCredentials.getString("accessKeyId"));
        config.setSecretAccessKey(snsServiceCredentials.getString("secretAccessKey"));
        config.setRegion(snsServiceCredentials.getString("region"));
        config.setFunctionName(snsServiceCredentials.getString("functionName"));
        return config;
    }
}

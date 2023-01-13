package com.sap.pls.samples.sns;

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
import software.amazon.awssdk.services.sns.SnsClient;

import javax.validation.Valid;
import java.net.URI;

@SpringBootApplication
@Validated
public class Application {

    private static final String USER_PROVIDED_LABEL = "user-provided";
    private static final String PRIVATELINK_LABEL = "privatelink";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    SnsClient snsClient(Config config) {
        logger.info("Using custom SNS endpoint URL {}", config.getEndpointUrl());

        AwsBasicCredentials credentials = AwsBasicCredentials.create(config.getAccessKeyId(), config.getSecretAccessKey());
        return SnsClient.builder()
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
        config.setTopicArn(snsServiceCredentials.getString("topicArn"));
        config.setRegion(snsServiceCredentials.getString("region"));
        return config;
    }
}

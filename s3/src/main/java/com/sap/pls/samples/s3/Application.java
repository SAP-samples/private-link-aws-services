package com.sap.pls.samples.s3;

import io.pivotal.cfenv.core.CfCredentials;
import io.pivotal.cfenv.core.CfEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3control.S3ControlClient;

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
    public S3Client s3Client(Config config, StaticCredentialsProvider staticCredentialsProvider) {
        URI uri = URI.create("https://bucket." + config.getEndpointHostname());
        logger.info("Using custom S3 endpoint URL {}", uri);

        return S3Client.builder()
                .endpointOverride(uri)
                .region(Region.of(config.getRegion()))
                .credentialsProvider(staticCredentialsProvider)
                .build();
    }

    @Bean
    public S3ControlClient s3ControlClient(Config config, StaticCredentialsProvider staticCredentialsProvider) {
        URI uri = URI.create("https://control." + config.getEndpointHostname());
        logger.info("Using custom S3 control endpoint URL {}", uri);

        return S3ControlClient.builder()
                .endpointOverride(uri)
                .region(Region.of(config.getRegion()))
                .credentialsProvider(staticCredentialsProvider)
                .build();
    }

    @Bean
    public S3Client s3AccessPointClient(Config config, StaticCredentialsProvider staticCredentialsProvider) {
        URI uri = URI.create("https://accesspoint." + config.getEndpointHostname());
        logger.info("Using custom S3 access point endpoint URL {}", uri);

        return S3Client.builder()
                .endpointOverride(uri)
                .region(Region.of(config.getRegion()))
                .credentialsProvider(staticCredentialsProvider)
                .build();
    }

    @Bean
    public StaticCredentialsProvider credentials(Config config) {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(config.getAccessKeyId(), config.getSecretAccessKey()));
    }

    @Bean
    CfEnv cfEnv() {
        return new CfEnv();
    }

    @Bean
    @Valid
    Config config(CfEnv cfEnv) {
        CfCredentials s3ServiceCredentials = cfEnv.findCredentialsByLabel(USER_PROVIDED_LABEL);
        String host = cfEnv.findCredentialsByLabel(PRIVATELINK_LABEL).getHost();
        if (StringUtils.hasText(host)) {
            host = host.replaceFirst("^\\*.", "");
        }

        Config config = new Config();
        config.setEndpointHostname(host);
        config.setAccessKeyId(s3ServiceCredentials.getString("accessKeyId"));
        config.setSecretAccessKey(s3ServiceCredentials.getString("secretAccessKey"));
        config.setRegion(s3ServiceCredentials.getString("region"));
        config.setAccountId(s3ServiceCredentials.getString("accountId"));
        config.setAccessPointArn(s3ServiceCredentials.getString("accessPointArn"));
        return config;
    }
}

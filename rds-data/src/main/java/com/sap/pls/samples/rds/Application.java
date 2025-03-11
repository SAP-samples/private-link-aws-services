package com.sap.pls.samples.rds;

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
import software.amazon.awssdk.core.internal.http.loader.DefaultSdkHttpClientBuilder;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.SdkHttpConfigurationOption;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rdsdata.RdsDataClient;
import software.amazon.awssdk.utils.AttributeMap;

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
    public RdsDataClient rdsDataClient(Config config) {
        logger.info("Using custom RDS Data endpoint URL {}", config.getEndpointUrl());

        AwsBasicCredentials credentials = AwsBasicCredentials.create(config.getAccessKeyId(),
                config.getSecretAccessKey());
        final AttributeMap.Builder amBuilder = AttributeMap.builder();

        logger.info("TRUST ALL CERTIFICATES: {}", config.getTrustAllCertificates());

        if (config.getTrustAllCertificates()) {
            amBuilder.put(SdkHttpConfigurationOption.TRUST_ALL_CERTIFICATES, true);
        }
        final AttributeMap attributeMap = amBuilder.build();
        final SdkHttpClient httpClient = new DefaultSdkHttpClientBuilder().buildWithDefaults(attributeMap);

        return RdsDataClient.builder()
                .endpointOverride(URI.create(config.getEndpointUrl()))
                .region(Region.of(config.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .httpClient(httpClient)
                .build();
    }

    @Bean
    CfEnv cfEnv() {
        return new CfEnv();
    }

    @Bean
    @Valid
    Config config(CfEnv cfEnv) {
        CfCredentials rdsServiceCredentials = cfEnv.findCredentialsByLabel(USER_PROVIDED_LABEL);

        Config config = new Config();
        config.setEndpointUrl(String.format("https://%s", cfEnv.findCredentialsByLabel(PRIVATELINK_LABEL).getHost()));
        config.setAccessKeyId(rdsServiceCredentials.getString("accessKeyId"));
        config.setSecretAccessKey(rdsServiceCredentials.getString("secretAccessKey"));
        config.setRegion(rdsServiceCredentials.getString("region"));
        config.setResourceArn(rdsServiceCredentials.getString("rdsArn"));
        config.setSecretArn(rdsServiceCredentials.getString("secretArn"));
        return config;
    }
}

package com.sap.pls.samples.sns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@RestController
public class Controller {

    private Config config;
    private SnsClient snsClient;

    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    public Controller(Config config, SnsClient snsClient) {
        this.config = config;
        this.snsClient = snsClient;
    }

    @GetMapping
    public void publishMessage(@RequestParam("message") String message) {
        logger.info("Requested SNS message for ARN '{}' and message '{}'", config.getTopicArn(), message);
        PublishRequest request = PublishRequest.builder()
                .message(message)
                .topicArn(config.getTopicArn())
                .build();

        PublishResponse result = snsClient.publish(request);
        logger.info("Sent message with id '{}'. Status: {}", result.messageId(), result.sdkHttpResponse().statusCode());
    }
}

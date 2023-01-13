package com.sap.pls.samples.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Controller {

    private final Config config;
    private final SqsClient sqsClient;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Controller(Config config, SqsClient sqsClient) {
        this.config = config;
        this.sqsClient = sqsClient;
    }

    @GetMapping
    public List<String> readMessages() {
        logger.info("Reading messages from {}", config.getQueueUrl());
        return sqsClient.
                receiveMessage(builder -> builder.queueUrl(config.getQueueUrl()).maxNumberOfMessages(10))
                .messages()
                .stream()
                .map(Message::body)
                .collect(Collectors.toList());
    }
}

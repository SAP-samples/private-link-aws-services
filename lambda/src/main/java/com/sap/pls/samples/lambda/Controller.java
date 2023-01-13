package com.sap.pls.samples.lambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

@RestController
public class Controller {

    private final Config config;
    private final LambdaClient lambdaClient;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Controller(Config config, LambdaClient lambdaClient) {
        this.config = config;
        this.lambdaClient = lambdaClient;
    }

    @GetMapping
    public String callFunction() {
        logger.info("Calling Lambda function {}", config.getFunctionName());

        InvokeRequest req = InvokeRequest.builder()
                .functionName(config.getFunctionName())
                .build();

        return lambdaClient.invoke(req).payload().asUtf8String();
    }
}

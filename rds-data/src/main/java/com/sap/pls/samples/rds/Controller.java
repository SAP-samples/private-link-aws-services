package com.sap.pls.samples.rds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.rdsdata.RdsDataClient;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementRequest;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementResponse;

@RestController
public class Controller {
    private final Config config;
    private final RdsDataClient rdsDataClient;

    @Autowired
    public Controller(Config config, RdsDataClient rdsDataClient) {
        this.config = config;
        this.rdsDataClient = rdsDataClient;
    }

    @PostMapping("/database")
    public String createDatabase() {
        String insertSQL = "CREATE DATABASE IF NOT EXISTS test";

        ExecuteStatementRequest sqlRequest = createExecuteStatementRequest(insertSQL);
        ExecuteStatementResponse executeStatementResponse = rdsDataClient.executeStatement(sqlRequest);

        return "Created Database 'test' with status code:" + executeStatementResponse.sdkHttpResponse().statusCode();
    }

    private ExecuteStatementRequest createExecuteStatementRequest(String sql) {
        ExecuteStatementRequest request = ExecuteStatementRequest.builder()
                .secretArn(config.getSecretArn())
                .resourceArn(config.getResourceArn())
                .sql(sql)
                .build();
        return request;
    }
}



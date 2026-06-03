package com.sap.pls.samples.rds;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.rdsdata.RdsDataClient;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementRequest;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementResponse;

@RestController
public class Controller {

    private final Config config;
    private final RdsDataClient rdsDataClient;

    public Controller(Config config, RdsDataClient rdsDataClient) {
        this.config = config;
        this.rdsDataClient = rdsDataClient;
    }

    @PostMapping("/database")
    public String createDatabase() {
        String insertSQL = "CREATE DATABASE test";
        String checkDbSQL = "SELECT EXISTS(SELECT datname FROM pg_catalog.pg_database WHERE datname = 'test')";

        ExecuteStatementRequest sqlRequest = createExecuteStatementRequest(checkDbSQL);
        ExecuteStatementResponse executeStatementResponse = rdsDataClient.executeStatement(sqlRequest);
        final int httpStatus = executeStatementResponse.sdkHttpResponse().statusCode();
        if (httpStatus >= 200 && httpStatus < 300) {

            if (executeStatementResponse.hasRecords()) {
                final boolean dbExists = executeStatementResponse.records().get(0).get(0).booleanValue();
                if (dbExists) {
                    return "Database 'test' already exists";
                }
            }

            sqlRequest = createExecuteStatementRequest(insertSQL);
            executeStatementResponse = rdsDataClient.executeStatement(sqlRequest);

            return "Created Database 'test' with status code:"
                    + executeStatementResponse.sdkHttpResponse().statusCode();

        }
        return "Error: executing SQL that checks if DB exists, status code:" + httpStatus;
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

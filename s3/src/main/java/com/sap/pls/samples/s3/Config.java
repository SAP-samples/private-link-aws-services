package com.sap.pls.samples.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties(prefix = "s3")
@Validated
public class Config {
    
    @NotBlank
    private String endpointHostname;
    @NotBlank
    private String accountId;
    @NotBlank
    private String accessPointArn;

    public String getEndpointHostname() {
        return endpointHostname;
    }

    public void setEndpointHostname(String endpointHostname) {
        this.endpointHostname = endpointHostname;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccessPointArn() {
        return accessPointArn;
    }

    public void setAccessPointArn(String accessPointArn) {
        this.accessPointArn = accessPointArn;
    }
}

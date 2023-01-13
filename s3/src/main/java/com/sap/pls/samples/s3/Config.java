package com.sap.pls.samples.s3;

import javax.validation.constraints.NotBlank;

public class Config {

    @NotBlank
    private String endpointHostname;

    @NotBlank
    private String accountId;

    @NotBlank
    private String accessPointArn;

    @NotBlank
    private String accessKeyId;

    @NotBlank
    private String secretAccessKey;

    @NotBlank
    private String region;

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

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
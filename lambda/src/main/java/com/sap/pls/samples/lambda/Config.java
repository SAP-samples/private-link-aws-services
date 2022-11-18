package com.sap.pls.samples.lambda;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties(prefix = "lambda")
@Validated
public class Config {
    @NotBlank
    private String endpointUrl;
    
    @NotBlank
    private String functionName;

    public String getEndpointUrl() {
        return this.endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
}

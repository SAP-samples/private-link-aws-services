package com.sap.pls.samples.ses;

import io.pivotal.cfenv.core.CfCredentials;
import io.pivotal.cfenv.core.CfService;
import io.pivotal.cfenv.spring.boot.CfEnvProcessor;
import io.pivotal.cfenv.spring.boot.CfEnvProcessorProperties;

import java.util.Map;


// See https://github.com/pivotal-cf/java-cfenv#supporting-other-services
public class UserProvidedServiceCfEnvProcessor implements CfEnvProcessor {

    private static final String SERVICE = "user-provided";

    @Override
    public boolean accept(CfService service) {
        return service.existsByTagIgnoreCase(SERVICE) ||
                service.existsByLabelStartsWith(SERVICE) ||
                service.existsByUriSchemeStartsWith(SERVICE) ||
                service.existsByCredentialsContainsUriField(SERVICE);
    }

    @Override
    public void process(CfCredentials cfCredentials, Map<String, Object> properties) {
        properties.put("spring.mail.username", cfCredentials.getUsername());
        properties.put("spring.mail.password", cfCredentials.getPassword());
        properties.put("ses-sample.from", cfCredentials.getString("from"));
        properties.put("ses-sample.to", cfCredentials.getString("to"));
    }

    @Override
    public CfEnvProcessorProperties getProperties() {
        return CfEnvProcessorProperties.builder()
                .propertyPrefixes("spring.mail,ses-sample")
                .serviceName(SERVICE)
                .build();
    }
}

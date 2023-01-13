package com.sap.pls.samples.ses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    /***
     * Default {@link JavaMailSender} bean created spring, autopopulated with properties from config.
     */
    private final JavaMailSender emailSender;
    private final String from;
    private final String to;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Controller(
            JavaMailSender emailSender,
            @Value("${ses-sample.from}") String from,
            @Value("${ses-sample.to}") String to
    ) {
        this.emailSender = emailSender;
        this.to = to;
        this.from = from;
    }

    /**
     * Sends a mail from a pre-defined address to a pre-defined recipient.
     */
    @GetMapping("/")
    void send(@RequestParam("body") String body) {
        logger.info("Sending mail to '{}' from '{}'", to, from);

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("sent through SES via PrivateLink");
        message.setText(body);
        emailSender.send(message);

        logger.info("Sent mail to '{}' from '{}'", to, from);
    }
}

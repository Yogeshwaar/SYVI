package com.coeta.group5.chat_bot_ai.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Component
public class ConstantFileHandler {

    Logger log = LoggerFactory.getLogger(ConstantFileHandler.class);

    public static String EMAIL_BODY;

    public static String SUBJECT = "One Time Password for Email address verification";

    public void loadEmailTemplate() {
        try {
            ClassPathResource resource = new ClassPathResource("templates/email-body.html");
            byte[] fileData = Files.readAllBytes(resource.getFile().toPath());
            EMAIL_BODY =  new String(fileData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Exception occurred while loading the email body from templates");
        }
    }

}

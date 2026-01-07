package com.coeta.group5.chat_bot_ai.helper;

import com.coeta.group5.chat_bot_ai.entity.PromptContentRequestObj;
import com.coeta.group5.chat_bot_ai.entity.PromptContentResponseObj;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PromptHandler {

    Logger log = LoggerFactory.getLogger(PromptHandler.class);

    @Autowired
    private ConstantFileHandler fileHandler;

    public enum keys{
        CHATBOT_INTERNAL,
        CHATBOT_PUBLIC,
        INTERNAL_PROMPT,
        PUBLIC_PROMPT
    }

    List<String> fileNames = List.of(keys.CHATBOT_INTERNAL+".txt",
            keys.CHATBOT_PUBLIC+".txt");

    public static Map<String, String> PROMPTS = new HashMap<>();

    // load prompt from classPath
    private String loadPromptTemplate(String fileName) throws IOException {
        Path filePath = Paths.get("prompts", fileName);
        return Files.readString(filePath);
    }

    @PostConstruct
    public void init()
    {
        try{
            for(String fileName : fileNames){
                Path filePath = Paths.get("prompts", fileName);
                Files.createDirectories(filePath.getParent());
                if (!Files.exists(filePath)) {
                    Files.createFile(filePath);
                }
                String prompt = loadPromptTemplate(fileName.toLowerCase());
                String key = fileName.split("_")[1].split(".txt")[0]+"_PROMPT";
                PROMPTS.put(key, prompt);
                fileHandler.loadEmailTemplate();
                System.out.println(key);
            }
        }catch (IOException e){
            log.error("Exception occurred while loading the prompt ",e);
        }
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public void autoLoadPrompts(){
        init();
        log.info("Prompts Loaded");
    }

    public boolean updatePrompt(String promptType, PromptContentRequestObj requestObj){
        boolean status = false;
        try{
            Optional<String> fileName = fileNames.stream().filter(f -> f.toLowerCase().contains(promptType)).findFirst();
            if(fileName.isPresent()){
                Path filePath = Paths.get("prompts", fileName.get());
                Files.write(filePath, requestObj.getPromptContent().getBytes(), StandardOpenOption.APPEND);
                status = true;
                log.info("File content overwritten successfully on file = "+filePath);
                init();
            }else{
                log.info("File is not present");
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return status;
    }

    public PromptContentResponseObj getALlPrompts(){
        PromptContentResponseObj responseObj = new PromptContentResponseObj();
        responseObj.setInternalPrompt(PROMPTS.get(keys.INTERNAL_PROMPT.toString()));
        responseObj.setPublicPrompt(PROMPTS.get(keys.PUBLIC_PROMPT.toString()));
        return responseObj;
    }

}

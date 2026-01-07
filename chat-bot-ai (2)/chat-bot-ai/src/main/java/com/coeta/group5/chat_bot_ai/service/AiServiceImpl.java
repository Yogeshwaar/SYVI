 package com.coeta.group5.chat_bot_ai.service;

import com.coeta.group5.chat_bot_ai.helper.PromptHandler;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.coeta.group5.chat_bot_ai.helper.PromptHandler.PROMPTS;

 @Service
 public class AiServiceImpl implements AiService{

     private ChatClient client;

     public AiServiceImpl(ChatClient.Builder builder){
         this.client = builder.build();
     }

     @Override
     public String askPublicAi(String question) {
         String systemPrompt = PROMPTS.get(PromptHandler.keys.PUBLIC_PROMPT.toString());
         return client.prompt()
                 .system(systemPrompt)
                 .user(question)
                 .call()
                 .content();
     }

     @Override
     public Flux<String> askInternalStreamAi(String question) {
         SecurityContext context = SecurityContextHolder.getContext();
         String systemPrompt = PROMPTS.get(PromptHandler.keys.INTERNAL_PROMPT.toString());
         return client.prompt().system(systemPrompt)
                 .user(question)
                 .stream().content()
                 .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
     }

     @Override
     public String askInternalAi(String question) {
         String systemPrompt = PROMPTS.get(PromptHandler.keys.INTERNAL_PROMPT.toString());
         return client.prompt()
                 .system(systemPrompt)
                 .user(question)
                 .call()
                 .content();
     }
 }

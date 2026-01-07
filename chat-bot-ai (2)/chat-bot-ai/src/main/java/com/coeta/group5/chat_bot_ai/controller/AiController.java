package com.coeta.group5.chat_bot_ai.controller;

import com.coeta.group5.chat_bot_ai.entity.ChatEntry;
import com.coeta.group5.chat_bot_ai.entity.Request;
import com.coeta.group5.chat_bot_ai.entity.Response;
import com.coeta.group5.chat_bot_ai.service.AiService;
import com.coeta.group5.chat_bot_ai.service.ChatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AiController {

    @Autowired
    private ChatServiceImpl chatService;

    @Autowired
    private AiService aiService;

    @PostMapping("/ask-ai")
    public ResponseEntity<Response> askAiInStream(
            @RequestBody Request request
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String responseText = chatService.askAiSaveChatEntry(request.getQuestion(),username);
        return ResponseEntity.ok(new Response(responseText));
    }

    @GetMapping("/chat-history")
    public ResponseEntity<Page<ChatEntry>> getChatHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int size
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Page<ChatEntry> response = chatService.getAllEntries(username,page,size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

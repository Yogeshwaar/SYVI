package com.coeta.group5.chat_bot_ai.service;

import com.coeta.group5.chat_bot_ai.entity.ChatEntry;
import com.coeta.group5.chat_bot_ai.entity.User;
import com.coeta.group5.chat_bot_ai.helper.ChatResponseCache;
import com.coeta.group5.chat_bot_ai.repository.ChatEntriesRepository;
import com.coeta.group5.chat_bot_ai.repository.ChatEntriesRepositoryImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl {

    @Autowired
    private AiService aiService;

    @Autowired
    private ChatEntriesRepository chatEntriesRepository;

    @Autowired
    private ChatResponseCache chatResponseCache;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatEntriesRepositoryImpl chatEntriesRepositoryImpl;


    public Flux<String> askStreamAiSaveChatEntry(String question, String username){
        chatResponseCache.storeQuestion(username,question);

        return aiService.askInternalStreamAi(question).doOnNext(responseChunk -> chatResponseCache
                .addResponse(username,responseChunk)).doOnComplete(() -> saveResponseInBackground(username));
    }

    public String askAiSaveChatEntry(String question, String username){
        String answer = aiService.askInternalAi(question);
        new Thread(() -> {
            ChatEntry savedEntry = chatEntriesRepository.save(new ChatEntry(question, answer));
            User user = userService.findByUserName(username);
            user.getChatEntries().add(savedEntry);
            userService.saveUser(user);
        }).start();
        return answer;
    }

    private void saveResponseInBackground(String username){
        new Thread(() -> {
            List<String> responses = chatResponseCache.getResponses(username);
            String question = chatResponseCache.getQuestion(username);
            if(!responses.isEmpty()){
                String fullResponse = String.join("", responses);
                ChatEntry savedEntry = chatEntriesRepository.save(new ChatEntry(question,fullResponse));
                User user = userService.findByUserName(username);
                user.getChatEntries().add(savedEntry);
                userService.saveUser(user);
                chatResponseCache.removeResponses(username);
            }
        }).start();
    }

    public Page<ChatEntry> getAllEntries(String username, int page, int size){
        User user = userService.findByUserName(username);
        Set<ObjectId> objectIds = user.getChatEntries().stream().map(ChatEntry::getId).collect(Collectors.toSet());
        return chatEntriesRepositoryImpl.getLatestChatEntries(page,size,objectIds);
    }
}

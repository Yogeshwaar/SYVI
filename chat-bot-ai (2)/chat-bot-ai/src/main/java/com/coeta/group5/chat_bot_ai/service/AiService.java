package com.coeta.group5.chat_bot_ai.service;

import reactor.core.publisher.Flux;

public interface AiService {

    String askPublicAi(String question);

//    String askInternalAi(String question);
    Flux<String> askInternalStreamAi(String question);

    String askInternalAi(String question);
}

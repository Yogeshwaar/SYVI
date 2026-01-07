package com.coeta.group5.chat_bot_ai.repository;

import com.coeta.group5.chat_bot_ai.entity.ChatEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatEntriesRepository extends MongoRepository<ChatEntry, ObjectId> {
}

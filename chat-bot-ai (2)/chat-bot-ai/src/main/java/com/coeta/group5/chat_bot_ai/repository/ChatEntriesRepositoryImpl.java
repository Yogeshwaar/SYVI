package com.coeta.group5.chat_bot_ai.repository;

import com.coeta.group5.chat_bot_ai.entity.ChatEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class ChatEntriesRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<ChatEntry> getLatestChatEntries(int page, int size, Set<ObjectId> ids){
        Pageable pageable = PageRequest.of(page,size);
        Query query = new Query()
                .addCriteria(Criteria.where("_id").in(ids))
                .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(500)
                .with(pageable);
        List<ChatEntry> chatEntries = mongoTemplate.find(query,ChatEntry.class);
        long total = mongoTemplate.count(new Query(Criteria.where("_id").in(ids)),ChatEntry.class);

        return new PageImpl<>(chatEntries,pageable,total);
    }

}

package com.coeta.group5.chat_bot_ai.repository;

import com.coeta.group5.chat_bot_ai.entity.NameEmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<NameEmailDTO> getAllUsers(){
        Query query = new Query();
        query.fields().include("_id").include("fullName").include("userName");
        return mongoTemplate.find(query,NameEmailDTO.class,"users");
    }
}

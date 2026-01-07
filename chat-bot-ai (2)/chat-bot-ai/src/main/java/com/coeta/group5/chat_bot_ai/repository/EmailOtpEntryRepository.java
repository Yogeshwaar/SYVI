package com.coeta.group5.chat_bot_ai.repository;

import com.coeta.group5.chat_bot_ai.entity.EmailOtpEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailOtpEntryRepository extends MongoRepository<EmailOtpEntry, ObjectId> {

    EmailOtpEntry findByEmail(String email);
    EmailOtpEntry findByEmailAndOtp(String email, long otp);
}

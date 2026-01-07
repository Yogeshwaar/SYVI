package com.coeta.group5.chat_bot_ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class ChatBotAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatBotAiApplication.class, args);
	}

	@Bean
	public PlatformTransactionManager addTransaction(MongoDatabaseFactory dbFactory){
		return new MongoTransactionManager(dbFactory);
	}
}

package com.example.food_deliv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfig {
    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.chat-id}")
    private String chatId;

    public String getBotToken() {
        return botToken;
    }

    public String getChatId() {
        return chatId;
    }
}

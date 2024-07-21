package com.safronov.timofei.telegram.helper.bot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@RequiredArgsConstructor
public class BotTelegramSenderModels {

    @RequiredArgsConstructor
    @Getter
    public static class SendMessageEvent {
        private final Long chatId;
        private final String message;
        private final CountDownLatch cdl;
    }

    @RequiredArgsConstructor
    @Getter
    public static class EditMessageEvent {
        private final Long chatId;
        private final Integer messageId;
        private final String newText;
        private final CountDownLatch cdl;
    }
}

package com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher;

import com.safronov.timofei.telegram.helper.bot.telegramui.openai.OpenAiTelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.yandex.YandexOpenAiFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
@Slf4j
public class TelegramDispatcherButtons extends AbstractTelegramDispatcher {
    public TelegramDispatcherButtons(OpenAiTelegramFacade openAiTelegramFacade, YandexOpenAiFacade yandexOpenAiFacade) {
        super(openAiTelegramFacade, yandexOpenAiFacade);
    }

    public void handle(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String responseText = switch (callbackData) {
            case "help" -> "Памагити";
            case "choose_service" -> "Выбираем сервис";
            case "info" -> "Информация: " + callbackQuery.getFrom();
            default -> "Unknown selection.";
        };

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .text(responseText)
                .build();

        getTelegramBotComponent().send(editMessage);

    }
}

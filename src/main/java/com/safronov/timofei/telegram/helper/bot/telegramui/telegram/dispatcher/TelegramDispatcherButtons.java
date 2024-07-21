package com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher;

import com.safronov.timofei.telegram.helper.bot.model.db.ServiceItem;
import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.repositorie.UsersRepository;
import com.safronov.timofei.telegram.helper.bot.telegramui.openai.OpenAiTelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.InlineKeyboardService;
import com.safronov.timofei.telegram.helper.bot.telegramui.yandex.YandexOpenAiFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
@Slf4j
public class TelegramDispatcherButtons extends AbstractTelegramDispatcher {

    private final UsersRepository usersRepository;

    protected TelegramDispatcherButtons(OpenAiTelegramFacade openAiTelegramFacade, YandexOpenAiFacade yandexOpenAiFacade, InlineKeyboardService inlineKeyboardService, UsersRepository usersRepository) {
        super(openAiTelegramFacade, yandexOpenAiFacade, inlineKeyboardService);
        this.usersRepository = usersRepository;
    }

    public void handle(UserDao userDao, CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String responseText = switch (callbackData) {
            case "menu" -> {
                sendMenuInlineKeyboard(callbackQuery.getMessage(), userDao);
                yield "Меню";
            }
            case "choose_service" -> {
                sendChooseSystemInlineKeyboard(callbackQuery.getMessage());
                yield "Выбор системы";
            }
            case "help" -> "Памагити";
            case "info" -> "Информация: " + callbackQuery.getFrom();
            case "service_choose_openai" -> {
                saveChosenService(userDao, ServiceItem.OPEN_AI);
                yield "Успешно выбран сервис: " + ServiceItem.OPEN_AI;
            }
            case "service_choose_yandex" -> {
                saveChosenService(userDao, ServiceItem.YA_GPT);
                yield "Успешно выбран сервис: " + ServiceItem.YA_GPT;
            }
            default -> "Unknown selection.";
        };

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId.toString())
                .text(responseText)
                .messageId(messageId).build();

        getTelegramBotComponent().send(editMessage);
    }

    private void saveChosenService(UserDao userDao, ServiceItem serviceItem) {
        userDao.setLastServiceItem(serviceItem);
        usersRepository.save(userDao);
    }
}

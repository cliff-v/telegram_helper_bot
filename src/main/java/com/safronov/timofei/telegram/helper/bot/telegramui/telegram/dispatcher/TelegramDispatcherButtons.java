package com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher;

import com.safronov.timofei.telegram.helper.bot.model.db.ServiceItem;
import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.repositorie.UsersRepository;
import com.safronov.timofei.telegram.helper.bot.telegramui.openai.OpenAiTelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.InlineKeyboardService;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.RegistrationService;
import com.safronov.timofei.telegram.helper.bot.telegramui.yandex.YandexOpenAiFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
@Slf4j
public class TelegramDispatcherButtons extends AbstractTelegramDispatcher {

    private final UsersRepository usersRepository;
    private final RegistrationService registrationService;

    protected TelegramDispatcherButtons(OpenAiTelegramFacade openAiTelegramFacade, YandexOpenAiFacade yandexOpenAiFacade, InlineKeyboardService inlineKeyboardService, UsersRepository usersRepository, RegistrationService registrationService) {
        super(openAiTelegramFacade, yandexOpenAiFacade, inlineKeyboardService);
        this.usersRepository = usersRepository;
        this.registrationService = registrationService;
    }

    public void handle(UserDao userDao, CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String responseText = handleMenuCommand(userDao, callbackQuery)
                + handleServiceCommand(userDao, callbackQuery)
                + handleChooseServiceCommand(userDao, callbackQuery)
                + handleAdminCommand(userDao, callbackQuery);

        if (responseText.isBlank()) {
            responseText = "Unknown selection. Request didn't handled";
        }

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId.toString())
                .text(responseText)
                .messageId(messageId).build();

        getTelegramBotComponent().send(editMessage);
    }

    private String handleChooseServiceCommand(UserDao userDao, CallbackQuery callbackQuery) {
        return switch (callbackQuery.getData()) {
            case "choose_service" -> {
                sendChooseSystemInlineKeyboard(callbackQuery.getMessage());
                yield "Выбор системы";
            }
            case "service_choose_openai" -> {
                saveChosenService(userDao, ServiceItem.OPEN_AI);
                yield "Успешно выбран сервис: " + ServiceItem.OPEN_AI;
            }
            case "service_choose_yandex" -> {
                saveChosenService(userDao, ServiceItem.YA_GPT);
                yield "Успешно выбран сервис: " + ServiceItem.YA_GPT;
            }
            default -> "";
        };
    }

    private String handleAdminCommand(UserDao userDao, CallbackQuery callbackQuery) {
        return switch (callbackQuery.getData()) {
            case "registration_request_accept" -> {
                registrationService.handleRegistrationRequest(callbackQuery, true);
                yield "Заявка одобрена";
            }
            case "registration_request_reject" -> {
                registrationService.handleRegistrationRequest(callbackQuery, false);
                yield "Заявка отклонена.";
            }
            default -> "";
        };
    }

    private String handleServiceCommand(UserDao userDao, CallbackQuery callbackQuery) {
        return switch (callbackQuery.getData()) {

            default -> "";
        };
    }

    private String handleMenuCommand(UserDao userDao, CallbackQuery callbackQuery) {
        return switch (callbackQuery.getData()) {
            case "menu" -> {
                sendMenuInlineKeyboard(callbackQuery.getMessage(), userDao);
                yield "Меню";
            }
            case "help" -> "Памагити";
            case "info" -> "Информация: " + callbackQuery.getFrom();
            default -> "";
        };
    }

    private void saveChosenService(UserDao userDao, ServiceItem serviceItem) {
        userDao.setLastServiceItem(serviceItem);
        usersRepository.save(userDao);
    }
}

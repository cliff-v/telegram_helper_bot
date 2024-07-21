package com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.telegramui.TelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.openai.OpenAiTelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.InlineKeyboardService;
import com.safronov.timofei.telegram.helper.bot.telegramui.yandex.YandexOpenAiFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TelegramDispatcherMessages extends AbstractTelegramDispatcher {

    protected TelegramDispatcherMessages(OpenAiTelegramFacade openAiTelegramFacade, YandexOpenAiFacade yandexOpenAiFacade, InlineKeyboardService inlineKeyboardService) {
        super(openAiTelegramFacade, yandexOpenAiFacade, inlineKeyboardService);
    }

    public void handle(UserDao userDao, String message) {
        selectServiceAndSendMessage(userDao, message);
    }

    private void selectServiceAndSendMessage(UserDao userDao, String message) {

        TelegramFacade facade = getTelegramFacade(userDao);

        facade.processMessage(userDao, message);
    }
}

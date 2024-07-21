package com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.telegramui.openai.OpenAiTelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.InlineKeyboardService;
import com.safronov.timofei.telegram.helper.bot.telegramui.yandex.YandexOpenAiFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
public class TelegramDispatcherCommands extends AbstractTelegramDispatcher{

    protected TelegramDispatcherCommands(OpenAiTelegramFacade openAiTelegramFacade, YandexOpenAiFacade yandexOpenAiFacade, InlineKeyboardService inlineKeyboardService) {
        super(openAiTelegramFacade, yandexOpenAiFacade, inlineKeyboardService);
    }

    public void handle(Update update, UserDao userDao, String command) {
        var message = update.getMessage();
        switch (command.substring(1)) {
            case "menu" -> sendMenuInlineKeyboard(message, userDao);
            case "choose_service" -> sendChooseSystemInlineKeyboard(message);
        }
    }

    @Override
    public void sendChooseSystemInlineKeyboard(MaybeInaccessibleMessage message) {
        super.sendChooseSystemInlineKeyboard(message);
    }
}

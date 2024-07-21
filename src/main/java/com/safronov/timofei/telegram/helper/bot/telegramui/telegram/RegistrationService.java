package com.safronov.timofei.telegram.helper.bot.telegramui.telegram;

import com.safronov.timofei.telegram.helper.bot.telegramui.openai.OpenAiTelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher.AbstractTelegramDispatcher;
import com.safronov.timofei.telegram.helper.bot.telegramui.yandex.YandexOpenAiFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
@Service
public class RegistrationService extends AbstractTelegramDispatcher {

    protected RegistrationService(OpenAiTelegramFacade openAiTelegramFacade, YandexOpenAiFacade yandexOpenAiFacade, InlineKeyboardService inlineKeyboardService) {
        super(openAiTelegramFacade, yandexOpenAiFacade, inlineKeyboardService);
    }

    public void sendRejectMessage(User user) {
        getTelegramBotComponent().send(user.getId(), "Ступай своей дорогой, странник\\. Входа нет тебе в сию " +
                "обитель");
    }
}

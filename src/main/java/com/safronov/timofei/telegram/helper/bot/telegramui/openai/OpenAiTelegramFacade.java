package com.safronov.timofei.telegram.helper.bot.telegramui.openai;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.telegramui.TelegramBotComponent;
import com.safronov.timofei.telegram.helper.bot.telegramui.TelegramFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAiTelegramFacade implements TelegramFacade {

    private TelegramBotComponent telegramBotComponent;

    @Autowired
    public void setTelegramBotComponent(@Lazy TelegramBotComponent telegramBotComponent) {
        this.telegramBotComponent = telegramBotComponent;
    }

    @Override
    public void processMessage(UserDao userDao, String message) {
        telegramBotComponent.send(userDao.getTgId(), userDao.getTgName() + ": " + message);
    }
}

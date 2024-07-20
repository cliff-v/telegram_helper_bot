package com.safronov.timofei.telegram.helper.bot.telegramui;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;

public interface TelegramFacade {
    void processMessage(UserDao userDao, String message);
}

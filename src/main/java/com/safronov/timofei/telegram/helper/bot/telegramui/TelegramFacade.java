package com.safronov.timofei.telegram.helper.bot.telegramui;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface TelegramFacade {
    void processMessage(UserDao userDao, String message);

    List<List<InlineKeyboardButton>> getServiceMenuInlineKeyboard();
}

package com.safronov.timofei.telegram.helper.bot.telegramui.yandex;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.telegramui.TelegramBotComponent;
import com.safronov.timofei.telegram.helper.bot.telegramui.TelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.InlineKeyboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YandexOpenAiFacade implements TelegramFacade {

    private final InlineKeyboardService inlineKeyboardService;
    private TelegramBotComponent telegramBotComponent;

    @Autowired
    public void setTelegramBotComponent(@Lazy TelegramBotComponent telegramBotComponent) {
        this.telegramBotComponent = telegramBotComponent;
    }

    @Override
    public void processMessage(UserDao userDao, String message) {
        System.out.println("ОЛОЛО ОЛОЛО Я ВОДИТЕЛЬ НЛО");
    }

    @Override
    public List<List<InlineKeyboardButton>> getServiceMenuInlineKeyboard() {
        return inlineKeyboardService.getYaGptMenuInlineKeyboardRows();
    }
}

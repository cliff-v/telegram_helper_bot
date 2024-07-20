package com.safronov.timofei.telegram.helper.bot.telegramui.yandex;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.telegramui.TelegramFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YandexOpenAiFacade implements TelegramFacade {
    @Override
    public void processMessage(UserDao userDao, String message) {
        System.out.println("ОЛОЛО ОЛОЛО Я ВОДИТЕЛЬ НЛО");
    }
}

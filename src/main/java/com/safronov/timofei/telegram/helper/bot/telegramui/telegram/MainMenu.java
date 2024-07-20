package com.safronov.timofei.telegram.helper.bot.telegramui.telegram;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.telegramui.TelegramBotComponent;
import com.safronov.timofei.telegram.helper.bot.telegramui.TelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.openai.OpenAiTelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.yandex.YandexOpenAiFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainMenu {

    private final OpenAiTelegramFacade openAiTelegramFacade;
    private final YandexOpenAiFacade yandexOpenAiFacade;
    private TelegramBotComponent telegramBotComponent;

    public static Map<Long, MainMenuSelectItem> mainMenuSelectItemMap = new HashMap<>();

    @Autowired
    public void setTelegramBotComponent(@Lazy TelegramBotComponent telegramBotComponent) {
        this.telegramBotComponent = telegramBotComponent;
    }

    public void processMessage(UserDao userDao, String message) {
        MainMenuSelectItem selectItem = mainMenuSelectItemMap.getOrDefault(
                userDao.getTgId(),
                showMenuAndGetSelectItem()
        );

        selectServiceAndSendMessage(userDao, message, selectItem);
    }

    private MainMenuSelectItem showMenuAndGetSelectItem() {
//        telegramBotComponent.sendInlineKeyboard("1");
        return MainMenuSelectItem.YA_GPT;
    }

    private void selectServiceAndSendMessage(UserDao userDao, String message, MainMenuSelectItem selectItem) {

        TelegramFacade facade = switch (selectItem) {
            case OPEN_AI -> openAiTelegramFacade;
            case YA_GPT -> yandexOpenAiFacade;
            case MAIN -> throw new RuntimeException();
        };

        facade.processMessage(userDao, message);
    }
}

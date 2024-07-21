package com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.telegramui.TelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.openai.OpenAiTelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.MainMenuSelectItem;
import com.safronov.timofei.telegram.helper.bot.telegramui.yandex.YandexOpenAiFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TelegramDispatcherMessages extends AbstractTelegramDispatcher {

    public TelegramDispatcherMessages(OpenAiTelegramFacade openAiTelegramFacade, YandexOpenAiFacade yandexOpenAiFacade) {
        super(openAiTelegramFacade, yandexOpenAiFacade);
    }

    public void handle(UserDao userDao, String message) {
        MainMenuSelectItem selectItem = mainMenuSelectItemMap.getOrDefault(
                userDao.getTgId(),
                showMenuAndGetSelectItem()
        );

        selectServiceAndSendMessage(userDao, message, selectItem);
    }

    private MainMenuSelectItem showMenuAndGetSelectItem() {
        return MainMenuSelectItem.YA_GPT;
    }

    private void selectServiceAndSendMessage(UserDao userDao, String message, MainMenuSelectItem selectItem) {

        TelegramFacade facade = switch (selectItem) {
            case OPEN_AI -> getOpenAiTelegramFacade();
            case YA_GPT -> getYandexOpenAiFacade();
            case MAIN -> throw new RuntimeException();
        };

        facade.processMessage(userDao, message);
    }
}

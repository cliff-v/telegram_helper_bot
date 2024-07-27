package com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.telegramui.TelegramBotComponent;
import com.safronov.timofei.telegram.helper.bot.telegramui.TelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.openai.OpenAiTelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.InlineKeyboardService;
import com.safronov.timofei.telegram.helper.bot.telegramui.yandex.YandexOpenAiFacade;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Getter
@Setter
public abstract class AbstractTelegramDispatcher {
    protected final OpenAiTelegramFacade openAiTelegramFacade;
    protected final YandexOpenAiFacade yandexOpenAiFacade;
    protected final InlineKeyboardService inlineKeyboardService;
    protected TelegramBotComponent telegramBotComponent;

    protected AbstractTelegramDispatcher(OpenAiTelegramFacade openAiTelegramFacade, YandexOpenAiFacade yandexOpenAiFacade, InlineKeyboardService inlineKeyboardService) {
        this.openAiTelegramFacade = openAiTelegramFacade;
        this.yandexOpenAiFacade = yandexOpenAiFacade;
        this.inlineKeyboardService = inlineKeyboardService;
    }

    @Autowired
    public void setTelegramBotComponent(@Lazy TelegramBotComponent telegramBotComponent) {
        this.telegramBotComponent = telegramBotComponent;
    }

    protected TelegramFacade getTelegramFacade(UserDao userDao) {
        return switch (userDao.getLastServiceItem()) {
            case OPEN_AI -> getOpenAiTelegramFacade();
            case YA_GPT -> getYandexOpenAiFacade();
        };
    }

    @SafeVarargs
    protected final void sendInlineKeyboard(MaybeInaccessibleMessage message,
                                            String text,
                                            List<List<InlineKeyboardButton>>... rowsArr) {
        sendInlineKeyboard(message.getChatId().toString(), text, rowsArr);
    }

    @SafeVarargs
    protected final void sendInlineKeyboard(String chatId,
                                            String text,
                                            List<List<InlineKeyboardButton>>... rowsArr) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (List<List<InlineKeyboardButton>> rowsList : rowsArr) {
            rows.addAll(rowsList);
        }

        var markup = getInlineKeyboardService().getInlineKeyboardMarkup(rows);

        sendMessage.setReplyMarkup(markup);
        sendMessage.setText(text);

        getTelegramBotComponent().send(sendMessage);
    }

    protected void sendMenuInlineKeyboard(MaybeInaccessibleMessage message, UserDao userDao) {
        sendInlineKeyboard(message,
                "Меню: ",
                getTelegramFacade(userDao).getServiceMenuInlineKeyboard(),
                getInlineKeyboardService().getMainMenuInlineKeyboardRows()
        );
    }

    protected void sendChooseSystemInlineKeyboard(MaybeInaccessibleMessage message) {
        sendInlineKeyboard(message,
                "Выберите активную рабочую систему: ",
                getInlineKeyboardService().getServiceButtonsInlineKeyboardRows());
    }
}

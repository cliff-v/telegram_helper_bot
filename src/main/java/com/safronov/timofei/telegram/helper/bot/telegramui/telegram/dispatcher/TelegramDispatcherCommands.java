package com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.telegramui.openai.OpenAiTelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.yandex.YandexOpenAiFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TelegramDispatcherCommands extends AbstractTelegramDispatcher{
    public TelegramDispatcherCommands(OpenAiTelegramFacade openAiTelegramFacade, YandexOpenAiFacade yandexOpenAiFacade) {
        super(openAiTelegramFacade, yandexOpenAiFacade);
    }

    public void handle(Update update, UserDao userDao, String command) {
        switch (command.substring(1)) {
            case "menu" -> sendInlineKeyboard(update.getMessage().getChatId().toString());
        }
    }

    private void sendInlineKeyboard(String chatId) {
        //TODO автозаполнение
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Choose a bot from the list below:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(InlineKeyboardButton.builder().text("Выбрать сервис").callbackData("choose_service").build());
        row1.add(InlineKeyboardButton.builder().text("Личная информация").callbackData("info").build());

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(InlineKeyboardButton.builder().text("Помощь").callbackData("help").build());

        rows.add(row1);
        rows.add(row2);

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        getTelegramBotComponent().send(message);
    }
}

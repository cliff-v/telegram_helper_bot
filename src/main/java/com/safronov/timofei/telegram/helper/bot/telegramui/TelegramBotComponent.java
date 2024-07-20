package com.safronov.timofei.telegram.helper.bot.telegramui;

import com.safronov.timofei.telegram.helper.bot.repositorie.UsersRepository;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.BotService;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.MainMenu;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TelegramBotComponent extends TelegramLongPollingBot {
    private final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

    private final String botToken;
    private final String username;
    private final UsersRepository usersRepository;
    private final BotService botService;
    private final MainMenu mainMenu;

    public TelegramBotComponent(
            @Value("${telegram.bot.token}") String botToken,
            UsersRepository usersRepository,
            BotService botService,
            MainMenu mainMenu
    ) throws TelegramApiException {
        super(botToken);
        this.botToken = botToken;
        this.mainMenu = mainMenu;
        this.username = "username";
        this.usersRepository = usersRepository;
        this.botService = botService;
    }

    @PostConstruct
    private void init() throws TelegramApiException {
        telegramBotsApi.registerBot(this);

    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
            return;
        }

        User user = update.getMessage().getFrom();
        String text = update.getMessage().getText();

        if (text.equals("/start") ) {
            sendInlineKeyboard(update.getMessage().getChatId().toString());
            return;
        }

        var userDao = usersRepository.findByTgId(user.getId())
                .orElse(null);

        if (userDao == null || !BotService.checkAccess(userDao)) {
            sendMessage(user.getId(), "Ступай своей дорогой, странник\\. Входа нет тебе в сию обитель");
            return;
        }
        userDao = botService.fillAndUpdateUsername(user, userDao);


        mainMenu.processMessage(userDao, text);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    public void sendMessage(Long who, String what) {
        SendMessage message = SendMessage.builder()
                .chatId(who.toString())
                .text(what)
                .parseMode("MarkdownV2")
                .build();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendInlineKeyboard(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Choose a bot from the list below:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(InlineKeyboardButton.builder().text("Вариант 1").callbackData("bot_1").build());
        row1.add(InlineKeyboardButton.builder().text("Вариант 2").callbackData("bot_2").build());

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(InlineKeyboardButton.builder().text("Вариант 3").callbackData("bot_3").build());

        rows.add(row1);
        rows.add(row2);

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("", e);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String responseText = switch (callbackData) {
            case "bot_1" -> "You selected Вариант 1";
            case "bot_2" -> "You selected Вариант 2";
            case "bot_3" -> "You selected Вариант 3";
            default -> "Unknown selection.";
        };

        try {
            // Удаляем кнопки и обновляем текст сообщения
            EditMessageText editMessage = new EditMessageText();
            editMessage.setChatId(chatId.toString());
            editMessage.setMessageId(messageId);
            editMessage.setText(responseText);

            execute(editMessage);

        } catch (TelegramApiException e) {
            log.error("", e);
        }
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("/help");
        row1.add("/reset");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("/stats");
        row2.add("/resend");

        keyboard.add(row1);
        keyboard.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}

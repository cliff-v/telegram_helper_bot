package com.safronov.timofei.telegram.helper.bot.telegramui;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.BotService;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.RegistrationService;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher.TelegramDispatcherButtons;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher.TelegramDispatcherCommands;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher.TelegramDispatcherMessages;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.TelegramCommandLoader;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Service
@Slf4j
public class TelegramBotComponent extends TelegramLongPollingBot {
    private final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

    private final String username;
    private final BotService botService;
    private final TelegramDispatcherMessages dispatcherMessages;
    private final TelegramCommandLoader telegramCommandLoader;
    private final TelegramDispatcherCommands dispatcherCommands;
    private final TelegramDispatcherButtons dispatcherButtons;
    private final RegistrationService registrationService;

    public TelegramBotComponent(@Value("${telegram.bot.token}") String botToken,
                                BotService botService, TelegramDispatcherMessages dispatcherMessages,
                                TelegramCommandLoader commandLoader,
                                TelegramDispatcherCommands dispatcherCommands,
                                TelegramDispatcherButtons dispatcherButtons, RegistrationService registrationService) throws TelegramApiException {
        super(botToken);
        this.dispatcherMessages = dispatcherMessages;
        this.telegramCommandLoader = commandLoader;
        this.dispatcherCommands = dispatcherCommands;
        this.dispatcherButtons = dispatcherButtons;
        this.registrationService = registrationService;
        this.username = "username";
        this.botService = botService;
    }

    @PostConstruct
    private void init() throws TelegramApiException {
        telegramBotsApi.registerBot(this);
        updateCommands();
    }

    public void updateCommands() {
        List<BotCommand> commands = telegramCommandLoader.loadCommands();
        if (commands != null) {
            try {
                this.execute(SetMyCommands.builder().commands(commands).build());
            } catch (TelegramApiException e) {
                log.error("", e);
            }
        }
    }

    @Override
    @Transactional
    public void onUpdateReceived(Update update) {
        User user = botService.getUser(update);
        var userDao = botService.getUserDao(user);

        if (userValidation(userDao)) {
            registrationService.sendRejectMessage(user);
            return;
        }
        userDao = botService.fillAndUpdateUsername(user, userDao);

        if (update.hasCallbackQuery()) {
            dispatcherButtons.handle(userDao, update.getCallbackQuery());
            return;
        }

        if (update.hasMessage()) {
            if (userDao.getLastServiceItem() == null) {
                dispatcherCommands.sendChooseSystemInlineKeyboard(update.getMessage());
                return;
            }

            String text = update.getMessage().getText().trim();

            if (text.isBlank()) {
                //TODO
                System.out.println("EMPTY!");
            }

            if (isCommand(text)) {
                dispatcherCommands.handle(update, userDao, text);
            } else {
                dispatcherMessages.handle(userDao, text);
            }
        }
    }

    private boolean userValidation(UserDao userDao) {
        return userDao == null || !BotService.checkAccess(userDao);
    }

    private boolean isCommand(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        String commandPattern = "^/[a-zA-Z0-9_]+$";
        return text.matches(commandPattern);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    public void send(Long who, String what) {
        SendMessage message = SendMessage.builder()
                .chatId(who.toString())
                .text(what)
                .parseMode("MarkdownV2")
                .build();

        try {
            updateCommands();
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("", e);
        }
    }

    public void send(EditMessageText editMessage) {
        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            log.error("", e);
        }
    }
}

package com.safronov.timofei.telegram.helper.bot.telegramui;

import com.safronov.timofei.telegram.helper.bot.repositorie.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Service
@Slf4j
public class MainMenu extends TelegramLongPollingBot {
    private final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

    private final String botToken;
    private final String username;
    private final UsersRepository usersRepository;
    private final BotService botService;

    public MainMenu(
            @Value("${telegram.bot.token}") String botToken,
//            @Value("${telegram.bot.username}") String username,
            UsersRepository usersRepository,
            BotService botService
    ) throws TelegramApiException {
        super(botToken);
        this.botToken = botToken;
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
        User user = update.getMessage().getFrom();
        String text = update.getMessage().getText();

        var userDao = usersRepository.findByTgId(user.getId()).orElse(null);

        if (userDao == null || !BotService.checkAccess(userDao)) {
            sendMessage(user.getId(), "Ступай своей дорогой, странник\\. Входа нет тебе в сию обитель");
            return;
        }
        userDao = botService.fillAndUpdateUsername(user, userDao);

        System.out.println(text + " " + userDao);

        sendMessage(userDao.getTgId(), userDao.getTgName() + ": " + text);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    public void sendMessage(Long who, String what) {
        SendMessage message = SendMessage.builder().chatId(who.toString()).text(what).parseMode("MarkdownV2").build();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

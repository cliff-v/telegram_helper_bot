package com.safronov.timofei.telegram.helper.bot.telegramui.telegram;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.model.db.UserType;
import com.safronov.timofei.telegram.helper.bot.repositorie.UsersRepository;
import com.safronov.timofei.telegram.helper.bot.telegramui.openai.OpenAiTelegramFacade;
import com.safronov.timofei.telegram.helper.bot.telegramui.telegram.dispatcher.AbstractTelegramDispatcher;
import com.safronov.timofei.telegram.helper.bot.telegramui.yandex.YandexOpenAiFacade;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@Slf4j
@Service

public class RegistrationService extends AbstractTelegramDispatcher {

    private final UsersRepository usersRepository;

    protected RegistrationService(OpenAiTelegramFacade openAiTelegramFacade, YandexOpenAiFacade yandexOpenAiFacade, InlineKeyboardService inlineKeyboardService, UsersRepository usersRepository) {
        super(openAiTelegramFacade, yandexOpenAiFacade, inlineKeyboardService);
        this.usersRepository = usersRepository;
    }

    @Transactional
    public boolean userRegistrationValidation(Update update, UserDao userDao, User user) {
        if (userDao == null && update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(
                "registration_request_send")) {
            sendRegistrationRequestToAdmins(user, update.getCallbackQuery());
            return true;
        } else if (userDao == null) {
            sendRegistrationRequestInlineKeyboard(
                    update.getMessage() == null
                            ? update.getCallbackQuery().getMessage()
                            : update.getMessage()
            );
            return true;
        } else if (isUserWaitAccept(userDao)) {
            sendRejectBeforeApprovalMessage(userDao);
            return true;
        }
        return false;
    }

    private boolean isUserWaitAccept(UserDao userDao) {
        return userDao.getType().equals(UserType.WAITING_APPROVE);
    }

    public void sendRejectMessage(User user) {
        getTelegramBotComponent().send(user.getId(), "Ступай своей дорогой, странник\\. Входа нет тебе в сию " +
                "обитель");
    }

    private void sendRegistrationRequestInlineKeyboard(MaybeInaccessibleMessage message) {
        sendInlineKeyboard(message,
                "Для отправки заявки на регистрацию нажмите кнопку ниже: ",
                getInlineKeyboardService().getRegistrationRequestInlineKeyboardRows()
        );
    }

    @Transactional
    public void sendRegistrationRequestToAdmins(User user, CallbackQuery callbackQuery) {
        UserDao userDao = createNewUserDao(user);


        List<UserDao> admins = usersRepository.findAllByType(UserType.ADMIN);

        for (UserDao admin : admins) {
            getTelegramBotComponent().send(admin.getTgId(), "Request @" + userDao.getTgName());
        }

        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId.toString())
                .text("Заявка успешно отправлена!")
                .messageId(messageId).build();

        getTelegramBotComponent().send(editMessage);
    }

    private UserDao createNewUserDao(User user) {
        UserDao userDao = UserDao.builder()
                .tgId(user.getId())
                .type(UserType.WAITING_APPROVE)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .tgName(user.getUserName())
                .build();

        return usersRepository.save(userDao);
    }

    private void sendRejectBeforeApprovalMessage(UserDao userDao) {
        getTelegramBotComponent().send(userDao.getTgId(), "Запрос принят! Ожидайте одобрения");
    }
}

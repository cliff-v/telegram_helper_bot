package com.safronov.timofei.telegram.helper.bot.telegramui.telegram;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.model.db.UserType;
import com.safronov.timofei.telegram.helper.bot.repositorie.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class BotService {
    private final UsersRepository usersRepository;
    public static boolean checkAccess(UserDao userDao) {
        return userDao.getType().equals(UserType.ADMIN)
                || userDao.getType().equals(UserType.USER);
    }

    public UserDao fillAndUpdateUsername(
            User userTelegram,
            UserDao userDao
    ) {
        userDao.setTgName(userTelegram.getUserName());
        userDao.setFirstName(userTelegram.getFirstName());
        userDao.setLastName(userTelegram.getLastName());

        return usersRepository.save(userDao);
    }

    public UserDao getUserDao(User user) {
        return usersRepository.findByTgId(user.getId()).orElse(null);
    }

    public User getUser(Update update) {
        var message = update.getMessage();
        var callback = update.getCallbackQuery();
        return message != null
                ? message.getFrom()
                : callback.getFrom();
    }
}

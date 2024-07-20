package com.safronov.timofei.telegram.helper.bot.telegramui.telegram;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.model.db.UserType;
import com.safronov.timofei.telegram.helper.bot.repositorie.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class BotService {
    private final UsersRepository usersRepository;
    public static boolean checkAccess(UserDao userDao) {
        return userDao.getType().equals(UserType.ADMIN.name())
                || userDao.getType().equals(UserType.USER.name());
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
}
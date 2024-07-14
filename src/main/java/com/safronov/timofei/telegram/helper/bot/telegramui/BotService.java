package com.safronov.timofei.telegram.helper.bot.telegramui;

import com.safronov.timofei.telegram.helper.bot.model.db.UserType;
import com.safronov.timofei.telegram.helper.bot.repositorie.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class BotService {
    private final UsersRepository usersRepository;
    public static boolean checkAccess(com.safronov.timofei.telegram.helper.bot.model.db.User userDao) {
        return userDao.getType().equals(UserType.ADMIN.name()) || userDao.getType().equals(UserType.USER.name());
    }

    public com.safronov.timofei.telegram.helper.bot.model.db.User fillAndUpdateUsername(
            User user,
            com.safronov.timofei.telegram.helper.bot.model.db.User userDao
    ) {
        userDao.setTgName(user.getUserName());
        userDao.setFirstName(user.getFirstName());
        userDao.setLastName(user.getLastName());

        return usersRepository.save(userDao);
    }
}

package com.safronov.timofei.telegram.helper.bot.repositorie;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import com.safronov.timofei.telegram.helper.bot.model.db.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<UserDao, Long> {
    Optional<UserDao> findByTgId(Long tgId);

    List<UserDao> findAllByType(UserType userType);
}

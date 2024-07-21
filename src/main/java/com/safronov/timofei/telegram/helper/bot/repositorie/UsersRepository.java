package com.safronov.timofei.telegram.helper.bot.repositorie;

import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UserDao, Long> {
    Optional<UserDao> findByTgId(Long tgId);
}

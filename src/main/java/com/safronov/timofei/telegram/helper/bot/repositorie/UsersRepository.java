package com.safronov.timofei.telegram.helper.bot.repositorie;

import com.safronov.timofei.telegram.helper.bot.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByTgId(Long tgId);
}

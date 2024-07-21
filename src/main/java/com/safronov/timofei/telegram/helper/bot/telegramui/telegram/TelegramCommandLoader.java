package com.safronov.timofei.telegram.helper.bot.telegramui.telegram;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class TelegramCommandLoader {

    @Value("${telegram.commands.path}")
    private String commandsPath;

    public List<BotCommand> loadCommands() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream(commandsPath)) {
            return mapper.readValue(is, new TypeReference<>() {});
        } catch (IOException e) {
            log.error("",  e);
            return null;
        }
    }
}
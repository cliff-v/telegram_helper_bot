package com.safronov.timofei.telegram.helper.bot.telegramui.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InlineKeyboardService {

    public final InlineKeyboardMarkup getInlineKeyboardMarkup(List<List<InlineKeyboardButton>> rows) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        markup.setKeyboard(rows);

        return markup;
    }

    public List<List<InlineKeyboardButton>> getMainMenuInlineKeyboardRows() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(InlineKeyboardButton.builder()
                .text("Выбрать сервис")
                .callbackData("choose_service")
                .build());
        row1.add(InlineKeyboardButton.builder()
                .text("Личная информация")
                .callbackData("info")
                .build());

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(InlineKeyboardButton.builder()
                .text("Помощь")
                .callbackData("help")
                .build());

        rows.add(row1);
        rows.add(row2);

        return rows;
    }

    public List<List<InlineKeyboardButton>> getServiceButtonsInlineKeyboardRows() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(InlineKeyboardButton.builder()
                .text("OpenAI")
                .callbackData("service_choose_openai")
                .build());

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(InlineKeyboardButton.builder()
                .text("YaGPT")
                .callbackData("service_choose_yandex")
                .build());

        rows.add(row1);
        rows.add(row2);

        return rows;
    }

    public List<List<InlineKeyboardButton>> getOpenAiMenuInlineKeyboardRows() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(InlineKeyboardButton.builder()
                .text("Сбросить историю")
                .callbackData("service_openai_reset")
                .build());

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(InlineKeyboardButton.builder()
                .text("Повторная отправка последнего сообщения")
                .callbackData("service_openai_resend")
                .build());

        rows.add(row1);
        rows.add(row2);

        return rows;
    }

    public List<List<InlineKeyboardButton>> getYaGptMenuInlineKeyboardRows() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(InlineKeyboardButton.builder()
                .text("Сбросить историю YA")
                .callbackData("service_yandex_reset")
                .build());

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(InlineKeyboardButton.builder()
                .text("Повторная отправка последнего сообщения YA")
                .callbackData("service_yandex_resend")
                .build());

        rows.add(row1);
        rows.add(row2);

        return rows;
    }

    public List<List<InlineKeyboardButton>> getRegistrationRequestInlineKeyboardRows() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(InlineKeyboardButton.builder()
                .text("Отправить запрос на регистрацию в телеграм-боте")
                .callbackData("registration_request_send")
                .build());

        rows.add(row1);

        return rows;
    }

    public List<List<InlineKeyboardButton>> getAdminsRegistrationRequestInlineKeyboardRows() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = List.of(
                InlineKeyboardButton.builder()
                .text("Одобрить")
                .callbackData("registration_request_accept")
                .build()
        );

        List<InlineKeyboardButton> row2 = List.of(
                InlineKeyboardButton.builder()
                        .text("Отказать")
                        .callbackData("registration_request_reject")
                        .build()
        );

        rows.add(row1);
        rows.add(row2);

        return rows;
    }
}
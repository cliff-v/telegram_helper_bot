# Используйте мавен для сборки вашего приложения
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src /app/src

# Объявляем аргументы, которые будут переданы во время сборки
ARG TELEGRAM_BOT_TOKEN
ARG TELEGRAM_BOT_USERNAME
ARG TELEGRAM_PERSON_ID
ARG ADMIN_TELEGRAM_PERSON_ID
ARG CHAT_GPT_TOKEN
ARG DATASOURCE_URL
ARG DATASOURCE_USERNAME
ARG DATASOURCE_PASSWORD

# Устанавливаем переменные окружения из аргументов сборки
ENV TELEGRAM_BOT_TOKEN=${TELEGRAM_BOT_TOKEN}
ENV TELEGRAM_BOT_USERNAME=${TELEGRAM_BOT_USERNAME}
ENV TELEGRAM_PERSON_ID=${TELEGRAM_PERSON_ID}
ENV ADMIN_TELEGRAM_PERSON_ID=${ADMIN_TELEGRAM_PERSON_ID}
ENV CHAT_GPT_TOKEN=${CHAT_GPT_TOKEN}
ENV DATASOURCE_URL=${DATASOURCE_URL}
ENV DATASOURCE_USERNAME=${DATASOURCE_USERNAME}
ENV DATASOURCE_PASSWORD=${DATASOURCE_PASSWORD}

RUN mvn clean package

# Используйте базовый образ Java для запуска собранного jar
FROM eclipse-temurin:17-jre
WORKDIR /app

# Копируем только jar из сборочного контейнера
COPY --from=build /app/target/telegram-helper-bot.jar /app/telegram-helper-bot.jar

# Открываем порт, если ваше приложение использует веб-сервер
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "telegram-helper-bot.jar"]

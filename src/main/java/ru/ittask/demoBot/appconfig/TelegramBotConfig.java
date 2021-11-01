package ru.ittask.demoBot.appconfig;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:telegram.properties")
public class TelegramBotConfig {

    private final String sendTo;
    private final String sendFrom;
    private final String subject;
    private final String name;
    private final String token;

    public TelegramBotConfig() {

        sendTo = "1011513@outlook.com";
        sendFrom = "aagrigoryev1979@gmail.com";
        subject = "Интех Support Bot: новая заявка";
        name = "GrishshaBot";
        token = " ";
    }
}

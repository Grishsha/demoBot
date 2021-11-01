package ru.ittask.IntekhBot.service.impl;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.IntekhBot.cache.DataCache;
import ru.ittask.IntekhBot.controller.Controller;
import ru.ittask.IntekhBot.model.BotState;
import ru.ittask.IntekhBot.service.InputMessage;
import ru.ittask.IntekhBot.service.handlers.registration.NewUserProfile;
import ru.ittask.IntekhBot.service.handlers.registration.NewUserRegistrationInt;

@Component
@NoArgsConstructor
public class NewUserRegistration implements InputMessage {

    @Override
    public SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        NewUserRegistrationInt newUserRegistration = null;
        if (update.hasMessage())
            newUserRegistration = new NewUserProfile(chatId, dataCache, update, message, controller);

        if (newUserRegistration != null)
            return newUserRegistration.process();
        else {
            message.setText("не поддерживается.");
            return message;
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.REGISTRATION;
    }
}

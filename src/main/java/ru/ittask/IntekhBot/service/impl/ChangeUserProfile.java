package ru.ittask.IntekhBot.service.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.IntekhBot.cache.DataCache;
import ru.ittask.IntekhBot.controller.Controller;
import ru.ittask.IntekhBot.model.BotState;
import ru.ittask.IntekhBot.service.InputMessage;
import ru.ittask.IntekhBot.service.handlers.change_user_profile.ChangeUserProfileData;
import ru.ittask.IntekhBot.service.handlers.change_user_profile.ChangeUserProfileInt;
import ru.ittask.IntekhBot.service.handlers.change_user_profile.HandleCallbackOfChangeUserProfile;

@Slf4j
@Component
@NoArgsConstructor
public class ChangeUserProfile implements InputMessage {

    @Override
    public SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        ChangeUserProfileInt changeUserProfile = null;
        if (update.hasMessage())
            if (update.getMessage().hasText() || update.getMessage().hasContact())
                changeUserProfile = new ChangeUserProfileData(chatId, dataCache, update, message, controller);

        if (update.hasCallbackQuery())
            changeUserProfile = new HandleCallbackOfChangeUserProfile(chatId, dataCache, update, message, controller);

        if (changeUserProfile != null)
            return changeUserProfile.process();
        else {
            message.setText("не поддерживается.");
            return message;
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CHANGE_USER_PROFILE;
    }
}

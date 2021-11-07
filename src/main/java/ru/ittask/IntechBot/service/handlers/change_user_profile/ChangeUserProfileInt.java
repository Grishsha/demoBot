package ru.ittask.IntechBot.service.handlers.change_user_profile;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ChangeUserProfileInt {
    SendMessage process();
}

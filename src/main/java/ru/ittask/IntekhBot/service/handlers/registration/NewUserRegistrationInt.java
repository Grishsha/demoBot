package ru.ittask.IntekhBot.service.handlers.registration;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface NewUserRegistrationInt {
    SendMessage process();
}

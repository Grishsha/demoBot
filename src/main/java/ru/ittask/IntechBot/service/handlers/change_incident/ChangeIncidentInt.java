package ru.ittask.IntechBot.service.handlers.change_incident;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ChangeIncidentInt {
    SendMessage process();
}

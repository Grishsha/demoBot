package ru.ittask.IntechBot.service.impl;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.IntechBot.cache.DataCache;
import ru.ittask.IntechBot.controller.Controller;
import ru.ittask.IntechBot.model.BotState;
import ru.ittask.IntechBot.service.InputMessage;
import ru.ittask.IntechBot.service.handlers.change_incident.ChangeIncidentData;
import ru.ittask.IntechBot.service.handlers.change_incident.ChangeIncidentInt;
import ru.ittask.IntechBot.service.handlers.change_incident.HandleCallbackOfChangeIncidentData;

@Component
@NoArgsConstructor
public class ChangeIncident implements InputMessage {

    @Override
    public SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        ChangeIncidentInt changeIncident = null;
        if (update.hasMessage())
            changeIncident = new ChangeIncidentData(chatId, dataCache, update, message, controller);
        if (update.hasCallbackQuery())
            changeIncident = new HandleCallbackOfChangeIncidentData(chatId, dataCache, update, message, controller);

        if (changeIncident != null)
            return changeIncident.process();
        else {
            message.setText("не поддерживается.");
            return message;
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CHANGE_INCIDENT_DATA;
    }
}

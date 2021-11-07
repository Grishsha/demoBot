package ru.ittask.IntechBot.service.impl;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.IntechBot.cache.DataCache;
import ru.ittask.IntechBot.controller.Controller;
import ru.ittask.IntechBot.model.BotState;
import ru.ittask.IntechBot.service.InputMessage;
import ru.ittask.IntechBot.service.handlers.new_incident.CreateIncidentInt;
import ru.ittask.IntechBot.service.handlers.new_incident.HandleCallbackOfNewIncFilling;
import ru.ittask.IntechBot.service.handlers.new_incident.HandleNewPhoto;
import ru.ittask.IntechBot.service.handlers.new_incident.NewIncidentFilling;

@Component
@NoArgsConstructor
public class NewIncident implements InputMessage {

    @Override
    public SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        CreateIncidentInt createIncident = null;
        if (update.hasMessage()) {
            if (update.getMessage().hasText())
                createIncident = new NewIncidentFilling(chatId, dataCache, update, message, controller);
            if (update.getMessage().hasPhoto())
                createIncident = new HandleNewPhoto(chatId, dataCache, update, message, controller);
        }

        if (update.hasCallbackQuery())
            createIncident = new HandleCallbackOfNewIncFilling(chatId, dataCache, update, message, controller);

        if (createIncident != null)
            return createIncident.process();
        else {
            message.setText("не поддерживается.");
            return message;
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.INC_FILLING;
    }
}

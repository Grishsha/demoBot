package ru.ittask.demoBot.service.handlers.message_types_handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.demoBot.cache.DataCache;
import ru.ittask.demoBot.controller.Controller;
import ru.ittask.demoBot.model.MessageType;

public interface HandleMessageByType {
    SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller);

    MessageType getHandlerName();
}

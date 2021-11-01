package ru.ittask.demoBot.service.handlers.text_message_handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.demoBot.cache.DataCache;
import ru.ittask.demoBot.controller.Controller;
import ru.ittask.demoBot.model.BotState;

public interface HandleTextMessageByStage {
    SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller);

    BotState getHandlerName();
}

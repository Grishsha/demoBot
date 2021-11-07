package ru.ittask.IntechBot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.IntechBot.cache.DataCache;
import ru.ittask.IntechBot.controller.Controller;
import ru.ittask.IntechBot.model.BotState;

public interface InputMessage {
    SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller);

    BotState getHandlerName();
}

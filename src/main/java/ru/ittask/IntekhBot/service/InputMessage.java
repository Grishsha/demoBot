package ru.ittask.IntekhBot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.IntekhBot.cache.DataCache;
import ru.ittask.IntekhBot.controller.Controller;
import ru.ittask.IntekhBot.model.BotState;

public interface InputMessage {
    SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller);

    BotState getHandlerName();
}

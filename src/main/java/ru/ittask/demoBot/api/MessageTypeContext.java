package ru.ittask.demoBot.api;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.demoBot.cache.DataCache;
import ru.ittask.demoBot.controller.Controller;
import ru.ittask.demoBot.model.BotState;
import ru.ittask.demoBot.model.MessageType;
import ru.ittask.demoBot.service.handlers.message_types_handlers.HandleMessageByType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessageTypeContext {
    private Map<MessageType, HandleMessageByType> messageTypeHandlers = new HashMap<>();

    public MessageTypeContext(List<HandleMessageByType> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageTypeHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(long chatId, DataCache dataCache, BotState currentBotState, Update update, SendMessage message, Controller controller) {
        HandleMessageByType currentMessageHandler = findMessageHandler(update);
        return currentMessageHandler.handle(chatId, dataCache, update, message, controller);
    }

    private HandleMessageByType findMessageHandler(Update update) {
        return messageTypeHandlers.get(ChoosingHandler(update));
    }

    private MessageType ChoosingHandler(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return MessageType.TEXT;
        }
        if (update.hasCallbackQuery()) {
            return MessageType.CALLBACK_QUERY;
        }
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            return MessageType.PHOTO;
        }
        return null;
    }
}

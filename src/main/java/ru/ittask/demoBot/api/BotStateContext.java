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
public class BotStateContext {
    private Map<MessageType, HandleMessageByType> messageHandlers = new HashMap<>();

    public BotStateContext(List<HandleMessageByType> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(long chatId, DataCache dataCache, BotState currentBotState, Update update, SendMessage message, Controller controller) {
        HandleMessageByType currentMessageHandler = findMessageHandler(currentBotState);
        return currentMessageHandler.handle(chatId, dataCache, update, message, controller);
    }

    private HandleMessageByType findMessageHandler(BotState currentState) {
        return messageHandlers.get(ChoosingHandler(currentState));
    }

    private BotState ChoosingHandler(BotState currentBotState) {
        switch (currentBotState) {
            case NOT_AUTHORISED:
            case REG_FIRST_NAME:
            case REG_SECOND_NAME:
            case REG_LAST_NAME:
            case REG_EMAIL:
            case REG_DEPARTMENT:
            case REG_PHONE:
                return BotState.REGISTRATION;
            case NEW_TT:
            case TT_DESCRIPTION:
            case TT_PHOTO:
            case TT_SERVICE:
            case TT_RESP_GROUP:
            case TT_RESP_PERSON:
                return BotState.TT_FILLING;
            case SHOW_HELP_MENU:
                return BotState.SHOW_HELP_MENU;
            default:
                return BotState.SHOW_MAIN_MENU;
        }
    }


}
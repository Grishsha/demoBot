package ru.ittask.IntekhBot.service.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.IntekhBot.cache.DataCache;
import ru.ittask.IntekhBot.controller.Controller;
import ru.ittask.IntekhBot.model.BotState;
import ru.ittask.IntekhBot.service.InputMessage;
import ru.ittask.IntekhBot.service.handlers.menu.MainMenuButtons;

@Slf4j
@Component
@NoArgsConstructor
public class ShowMainMenu implements InputMessage {//HandleTextMessageByStage {

    @Override
    public SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        MainMenuButtons mainMenuButtons = new MainMenuButtons();
        BotState currentBotState = dataCache.getUserCurrentBotState(chatId);
        message.setText(currentBotState.getDescription());
        message.setReplyMarkup(mainMenuButtons.getMainMenuKeyboard());

        return message;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}

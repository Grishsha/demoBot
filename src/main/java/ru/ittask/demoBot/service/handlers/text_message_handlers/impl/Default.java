package ru.ittask.demoBot.service.handlers.text_message_handlers.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.demoBot.cache.DataCache;
import ru.ittask.demoBot.controller.Controller;
import ru.ittask.demoBot.model.BotState;
import ru.ittask.demoBot.model.UserAttachments;
import ru.ittask.demoBot.model.UserProfileData;
import ru.ittask.demoBot.service.handlers.menu.InlineMenuButtons;
import ru.ittask.demoBot.service.handlers.menu.MainMenuButtons;
import ru.ittask.demoBot.service.handlers.text_message_handlers.HandleTextMessageByStage;

public class Default implements HandleTextMessageByStage {
    BotState currentBotState;
    Controller controller;
    MainMenuButtons mainMenuButtons;
    UserProfileData userProfileData;
    UserAttachments userAttachments;
    InlineMenuButtons inlineMenuButtons;

    public Default() {
        mainMenuButtons = new MainMenuButtons();
        inlineMenuButtons = new InlineMenuButtons();
    }
    public SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        userProfileData = dataCache.getUserProfileData(chatId);
        userAttachments = dataCache.getUserFilesData(chatId);
        currentBotState = dataCache.getUserCurrentBotState(chatId);

        switch (currentBotState) {
            //---------------------------------------------------------------------------------------------------
            case SHOW_HELP_MENU:
                message.setText("Пожалуйста, выбирите один из пунктов меню.");
                message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                break;
            case SHOW_MAIN_MENU:
                message.setText("Пожалуйста, выбирите один из пунктов меню.");
                message.setReplyMarkup(mainMenuButtons.getMainMenuKeyboard());
                break;
            case SHOW_USER_PROFILE:
                message.setText("Пожалуйста, выбирите один из пунктов меню.");
                message.setReplyMarkup(mainMenuButtons.getChangeMenuKeyboard());
                break;
        }
        return message;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}

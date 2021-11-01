package ru.ittask.demoBot.service.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.ittask.demoBot.cache.DataCache;
import ru.ittask.demoBot.controller.Controller;
import ru.ittask.demoBot.model.BotState;
import ru.ittask.demoBot.model.IncidentData;
import ru.ittask.demoBot.model.UserAttachments;
import ru.ittask.demoBot.model.UserProfileData;
import ru.ittask.demoBot.service.handlers.menu.InlineMenuButtons;
import ru.ittask.demoBot.service.handlers.menu.MainMenuButtons;


public class TTRequestFilledHandler {
    MainMenuButtons mainMenuButtons;
    InlineMenuButtons inlineMenuButtons;
    UserProfileData userProfileData;
    UserAttachments userAttachments;
    IncidentData incidentData;

    public TTRequestFilledHandler() {
        mainMenuButtons = new MainMenuButtons();
    }

    public SendMessage handle(long chatId, DataCache dataCache, SendMessage message, Controller controller) {
        userProfileData = dataCache.getUserProfileData(chatId);
        userAttachments = dataCache.getUserFilesData(chatId);
        incidentData = dataCache.getIncidentData(chatId);

        String str = incidentData.toString();
        str += userProfileData.toString();
        //str += "\n\n Отправить заявку в СУИТ или внести "
        /*if (userAttachments != null) {
            str += "\n" + userAttachments;
        }*/
        message.setText(str);

        message.setReplyMarkup(mainMenuButtons.getMainMenuKeyboard());
        //message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());

        dataCache.setUserCurrentBotState(chatId, BotState.SHOW_MAIN_MENU);
        controller.sendEmail(str, userAttachments);
        userAttachments.removeAllPaths();
        incidentData.removeIncidentData();//.removeTT();
        return message;
    }
}

package ru.ittask.IntechBot.service.handlers.change_incident;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.IntechBot.cache.DataCache;
import ru.ittask.IntechBot.controller.Controller;
import ru.ittask.IntechBot.model.BotState;
import ru.ittask.IntechBot.model.IncidentData;
import ru.ittask.IntechBot.model.UserAttachments;
import ru.ittask.IntechBot.model.UserProfileData;
import ru.ittask.IntechBot.service.handlers.menu.InlineMenuButtons;
import ru.ittask.IntechBot.service.handlers.menu.MainMenuButtons;
import ru.ittask.IntechBot.utils.TTRequestFilledHandler;

@Slf4j
public class ChangeIncidentData implements ChangeIncidentInt {//HandleTextMessageByStage {
    private BotState currentBotState;
    private MainMenuButtons mainMenuButtons;
    private UserProfileData userProfileData;
    private IncidentData incidentData;
    private UserAttachments userAttachments;
    private InlineMenuButtons inlineMenuButtons;
    private TTRequestFilledHandler requestFilledHandler;
    private long chatId;
    private DataCache dataCache;
    private Update update;
    private SendMessage message;
    private Controller controller;

    public ChangeIncidentData(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        mainMenuButtons = new MainMenuButtons();
        inlineMenuButtons = new InlineMenuButtons();
        this.chatId = chatId;
        this.dataCache = dataCache;
        this.update = update;
        this.message = message;
        this.controller = controller;
    }

    @Override
    public SendMessage process() {
        userAttachments = dataCache.getUserFilesData(chatId);
        currentBotState = dataCache.getUserCurrentBotState(chatId);
        incidentData = dataCache.getIncidentData(chatId);

        switch (currentBotState) {
            //---------------------------------------------------------------------------------------------------
            case INC_DESCRIPTION:
                userProfileData.setLastName(update.getMessage().getText());
                break;
            case INC_PHOTO:
                userProfileData.setLastName(update.getMessage().getText());
                break;
            case INC_SERVICE:
                userProfileData.setLastName(update.getMessage().getText());
                break;
            case INC_RESP_GROUP:
                userProfileData.setLastName(update.getMessage().getText());
                break;
            case INC_RESP_PERSON:
                userProfileData.setLastName(update.getMessage().getText());
                break;
            default:
                break;
        }

        message.setText(BotState.CHANGE_INCIDENT_DATA.getDescription());
        message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
        dataCache.setUserProfileData(chatId, userProfileData);

        return message;
    }
}
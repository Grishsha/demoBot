package ru.ittask.IntechBot.utils;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.ittask.IntechBot.cache.DataCache;
import ru.ittask.IntechBot.controller.Controller;
import ru.ittask.IntechBot.model.BotState;
import ru.ittask.IntechBot.model.IncidentData;
import ru.ittask.IntechBot.model.UserAttachments;
import ru.ittask.IntechBot.model.UserProfileData;
import ru.ittask.IntechBot.repository.UserProfileDataRepository;
import ru.ittask.IntechBot.service.handlers.menu.MainMenuButtons;

@Slf4j
public class TTRequestFilledHandler {
    private long chatId;
    private DataCache dataCache;
    private SendMessage message;
    private Controller controller;

    public TTRequestFilledHandler(long chatId, DataCache dataCache, SendMessage message, Controller controller) {
        this.chatId = chatId;
        this.dataCache = dataCache;
        this.message = message;
        this.controller = controller;
    }

    public SendMessage handle() {
        MainMenuButtons mainMenuButtons = new MainMenuButtons();
        UserProfileDataRepository repository = controller.getRepository();
        UserProfileData userProfileData = dataCache.getUserProfileData(chatId, repository);
        UserAttachments userAttachments = dataCache.getUserFilesData(chatId);
        IncidentData incidentData = dataCache.getIncidentData(chatId);

        String str = incidentData.toString();
        str += userProfileData.toString();

        try {
            controller.sendEmail(chatId, str, userAttachments);
            str += "\n\nОтправлена в СУИТ.";
        } catch (Exception e) {// MessagingException | TelegramApiException e){
            str += "\n\nНе отправлена в СУИТ." +
                    "\nВ процессе отправки произошла ошибка:\n\n";
            str += e;
        }

        message.setText(str);
        message.setReplyMarkup(mainMenuButtons.getMainMenuKeyboard());

        dataCache.setUserCurrentBotState(chatId, BotState.SHOW_MAIN_MENU);
        //userAttachments.removeAllPaths();
        incidentData.removeIncidentData();
        return message;
    }
}

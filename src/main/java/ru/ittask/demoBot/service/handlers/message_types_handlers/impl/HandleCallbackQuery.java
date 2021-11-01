package ru.ittask.demoBot.service.handlers.message_types_handlers.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.demoBot.cache.DataCache;
import ru.ittask.demoBot.controller.Controller;
import ru.ittask.demoBot.model.*;
import ru.ittask.demoBot.service.handlers.TTRequestFilledHandler;
import ru.ittask.demoBot.service.handlers.menu.InlineMenuButtons;
import ru.ittask.demoBot.service.handlers.menu.MainMenuButtons;
import ru.ittask.demoBot.service.handlers.message_types_handlers.HandleMessageByType;

@Slf4j
@Component
@NoArgsConstructor
public class HandleCallbackQuery implements HandleMessageByType {

    BotState currentBotState;
    PhaseState currentPhaseState;
    UserProfileData userProfileData;
    UserAttachments userAttachments;
    //@Autowired
    MainMenuButtons mainMenuButtons;
    //@Autowired
    InlineMenuButtons inlineMenuButtons;

    TTRequestFilledHandler TTRequestFilledHandler;
    //DataCache dataCache;

    @Override
    public SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        currentBotState = dataCache.getUserCurrentBotState(chatId);
        currentPhaseState = dataCache.getUserCurrentPhaseState(chatId);
        userProfileData = dataCache.getUserProfileData(chatId);
        userAttachments = dataCache.getUserFilesData(chatId);
        mainMenuButtons = new MainMenuButtons();
        inlineMenuButtons = new InlineMenuButtons();

        switch (currentBotState) {
            //---------------------------------------------------------------------------------------------------
            case NEW_TT:
                break;
            case TT_DESCRIPTION:
                if (update.getCallbackQuery().getData().equals("buttonYes")) {
                    if (userAttachments.getCount() == 0) {
                        message.setText("Загрузите фото. \nПо окончанию загрузки нажмите кнопку ГОТОВО или введите любой текст и нажмите отправить");
                        dataCache.setUserCurrentBotState(chatId, BotState.TT_PHOTO);
                        message.setReplyMarkup(mainMenuButtons.getDoneKeyboard());
                    } else {
                        message.setText("Введите название IT-сервиса");
                        message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                        dataCache.setUserCurrentBotState(chatId, BotState.TT_SERVICE);
                    }
                }
                if (update.getCallbackQuery().getData().equals("buttonNo")) {
                    message.setText("Знаете ли вы название IT-сервиса?\n" +
                            "нажмите ДА или НЕТ");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    dataCache.setUserCurrentBotState(chatId, BotState.TT_PHOTO);
                }
                break;
            case TT_PHOTO:
                if (update.getCallbackQuery().getData().equals("buttonYes")) {
                    message.setText("Введите название IT-сервиса");
                    message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                    dataCache.setUserCurrentBotState(chatId, BotState.TT_SERVICE);
                }
                if (update.getCallbackQuery().getData().equals("buttonNo")) {
                    message.setText("Знаете ли вы название рабочей группы?\n" +
                            "нажмите ДА или НЕТ");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    dataCache.setUserCurrentBotState(chatId, BotState.TT_SERVICE);
                }
                break;
            case TT_SERVICE:
                if (update.getCallbackQuery().getData().equals("buttonYes")) {
                    message.setText("Введите название рабочей группы");
                    message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                    dataCache.setUserCurrentBotState(chatId, BotState.TT_RESP_GROUP);
                }
                if (update.getCallbackQuery().getData().equals("buttonNo")) {
                    message.setText("Знаете ли вы ФИО исполнителя?\n" +
                            "нажмите ДА или НЕТ");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    dataCache.setUserCurrentBotState(chatId, BotState.TT_RESP_GROUP);
                }
                break;
            case TT_RESP_GROUP:
                if (update.getCallbackQuery().getData().equals("buttonYes")) {
                    message.setText("Введите ФИО исполнителя");
                    message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                    dataCache.setUserCurrentBotState(chatId, BotState.TT_RESP_PERSON);
                }
                if (update.getCallbackQuery().getData().equals("buttonNo")) {
                    message.setText("Ваша заявка оформлена.");
                    dataCache.setUserCurrentBotState(chatId, BotState.TT_REQUEST_FILLED);
                }
                break;
            case TT_RESP_PERSON:
                break;
            //case CHANGE_USER_PROFILE:

            default:
                if (currentPhaseState == PhaseState.CHANGE_USER_PROFILE) {
                    switch (update.getCallbackQuery().getData()) {
                        case "buttonLastName":
                            message.setText("Пожалуйста, введите вашу фамилию handle callback");
                            dataCache.setUserCurrentBotState(chatId, BotState.REG_LAST_NAME);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_USER_PROFILE);
                            break;
                        case "buttonFirstName":
                            message.setText("Пожалуйста, введите ваше имя");
                            dataCache.setUserCurrentBotState(chatId, BotState.REG_FIRST_NAME);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_USER_PROFILE);
                            break;
                        case "buttonSecondName":
                            message.setText("Пожалуйста, введите ваше отчество");
                            dataCache.setUserCurrentBotState(chatId, BotState.REG_SECOND_NAME);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_USER_PROFILE);
                            break;
                        case "buttonEMail":
                            message.setText("Пожалуйста, введите ваш e-mail");
                            dataCache.setUserCurrentBotState(chatId, BotState.REG_EMAIL);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_USER_PROFILE);
                            break;
                        case "buttonDepartment":
                            message.setText("Пожалуйста, введите ваше подразделение");
                            dataCache.setUserCurrentBotState(chatId, BotState.REG_DEPARTMENT);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_USER_PROFILE);
                            break;
                        case "buttonPhone":
                            message.setText("Пожалуйста, введите ваш телефон");
                            dataCache.setUserCurrentBotState(chatId, BotState.REG_PHONE);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_USER_PROFILE);
                            break;
                        case "buttonYes":
                            message.setText("Пожалуйста, выберите что вы хотите изменить");                            dataCache.setUserCurrentBotState(chatId, BotState.SHOW_MAIN_MENU);
                            //dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_USER_PROFILE);
                            message.setReplyMarkup(inlineMenuButtons.getUserProfileButtons());
                            break;
                        case "buttonNo":
                            message.setText("Изменения внесены callback");
                            dataCache.setUserCurrentBotState(chatId, BotState.SHOW_MAIN_MENU);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.DEFAULT);
                            message.setReplyMarkup(mainMenuButtons.getMainMenuKeyboard());
                            break;
                    }
                }
                if (currentPhaseState == PhaseState.CHANGE_INCIDENT_DATA) {
                    switch (update.getCallbackQuery().getData()) {
                        case "buttonDescription":
                            message.setText("Пожалуйста, введите вашу фамилию handle callback");
                            dataCache.setUserCurrentBotState(chatId, BotState.TT_DESCRIPTION);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_INCIDENT_DATA);
                            break;
                        case "buttonService":
                            message.setText("Пожалуйста, введите название IT сервиса");
                            dataCache.setUserCurrentBotState(chatId, BotState.TT_SERVICE);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_INCIDENT_DATA);
                            break;
                        case "buttonRespGroup":
                            message.setText("Пожалуйста, введите название Рабочей группы");
                            dataCache.setUserCurrentBotState(chatId, BotState.TT_RESP_GROUP);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_INCIDENT_DATA);
                            break;
                        case "buttonRespPerson":
                            message.setText("Пожалуйста, введите ФИО испонителя");
                            dataCache.setUserCurrentBotState(chatId, BotState.TT_RESP_PERSON);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_INCIDENT_DATA);
                            break;
                        case "buttonYes":
                            message.setText("Пожалуйста, выберите что вы хотите изменить");
                            dataCache.setUserCurrentBotState(chatId, BotState.SHOW_MAIN_MENU);
                            //dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_USER_PROFILE);
                            message.setReplyMarkup(inlineMenuButtons.getUserProfileButtons());
                            break;
                        case "buttonNo":
                            message.setText("Изменения внесены");
                            dataCache.setUserCurrentBotState(chatId, BotState.SHOW_MAIN_MENU);
                            dataCache.setUserCurrentPhaseState(chatId, PhaseState.DEFAULT);
                            message.setReplyMarkup(mainMenuButtons.getMainMenuKeyboard());
                            break;
                    }
                }
                break;
        }

        if (dataCache.getUserCurrentBotState(chatId) == BotState.TT_REQUEST_FILLED) {
            TTRequestFilledHandler = new TTRequestFilledHandler();
            TTRequestFilledHandler.handle(chatId, dataCache, message, controller);
        }

        return message;//replyMessagesService.createReplyMessage(message, update, dataCache, chatId, controller);
    }

    @Override
    public MessageType getHandlerName() {
        return MessageType.CALLBACK_QUERY;
    }
}

package ru.ittask.demoBot.service.handlers.text_message_handlers.impl;

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
import ru.ittask.demoBot.service.handlers.text_message_handlers.HandleTextMessageByStage;

@Slf4j
@Component
public class IncidentFilling implements HandleTextMessageByStage {
    BotState currentBotState;
    PhaseState currentPhaseState;
    MainMenuButtons mainMenuButtons;
    UserProfileData userProfileData;
    IncidentData incidentData;
    UserAttachments userAttachments;
    InlineMenuButtons inlineMenuButtons;
    TTRequestFilledHandler TTRequestFilledHandler;

    public IncidentFilling() {
        mainMenuButtons = new MainMenuButtons();
        inlineMenuButtons = new InlineMenuButtons();
    }

    @Override
    public SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        userProfileData = dataCache.getUserProfileData(chatId);
        userAttachments = dataCache.getUserFilesData(chatId);
        currentBotState = dataCache.getUserCurrentBotState(chatId);
        currentPhaseState = dataCache.getUserCurrentPhaseState(chatId);
        incidentData = dataCache.getIncidentData(chatId);

        switch (currentBotState) {
            //---------------------------------------------------------------------------------------------------
            case NEW_TT:
                message.setText("Приступим к оформлению заявки.\n\n\n" +
                        "Пожалуйста, введите описание вашей заявки. Обратите внимание, что описание может содержать " +
                        "текст и фотографии (картинки).");
                message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());

                dataCache.setUserCurrentBotState(chatId, BotState.TT_DESCRIPTION);
                break;
            case TT_DESCRIPTION:
                //сделать проверку что в ответе текст
                incidentData.setDescription(update.getMessage().getText());

                if (currentPhaseState == PhaseState.CHANGE_INCIDENT_DATA) {
                    message.setText("Хотетие поменять что то еще?");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    break;
                }

                if (userAttachments.getCount() == 0)
                    message.setText("Хотите к описанию добавить фото?\n" +
                            "нажмите ДА или НЕТ");
                else {
                    message.setText("Знаете ли вы название IT-сервиса?\n" +
                            "нажмите ДА или НЕТ");
                    dataCache.setUserCurrentBotState(chatId, BotState.TT_PHOTO);
                }
                message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());

                break;
            case TT_PHOTO:
                if (userAttachments.getCount() > 0) {
                    message.setText("Знаете ли вы название IT-сервиса?\n" +
                            "нажмите ДА или НЕТ");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                }
                break;
            case TT_SERVICE:
                incidentData.setService(update.getMessage().getText());
                if(currentPhaseState == PhaseState.CHANGE_INCIDENT_DATA){
                    message.setText("Хотетие поменять что то еще?");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    break;
                }
                message.setText("Знаете ли вы название Рабочей группы?\n" +
                        "нажмите ДА или НЕТ");
                message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());

                break;
            case TT_RESP_GROUP:
                incidentData.setRespGroup(update.getMessage().getText());
                message.setText("Знаете ли вы ФИО исполнителя?\n" +
                        "нажмите ДА или НЕТ");
                message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());

                break;
            case TT_RESP_PERSON:
                incidentData.setRespPerson(update.getMessage().getText());
                if(currentPhaseState == PhaseState.CHANGE_INCIDENT_DATA){
                    message.setText("Хотетие поменять что то еще?");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    break;
                }
                message.setText("Ваша заявка оформлена.");
                message.setReplyMarkup(mainMenuButtons.getMainMenuKeyboard());
                dataCache.setUserCurrentBotState(chatId, BotState.TT_REQUEST_FILLED);
                break;
            default:
                break;
        }

        if (dataCache.getUserCurrentBotState(chatId) == BotState.TT_REQUEST_FILLED) {
            TTRequestFilledHandler = new TTRequestFilledHandler();
            TTRequestFilledHandler.handle(chatId, dataCache, message, controller);
        }

        return message;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.TT_FILLING;
    }
}
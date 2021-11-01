package ru.ittask.demoBot.service.handlers.text_message_handlers.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.demoBot.cache.DataCache;
import ru.ittask.demoBot.controller.Controller;
import ru.ittask.demoBot.model.BotState;
import ru.ittask.demoBot.model.PhaseState;
import ru.ittask.demoBot.model.UserAttachments;
import ru.ittask.demoBot.model.UserProfileData;
import ru.ittask.demoBot.service.handlers.menu.InlineMenuButtons;
import ru.ittask.demoBot.service.handlers.menu.MainMenuButtons;
import ru.ittask.demoBot.service.handlers.text_message_handlers.HandleTextMessageByStage;

@Slf4j
@Component
public class Registration implements HandleTextMessageByStage {
    BotState currentBotState;
    PhaseState currentPhaseState;
    Controller controller;
    MainMenuButtons mainMenuButtons;
    UserProfileData userProfileData;
    UserAttachments userAttachments;
    InlineMenuButtons inlineMenuButtons;

    public Registration() {
        mainMenuButtons = new MainMenuButtons();
        inlineMenuButtons = new InlineMenuButtons();
    }

    @Override
    public SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        currentBotState = dataCache.getUserCurrentBotState(chatId);
        currentPhaseState = dataCache.getUserCurrentPhaseState(chatId);
        userProfileData = dataCache.getUserProfileData(chatId);

        switch (currentBotState) {
            case NOT_AUTHORISED:
                message.setText("Для оформления заявки в техническую поддержку вам требуется пройти процедуру регистрации\n" +
                            "\nПожалуйста, введите вашу фамилию");
                message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                dataCache.setUserCurrentBotState(chatId, BotState.REG_LAST_NAME);
                break;
            case REG_LAST_NAME:
                //сделать проверку что в ответет текст
                userProfileData.setLastName(update.getMessage().getText());
                if(currentPhaseState == PhaseState.CHANGE_USER_PROFILE){
                    message.setText("Хотетие поменять что то еще?");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    break;
                }
                message.setText("Пожалуйста, введите ваше имя");
                message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                saveUserProfileData(chatId, dataCache, userProfileData, BotState.REG_FIRST_NAME);
                break;
            case REG_FIRST_NAME:
                //сделать проверку что в ответет текст
                userProfileData.setFirstName(update.getMessage().getText());
                if(currentPhaseState == PhaseState.CHANGE_USER_PROFILE){
                    message.setText("Хотетие поменять что то еще?");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    break;
                }
                message.setText("Пожалуйста, введите ваше отчество");
                message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                saveUserProfileData(chatId, dataCache, userProfileData, BotState.REG_SECOND_NAME);
                break;
            case REG_SECOND_NAME:
                //сделать проверку что в ответет текст
                userProfileData.setSecondName(update.getMessage().getText());
                message.setText("Пожалуйста, введите ваш работчий e-mail");
                if(currentPhaseState == PhaseState.CHANGE_USER_PROFILE){
                    message.setText("Хотетие поменять что то еще?");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    break;
                }
                message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                saveUserProfileData(chatId, dataCache, userProfileData, BotState.REG_EMAIL);
                break;
            case REG_EMAIL:
                //сделать проверку что в ответет текст
                if (!update.getMessage().getText()
                        .matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
                    message.setText("введенный вами текст не похож на адрес почты.\n" +
                            "Попробуйте еще раз.");
                    break;
                }
                userProfileData.setEMail(update.getMessage().getText());
                if(currentPhaseState == PhaseState.CHANGE_USER_PROFILE){
                    message.setText("Хотетие поменять что то еще?");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    break;
                }
                message.setText("Пожалуйста введите ваше подразделение в Интех");
                message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                saveUserProfileData(chatId, dataCache, userProfileData, BotState.REG_DEPARTMENT);
                break;
            case REG_DEPARTMENT:
                //сделать проверку что в ответет текст
                userProfileData.setDepartment(update.getMessage().getText());
                if(currentPhaseState == PhaseState.CHANGE_USER_PROFILE){
                    message.setText("Хотетие поменять что то еще?");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    break;
                }
                message.setText("Пожалуйста введите ваш контактный телефон.\n" +
                        "Потребуется для связи с вами если у поддержки возникнут вопросы");
                message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                saveUserProfileData(chatId, dataCache, userProfileData, BotState.REG_PHONE);
                break;
            case REG_PHONE:
                //сделать проверку что в ответет текст
                if (!update.getMessage().getText()
                        .matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")) {
                    message.setText("введенный вами текст не похож на номер телефона.\n" +
                            "Попробуйте еще раз.");
                    break;
                }
                userProfileData.setPhone(update.getMessage().getText());
                if(currentPhaseState == PhaseState.CHANGE_USER_PROFILE){
                    message.setText("Хотетие поменять что то еще?");
                    message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                    break;
                }
                message.setText("Процесс регистрации завершен. Спасибо.\n" +
                        "Теперь вы можете оформлять заявки в техническую поддержку.");
                message.setReplyMarkup(mainMenuButtons.getMainMenuKeyboard());
                saveUserProfileData(chatId, dataCache, userProfileData, BotState.USER_PROFILE_FILLED);
                break;
            default:
                break;
        }

        return message;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.REGISTRATION;
    }

    private void saveUserProfileData(long chatId, DataCache dataCache, UserProfileData userProfileData, BotState nextState) {
        dataCache.setUserProfileData(chatId, userProfileData);
        dataCache.setUserCurrentBotState(chatId, nextState);
    }
}

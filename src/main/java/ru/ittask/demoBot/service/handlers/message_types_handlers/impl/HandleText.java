package ru.ittask.demoBot.service.handlers.message_types_handlers.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.demoBot.cache.DataCache;
import ru.ittask.demoBot.controller.Controller;
import ru.ittask.demoBot.model.BotState;
import ru.ittask.demoBot.model.MessageType;
import ru.ittask.demoBot.model.PhaseState;
import ru.ittask.demoBot.model.UserProfileData;
import ru.ittask.demoBot.service.CurrentProcessPhase;
import ru.ittask.demoBot.service.handlers.menu.InlineMenuButtons;
import ru.ittask.demoBot.service.handlers.menu.MainMenuButtons;
import ru.ittask.demoBot.service.handlers.message_types_handlers.HandleMessageByType;
import ru.ittask.demoBot.service.handlers.text_message_handlers.HandleTextMessageByStage;
import ru.ittask.demoBot.service.handlers.text_message_handlers.impl.Default;
import ru.ittask.demoBot.service.handlers.text_message_handlers.impl.Registration;
import ru.ittask.demoBot.service.handlers.text_message_handlers.impl.IncidentFilling;

@Slf4j
@Component
@NoArgsConstructor
public class HandleText implements HandleMessageByType {
    MainMenuButtons mainMenuButtons;
    InlineMenuButtons inlineMenuButtons;
    HandleTextMessageByStage handleTextMessageByStage;
    CurrentProcessPhase currentProcessPhase;
    BotState currentBotState;
    PhaseState currentPhaseState;
    UserProfileData userProfileData;

    @Override
    public SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        //dataCache = new UserDataCache();
        mainMenuButtons = new MainMenuButtons();
        inlineMenuButtons = new InlineMenuButtons();
        currentProcessPhase = new CurrentProcessPhase();
        currentBotState = dataCache.getUserCurrentBotState(chatId);
        currentPhaseState = dataCache.getUserCurrentPhaseState(chatId);
        userProfileData = dataCache.getUserProfileData(chatId);

        switch (update.getMessage().getText()) {
            case "/start":
                message.setReplyMarkup(mainMenuButtons.getMainMenuKeyboard());
                if (userProfileData.getLastName() == null) {
                    message.setText("Вас приветствует чат бот поддержки РСХБ.\nЯ помогу вам оформить заявку в службу ИТ.");
                    currentBotState = BotState.NOT_AUTHORISED;
                } else {
                    message.setText("Здравствуйте " + userProfileData.getFirstName() + " " + userProfileData.getSecondName() + "!");
                    currentBotState = BotState.SHOW_MAIN_MENU;
                }
                dataCache.setUserCurrentBotState(chatId, currentBotState);
                break;
            case "Оформить заявку":
                if (userProfileData.getLastName() == null) {
                    currentBotState = BotState.NOT_AUTHORISED;
                    dataCache.setUserCurrentBotState(chatId, currentBotState);
                    handleTextMessageByStage = new Registration();
                    message = handleTextMessageByStage.handle(chatId, dataCache, update, message, controller);
                    //message = replyMessagesService.createReplyMessage(message, update, dataCache, update.getMessage().getFrom().getId(), controller);
                } else {
                    currentBotState = BotState.NEW_TT;
                    dataCache.setUserCurrentBotState(chatId, currentBotState);
                    handleTextMessageByStage = new IncidentFilling();
                    message = handleTextMessageByStage.handle(chatId, dataCache, update, message, controller);

                    //message = replyMessagesService.createReplyMessage(message, update, dataCache, chatId, controller);
                }
                break;
            case "Мои регистрационные данные":
                String str = "Имя: " + userProfileData.getFirstName() +
                        "\nОтчество: " + userProfileData.getSecondName() +
                        "\nФамилия: " + userProfileData.getLastName() +
                        "\ne-mail: " + userProfileData.getEMail() +
                        "\nПодразделение: " + userProfileData.getDepartment() +
                        "\nТелефон: " + userProfileData.getPhone();
                message.setText(str);
                message.setReplyMarkup(mainMenuButtons.getChangeMenuKeyboard());
                currentBotState = BotState.SHOW_USER_PROFILE;
                dataCache.setUserCurrentBotState(chatId, currentBotState);
                break;
            case "Изменить":
                message.setText("Пожалуйста, выберите что вы хотите изменить");
                //message.setText("Пожалуйста, введите вашу фамилию");
                message.setReplyMarkup(mainMenuButtons.getSimpleMenuKeyboard());
                //currentBotState = BotState.CHANGE_USER_PROFILE;
                //dataCache.setUserCurrentBotState(chatId, currentBotState);
                //handleTextMessageByStage = new ChangeUserProfileData();
                //message = handleTextMessageByStage.handle(chatId, dataCache, update, message, controller);

                dataCache.setUserCurrentBotState(chatId, BotState.CHANGE_USER_PROFILE);
                dataCache.setUserCurrentPhaseState(chatId, PhaseState.CHANGE_USER_PROFILE);
                message.setReplyMarkup(inlineMenuButtons.getUserProfileButtons());

                break;
            case "Помощь":
                message.setText("Здесь будет краткое описание того, как оформить заявку и т.д.");
                log.error("__________________________________________________________________________________________");
                message.setReplyMarkup(mainMenuButtons.getMainMenuKeyboard());
                currentBotState = BotState.SHOW_HELP_MENU;
                dataCache.setUserCurrentBotState(chatId, currentBotState);
                break;
            case "В Главное Меню":
                message.setText("Вы находитесь в главном меню.");
                log.error("__________________________________________________________________________________________");
                message.setReplyMarkup(mainMenuButtons.getMainMenuKeyboard());
                currentBotState = BotState.SHOW_MAIN_MENU;
                dataCache.setUserCurrentBotState(chatId, currentBotState);
                break;
            case "Готово":
                message.setText("Знаете ли вы название IT-сервиса?\n" +
                        "нажмите ДА или НЕТ");
                message.setReplyMarkup(inlineMenuButtons.getInlineMessageButtons());
                break;
            default:
                //if (currentProcessPhase.get(currentBotState) == BotState.CHANGE_USER_PROFILE)
                //    handleTextMessageByStage = new ChangeUserProfileData();
                if (currentProcessPhase.get(currentBotState) == BotState.REGISTRATION)
                    handleTextMessageByStage = new Registration();
                if (currentProcessPhase.get(currentBotState) == BotState.TT_FILLING)
                    handleTextMessageByStage = new IncidentFilling();
                if (currentProcessPhase.get(currentBotState) == BotState.SHOW_HELP_MENU
                        || currentProcessPhase.get(currentBotState) == BotState.SHOW_MAIN_MENU
                        || currentProcessPhase.get(currentBotState) == BotState.SHOW_USER_PROFILE){
                    handleTextMessageByStage = new Default();
                }
                if (handleTextMessageByStage != null)
                    message = handleTextMessageByStage.handle(chatId, dataCache, update, message, controller);
                break;
        }
        try {
            if (message.getText().isEmpty())
                message.setText("Тут почему то пусто");
        } catch (NullPointerException e){
            //currentProcessPhase.get(currentBotState).toString();
            message.setText("Пожалуйста, выбирите один из пунктов меню.");
            //message.setText(currentBotState.toString());
        }
        return message;
    }

    @Override
    public MessageType getHandlerName() {
        return MessageType.TEXT;
    }
}

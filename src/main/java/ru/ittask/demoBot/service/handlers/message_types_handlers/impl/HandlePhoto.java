package ru.ittask.demoBot.service.handlers.message_types_handlers.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ittask.demoBot.cache.DataCache;
import ru.ittask.demoBot.controller.Controller;
import ru.ittask.demoBot.model.BotState;
import ru.ittask.demoBot.model.MessageType;
import ru.ittask.demoBot.model.UserAttachments;
import ru.ittask.demoBot.service.handlers.menu.MainMenuButtons;
import ru.ittask.demoBot.service.handlers.message_types_handlers.HandleMessageByType;

@Slf4j
@Component
@NoArgsConstructor
public class HandlePhoto implements HandleMessageByType {
    MainMenuButtons mainMenuButtons;
    UserAttachments userAttachments;
    BotState currentBotState;

    @Override
    public SendMessage handle(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        currentBotState = dataCache.getUserCurrentBotState(chatId);
        userAttachments = dataCache.getUserFilesData(chatId);
        mainMenuButtons = new MainMenuButtons();

        if (currentBotState == BotState.TT_DESCRIPTION || currentBotState == BotState.TT_PHOTO) {
            String fp = null;
            try {
                fp = controller.getFilePath(update);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                message.setText("К сожалению добавить фотографию к заявке не удалось.\n" +
                        "Попробуйте еще раз.");
                return message;
            }
            userAttachments.addNewAttachmentPath(fp);
            dataCache.setUserAttachments(update.getMessage().getChatId(), userAttachments);
            message.setText("Фотография добавлена к заявке");
            if (currentBotState == BotState.TT_PHOTO) {
                message.setText("Фотография добавлена к заявке.\n" +
                        "По окончанию загрузки нажмите кнопку ГОТОВО или введите любой текст и нажмите отправить");
                message.setReplyMarkup(mainMenuButtons.getDoneKeyboard());
            } else {
                message.setText("Фотография добавлена к заявке.\n" +
                        "Не забудьте добавить описание к вашей заявке.");
            }
            //controller.sendSimpleEmail("1011513@outlook.com", "Test", "lkjhlkhlkjhlkjh", update, fp);
            return message;
        } else {
            message.setText("Здесь нельзя вставлять картинки");
            return message;
        }
    }

    @Override
    public MessageType getHandlerName() {
        return MessageType.PHOTO;
    }
}

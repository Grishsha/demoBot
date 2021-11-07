package ru.ittask.IntechBot.service.handlers.new_incident;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ittask.IntechBot.cache.DataCache;
import ru.ittask.IntechBot.controller.Controller;
import ru.ittask.IntechBot.model.BotState;
import ru.ittask.IntechBot.model.UserAttachments;
import ru.ittask.IntechBot.service.handlers.menu.MainMenuButtons;

@Slf4j
@NoArgsConstructor
public class HandleNewPhoto implements CreateIncidentInt {//HandleMessageByType {
    private long chatId;
    private DataCache dataCache;
    private Update update;
    private SendMessage message;
    private Controller controller;

    public HandleNewPhoto(long chatId, DataCache dataCache, Update update, SendMessage message, Controller controller) {
        this.chatId = chatId;
        this.dataCache = dataCache;
        this.update = update;
        this.message = message;
        this.controller = controller;
    }

    @Override
    public SendMessage process() {
        BotState currentBotState = dataCache.getUserCurrentBotState(chatId);
        UserAttachments userAttachments = dataCache.getUserFilesData(chatId);
        MainMenuButtons mainMenuButtons = new MainMenuButtons();

        if (currentBotState == BotState.INC_DESCRIPTION || currentBotState == BotState.INC_PHOTO) {
            String fp;
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
            if (currentBotState == BotState.INC_PHOTO) {
                message.setText("Фотография добавлена к заявке.\n\n" +
                        "По окончании загрузки нажмите кнопку ГОТОВО или введите любой текст и нажмите отправить");
                message.setReplyMarkup(mainMenuButtons.getDoneKeyboard());
            } else {
                message.setText("Фотография добавлена к заявке.\n" +
                        "Не забудьте добавить описание к вашей заявке.");
            }
            return message;
        } else {
            message.setText("Здесь нельзя вставлять картинки");
            return message;
        }
    }
}

package ru.ittask.demoBot.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ittask.demoBot.cache.DataCache;
import ru.ittask.demoBot.controller.Controller;
import ru.ittask.demoBot.service.handlers.message_types_handlers.HandleMessageByType;
import ru.ittask.demoBot.service.handlers.message_types_handlers.impl.HandleCallbackQuery;
import ru.ittask.demoBot.service.handlers.message_types_handlers.impl.HandlePhoto;
import ru.ittask.demoBot.service.handlers.message_types_handlers.impl.HandleText;


//----------------------------------------------------------------------------------------------------
//Определяем тип сообщения и выбираем для каждого типа свой обработчик сообщений
//
//возвращаем полностью сформированное сообщение для отправки в телеграм.
//----------------------------------------------------------------------------------------------------


@Slf4j
@Component

@NoArgsConstructor
public class Service {
    //@Autowired
    HandleMessageByType handleMessageByType;
    //@Autowired
    //DataCache dataCache;

    public SendMessage handleUpdate(Update update, SendMessage message, Controller controller, DataCache dataCache) {//, SendMessage message, DataCache dataCache, Controller controller) throws TelegramApiException, MessagingException {

        //----------------------------------------------------------------------------------------------------
        //Если входящее сообщение содержит текст или нажатие кнопок главного меню (они тоже шлют текст)
        //Создаем нужный обработчик, передаем в него требуемые параметры и запускаем обработку.
        //----------------------------------------------------------------------------------------------------
        long chatId;

        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            message.setChatId(String.valueOf(chatId));

            handleMessageByType = new HandleText();
            message = handleMessageByType.handle(chatId, dataCache, update, message, controller);

            return message;
        }
        //----------------------------------------------------------------------------------------------------
        //Если входящее сообщение содержит информацию о нажатии inline кнопок
        //Создаем нужный обработчик, передаем в него требуемые параметры и запускаем обработку.
        //----------------------------------------------------------------------------------------------------
        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getFrom().getId();
            message.setChatId(String.valueOf(chatId));

            handleMessageByType = new HandleCallbackQuery();
            message = handleMessageByType.handle(chatId, dataCache, update, message, controller);

            return message;
        }
        //----------------------------------------------------------------------------------------------------
        //Если входящее сообщение содержит фото
        //Создаем нужный обработчик, передаем в него требуемые параметры и запускаем обработку.
        //----------------------------------------------------------------------------------------------------
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            chatId = update.getMessage().getChatId();
            message.setChatId(String.valueOf(chatId));
            handleMessageByType = new HandlePhoto();
            message = handleMessageByType.handle(chatId, dataCache, update, message, controller);

            return message;
        }
        //Если ни один из обработчиков не подошел, то возвращаем null.
        return null;
    }
}

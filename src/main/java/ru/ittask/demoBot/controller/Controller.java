package ru.ittask.demoBot.controller;


import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ittask.demoBot.appconfig.MailConfiguration;
import ru.ittask.demoBot.appconfig.TelegramBotConfig;
import ru.ittask.demoBot.cache.DataCache;
import ru.ittask.demoBot.cache.UserDataCache;
import ru.ittask.demoBot.model.UserAttachments;
import ru.ittask.demoBot.service.Service;

import javax.mail.internet.MimeMessage;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
@Getter
public class Controller extends TelegramLongPollingBot {
    //@Value("${spring.mail.sendto}")
    private final String mailSendTo;
    //@Value("${spring.mail.sendfrom}")
    private final String mailSendFrom;
    //@Value("${spring.mail.subject}")
    private final String mailSubject;
    //@Value("${telegram.bot.name}")
    private final String botUserName;
    //@Value("${telegram.bot.token}")
    private final String botToken;
    private TelegramBotConfig telegramBotConfig;
    private DataCache dataCache;


    public Controller() {
        telegramBotConfig = new TelegramBotConfig();
        dataCache = new UserDataCache();

        mailSendTo = telegramBotConfig.getSendTo();
        mailSendFrom = telegramBotConfig.getSendFrom();
        mailSubject = telegramBotConfig.getSubject();
        botUserName = telegramBotConfig.getName();
        botToken = telegramBotConfig.getToken();
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        Service service = new Service();

        message = service.handleUpdate(update, message, this, dataCache);//, message, dataCache, this);

        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Exception while sending message {} to user: {}", message, e.getMessage());
            }
        }
    }

    @SneakyThrows
    public String getFilePath(Update update) throws TelegramApiException {
        // Check that the update contains a message and the message has a photo
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            // When receiving a photo, you usually get different sizes of it
            List<PhotoSize> photos = update.getMessage().getPhoto();
            PhotoSize photo = photos.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);

            assert photo != null;
            String fp = photo.getFilePath();
            if (fp != null) {
                return fp;
            } else {
                String fff = photo.getFileId();
                GetFile getFileMethod = new GetFile();
                getFileMethod.setFileId(photo.getFileId());
                try {
                    // We execute the method using AbsSender::execute method.
                    File file = execute(getFileMethod);
                    // We now have the file_path
                    return file.getFilePath();
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        // Return null if not found
        return null;
    }

    @SneakyThrows
    public void sendEmail(String message, UserAttachments userAttachments) {
        List<String> attPath;
        int count = 1;
        String fileName = "File";
        String filePath = "";
        MailConfiguration mailConf = new MailConfiguration();
        JavaMailSender emailSender = mailConf.getJavaMailSender();
        final MimeMessage mimeMessage = emailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(mailSubject);
        helper.setFrom(mailSendFrom);
        helper.setTo(mailSendTo);
        helper.setText(message, false);

        if (userAttachments.getAttachmentsPath() != null) {
            attPath = userAttachments.getAttachmentsPath();
            for (String path : attPath) {
                fileName = "Attachment_";
                fileName += count + ".jpg";
                filePath = path;
                helper.addAttachment(fileName, downloadFile(filePath));
                count++;
            }
            userAttachments.removeAllPaths();
        }
        emailSender.send(mimeMessage);
    }

    /*private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }*/

    @SneakyThrows
    public void sendPhoto(long chatId, String imageCaption, String imagePath) {
        /*File image = ResourceUtils.getFile("classpath:" + imagePath);
        SendPhoto sendPhoto = new SendPhoto().setPhoto(image);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(imageCaption);
        execute(sendPhoto);*/
    }

    @SneakyThrows
    public void sendDocument(long chatId, String caption, File sendFile) {
        /*SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setCaption(caption);
        sendDocument.setDocument(sendFile);
        execute(sendDocument);*/
    }
}
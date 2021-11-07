package ru.ittask.IntechBot.controller;


import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ittask.IntechBot.cache.DataCache;
import ru.ittask.IntechBot.cache.UserDataCache;
import ru.ittask.IntechBot.model.UserAttachments;
import ru.ittask.IntechBot.repository.UserProfileDataRepository;
import ru.ittask.IntechBot.service.ContextProcessor;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
@Configuration
@Getter
public class Controller extends TelegramLongPollingBot {
    @Autowired
    UserProfileDataRepository repository;
    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private String mailPort;
    @Value("${spring.mail.username}")
    private String mailUserName;
    @Value("${spring.mail.password}")
    private String mailPassword;
    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String mailProtocol;
    @Value("${spring.mail.properties.mail.smtp.ssl.trust}")
    private String mailSslTrust;
    @Value("${spring.mail.properties.mail.debug}")
    private String mailDebug;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String mailSmtpAuth;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String mailTtlsEnable;
    @Value("${spring.mail.sendto}")
    private String mailSendTo;
    @Value("${spring.mail.sendfrom}")
    private String mailSendFrom;
    @Value("${spring.mail.subject}")
    private String mailSubject;
    @Value("${telegram.bot.name}")
    private String botUserName;
    @Value("${telegram.bot.token}")
    private String botToken;
    private DataCache dataCache;
    private SendMessage message;

    public Controller() {
        dataCache = new UserDataCache();
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
        //ServiceClass service = new ServiceClass(update, this, dataCache);
        ContextProcessor contextProcessor = new ContextProcessor();

        message = contextProcessor.process(dataCache, update, this);//service.handleUpdate();

        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                //log.error("Exception while sending message {} to user: {}", message, e.getMessage());
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

    //@SneakyThrows
    public void sendEmail(long chatId, String message, UserAttachments userAttachments) throws MessagingException, TelegramApiException {
        List<String> attPath;
        int count = 1;
        String fileName = "File";
        String filePath = "";
        JavaMailSender emailSender = getJavaMailSender();
        final MimeMessage mimeMessage = emailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(mailSubject);
        helper.setFrom(mailSendFrom);
        helper.setTo(dataCache.getUserProfileData(chatId, repository).getEMail());//mailSendTo);//dataCache.getUserProfileData(chatId, repository).getEMail());
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

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(Integer.parseInt(mailPort));
        mailSender.setUsername(mailUserName);
        mailSender.setPassword(mailPassword);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.trust", mailSslTrust);
        props.put("mail.transport.protocol", mailProtocol);
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailTtlsEnable);
        props.put("mail.debug", mailDebug);

        return mailSender;
    }

    /*public void setUserProfileToRepository(long chatId, UserProfileData userProfileData){
        UserDataEntity entity;
        entity = new UserDataEntity();
        entity.setId(chatId);
        entity.setUserProfileData(userProfileData);
        try {
            repository.deleteById(chatId);
            repository.saveAndFlush(entity);
        } catch (Exception e){
            log.error("Exception while sending message {} to user: {}", message, e.getMessage());
        }
    }*/

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
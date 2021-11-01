package ru.ittask.demoBot.appconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfiguration {

    /*@Value("$spring.mail.host}")
    private String mailHost;
    @Value("$spring.mail.port}")
    private int mailPort;
    @Value("$spring.mail.username}")
    private String mailUserName;
    @Value("$spring.mail.password}")
    private String mailPassword;
    @Value("$spring.mail.properties.mail.transport.protocol}")
    private String mailProtocol;
    @Value("$spring.mail.sendto}")
    private String mailSendTo;
    @Value("$spring.mail.sendfrom}")
    private String mailSendFrom;
    @Value("$spring.mail.properties.mail.smtp.ssl.trust}")
    private String mailSslTrust;
    @Value("$spring.mail.properties.mail.debug}")
    private Boolean mailDebug;
    @Value("$spring.mail.properties.mail.smtp.auth}")
    private Boolean mailSmtpAuth;
    @Value("$spring.mail.properties.mail.smtp.starttls.enable}")
    private Boolean mailTtlsEnable;

    @Bean*/
    public JavaMailSender getJavaMailSender() {
        /*JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);//("mx0.it-task.ru"); //
        mailSender.setPort(mailPort);

        mailSender.setUsername(mailUserName); //("supportbot@it-task.ru");//
        mailSender.setPassword(mailPassword);//

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.trust",  mailSslTrust);
        props.put("mail.transport.protocol", mailProtocol);
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailTtlsEnable);
        props.put("mail.debug", mailDebug);

        return mailSender;*/

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");//("mx0.it-task.ru"); //
        mailSender.setPort(587);

        mailSender.setUsername("aagrigoryev1979@gmail.com"); //("supportbot@it-task.ru");//
        mailSender.setPassword(" ");//

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }
}

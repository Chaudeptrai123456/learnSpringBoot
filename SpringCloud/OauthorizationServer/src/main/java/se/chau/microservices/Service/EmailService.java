package se.chau.microservices.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import se.chau.microservices.api.core.User.User;
import se.chau.microservices.api.core.order.OtpTemplate;

import java.util.Properties;

@Service
public class EmailService  {
    private MailSender mailSender = this.mailSender();
    private SimpleMailMessage templateMessage;

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private  String usernameEmail;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${auth_url}")
    private String auth_url;
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(this.usernameEmail);
        mailSender.setPassword(password);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
    public  void sendOpt(User request,String opt) {
        MimeMessage message = this.mailSender().createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setTo(request.getEmail());
            //test
            helper.setTo("nguyentienanh2001.dev@gmail.com");
            helper.setSubject("Confirm Order");
            helper.setText(new OtpTemplate(request,opt,auth_url).getContent(),true);
            mailSender().send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}

package vn.tpsc.it4u.service;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailServiceImpl implements EmailService{
    // @Autowired
    // private JavaMailSender mailSender;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.yandex.com");
        mailSender.setPort(587);
        mailSender.setUsername("lap.dang@tpsc.vn");
        mailSender.setPassword("Nccvbcl0167@");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        mailSender.send(email);
    }
}
package Rift.Radio.Service;

import Rift.Radio.Repository.EmailSenderService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService implements EmailSenderService {
    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Override
    public void sendEmail(String to, String subject, String message) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        this.mailSender.send(simpleMailMessage);
    }
}

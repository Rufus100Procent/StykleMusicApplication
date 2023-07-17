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
    public void sendEmail(String name, String mail, String description) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("@gmail.com");
        simpleMailMessage.setTo(name);
        simpleMailMessage.setSubject(mail);
        simpleMailMessage.setText(description);

        this.mailSender.send(simpleMailMessage);
    }
}

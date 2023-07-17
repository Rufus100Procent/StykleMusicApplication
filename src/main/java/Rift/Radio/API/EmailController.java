package Rift.Radio.API;

import Rift.Radio.Model.Email;
import Rift.Radio.Service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {
    private final EmailService emailSenderService;

    public EmailController(EmailService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }
    @PostMapping("/send-email")
    public ResponseEntity sendEmail(@RequestBody Email emailMessage) {
        this.emailSenderService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage());
        return ResponseEntity.ok("Success");
    }

}

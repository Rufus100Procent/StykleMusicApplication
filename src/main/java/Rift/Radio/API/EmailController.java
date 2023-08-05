//package Rift.Radio.API;
//
//import Rift.Radio.Model.Email;
//import Rift.Radio.Service.EmailService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//public class EmailController {
//    private final EmailService emailSenderService;
//
//    public EmailController(EmailService emailSenderService) {
//        this.emailSenderService = emailSenderService;
//    }
//    @GetMapping("/send-feedback")
//    public String Testing() {
//        return "feedback";
//    }
//    @PostMapping("/send-feedback")
//    public ResponseEntity sendFeedback(@RequestBody Email email) {
//        String name = "@gmail.com";
//        String subject = "Rift.Radio Feedback: From: " + email.getName();
//        String description = email.getDescription() + "\n\nFrom: " +  email.getEmail();
//
//        emailSenderService.sendEmail(name, subject, description);
//        return ResponseEntity.ok("Feedback submitted successfully");
//    }
//}
//

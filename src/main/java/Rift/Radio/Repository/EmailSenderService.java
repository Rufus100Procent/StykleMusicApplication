package Rift.Radio.Repository;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String message);
}

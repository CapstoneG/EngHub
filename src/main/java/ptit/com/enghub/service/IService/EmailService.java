package ptit.com.enghub.service.IService;

import org.springframework.http.ResponseEntity;
import org.thymeleaf.context.Context;
import ptit.com.enghub.dto.request.EmailDetails;

import java.util.Optional;

public interface EmailService {
    public ResponseEntity<?> sendSimpleMail(EmailDetails details);
    public String sendMailWithAttachment(EmailDetails details);
    public void sendEmailWithHtmlTemplate(String to, String subject, String templateName, Context context);
}

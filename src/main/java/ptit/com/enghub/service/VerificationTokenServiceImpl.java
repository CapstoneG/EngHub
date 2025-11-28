package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.entity.VerificationToken;
import ptit.com.enghub.repository.VerificationTokenRepository;
import ptit.com.enghub.service.IService.EmailService;
import ptit.com.enghub.service.IService.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    @Override
    @Async
    public void createAndSendVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .type("EMAIL_VERIFY")
                .expiryDate(LocalDateTime.now().plusHours(1))
                .user(user)
                .build();

        verificationTokenRepository.save(verificationToken);

        String verificationUrl = "http://localhost:8080/auth/verify?token=" + token;
        sendVerificationEmail(user.getEmail(), verificationUrl);
    }

    @Override
    public void sendVerificationEmail(String email, String verificationUrl) {
        Context context = new Context();
        context.setVariable("verificationUrl", verificationUrl);
        emailService.sendEmailWithHtmlTemplate(
                email,
                "Verify Your Email Address",
                "email-verification",
                context
        );
    }

    @Override
    public boolean verifyToken(String token) {
        return verificationTokenRepository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .map(t -> {
                    User user = t.getUser();
                    user.setVerified(true);
                    verificationTokenRepository.delete(t); // Xóa token sau khi xác thực
                    return true;
                })
                .orElse(false);
    }
}

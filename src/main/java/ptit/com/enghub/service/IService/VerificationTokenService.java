package ptit.com.enghub.service.IService;

import ptit.com.enghub.entity.User;

public interface VerificationTokenService {
    public void createAndSendVerificationToken(User user);
    public void sendVerificationEmail(String email, String verificationUrl);
    public boolean verifyToken(String token);
}

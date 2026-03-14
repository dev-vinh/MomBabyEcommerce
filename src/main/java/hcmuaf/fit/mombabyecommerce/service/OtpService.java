package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.OtpDao;
import org.jdbi.v3.core.Jdbi;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OtpService {
    private final OtpDao otpDao;
    private final EmailService emailService;

    public OtpService(Jdbi jdbi) {
        this.otpDao = jdbi.onDemand(OtpDao.class);
        this.emailService = new EmailService();
    }

    public boolean generateAndSendOTP(String email) {
        try {
            String otp = emailService.generateOTP();
            LocalDateTime now = LocalDateTime.now();
            Timestamp expiresAt = Timestamp.valueOf(now.plusMinutes(5));
            otpDao.saveOrUpdateOTP(email, otp, expiresAt);
            emailService.sendEmailWithOTP(email, otp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

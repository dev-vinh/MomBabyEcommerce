package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.UserOtp;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.Timestamp;
@RegisterConstructorMapper(UserOtp.class)
public interface OtpDao {
    @SqlUpdate("""
        INSERT INTO user_otp (email, otpCode, expiresAt)
        VALUES (:email, :otpCode, :expiresAt)
        ON DUPLICATE KEY UPDATE
            otpCode = VALUES(otpCode),
            createdAt = CURRENT_TIMESTAMP,
            expiresAt = VALUES(expiresAt)
    """)
    void saveOrUpdateOTP(@Bind("email") String email,
                         @Bind("otpCode") String otpCode,
                         @Bind("expiresAt") Timestamp expiresAt);
}

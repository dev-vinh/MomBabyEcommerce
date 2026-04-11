package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.UserOTP;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.Timestamp;
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

    @SqlQuery("""
        SELECT 
            email,
            otpCode,
            createdAt,
            expiresAt
        FROM user_otp
        WHERE email = :email
    """)
    UserOTP getValidOTPByEmail(@Bind("email") String email);

    @SqlUpdate("DELETE FROM user_otp WHERE email = :email")
    void deleteOTP(@Bind("email") String email);
}

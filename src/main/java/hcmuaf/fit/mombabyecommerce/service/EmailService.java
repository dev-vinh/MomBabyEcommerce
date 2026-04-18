package hcmuaf.fit.mombabyecommerce.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailService {
    String host = "smtp.gmail.com";
    String fromEmail = "tuanghiencuu@gmail.com";
    String password = "trgm jemx lzxr dmyw";

    // Server URL - có thể set qua System property khi deploy
    // VD: -Dserver.url=https://domain.com
    private static String serverUrl = System.getProperty("server.url", "http://localhost:8080");

    // Hàm gửi email chứa mã OTP
    // Hàm gửi email chứa mã OTP
    public void sendEmailWithOTP(String toEmail, String otp) throws MessagingException {
        // Cấu hình thông tin kết nối với SMTP server

        // Cấu hình các thuộc tính SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Tạo một session email
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        // Tạo đối tượng MimeMessage
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail)); // Địa chỉ người gửi
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail)); // Địa chỉ người nhận
        message.setSubject("Mã OTP xác thực"); // Tiêu đề email

        // Nội dung email
        String emailContent = "<h3>Toi da gui ma OTP cho ban. Ma OTP cua ban la: " + otp + "</h3>"
                + "<p>Vui lòng không chia sẻ mã OTP này voi bat ki ai.</p>";
        message.setContent(emailContent, "text/html");

        // Gửi email
        Transport.send(message);
        System.out.println("Email đã được gửi thành công đến " + toEmail);
    }

    public void sendConfirmationEmail(String toEmail, String sessionId) throws MessagingException {
        sendConfirmationEmail(toEmail, sessionId, "");
    }

    public void sendConfirmationEmail(String toEmail, String sessionId, String contextPath) throws MessagingException {

        // Cấu hình các thuộc tính SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Tạo một session email
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        // Tạo đối tượng MimeMessage
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail)); // Địa chỉ người gửi
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail)); // Địa chỉ người nhận
        message.setSubject("Xác nhận đăng ký tài khoản");

        // Nội dung email chứa liên kết xác nhận
        String confirmLink = serverUrl + contextPath + "/confirm?sessionId=" + sessionId;
        String emailContent = "<h3>Xin Chào!,</h3>"
                + "<p>Vui lòng nhập vào liên kết duoi dây de xac nhan tai khoan cua ban:</p>"
                + "<a href=\"" + confirmLink + "\">Xác nhận</a>";

        message.setContent(emailContent, "text/html; charset=UTF-8");

        // Gửi email
        Transport.send(message);
        System.out.println("Email xác nhận đã được gửi đến " + toEmail);
    }

    public String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000; // Tạo OTP 6 chữ số
        return String.valueOf(otp);
    }
}

package hcmuaf.fit.mombabyecommerce.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailService {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String USERNAME = "tuanghiencuu@gmail.com";
    private static final String PASSWORD = "trgm jemx lzxr dmyw";
    private static final Properties properties = new Properties();
    private static Session session;

    private static String serverUrl = System.getProperty("server.url", "http://localhost:8080");
    public void sendEmailWithOTP(String toEmail, String otp) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME)); // Địa chỉ người gửi
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail)); // Địa chỉ người nhận
        message.setSubject("Mã OTP xác thực"); // Tiêu đề email

        String emailContent = "<h3>Toi da gui ma OTP cho ban. Ma OTP cua ban la: " + otp + "</h3>"
                + "<p>Vui lòng không chia sẻ mã OTP này voi bat ki ai.</p>";
        message.setContent(emailContent, "text/html");

        Transport.send(message);
        System.out.println("Email đã được gửi thành công đến " + toEmail);
    }

    public void sendConfirmationEmail(String toEmail, String sessionId) throws MessagingException {
        sendConfirmationEmail(toEmail, sessionId, "");
    }

    public void sendConfirmationEmail(String toEmail, String sessionId, String contextPath) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME)); // Địa chỉ người gửi
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail)); // Địa chỉ người nhận
        message.setSubject("Xác nhận đăng ký tài khoản");

        String confirmLink = serverUrl + contextPath + "/confirm?sessionId=" + sessionId;
        String emailContent = "<h3>Xin Chào!,</h3>"
                + "<p>Vui lòng nhập vào liên kết duoi dây de xac nhan tai khoan cua ban:</p>"
                + "<a href=\"" + confirmLink + "\">Xác nhận</a>";

        message.setContent(emailContent, "text/html; charset=UTF-8");

        Transport.send(message);
        System.out.println("Email xác nhận đã được gửi đến " + toEmail);
    }

    public String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000; // Tạo OTP 6 chữ số
        return String.valueOf(otp);
    }

    public static void sendRegistrationEmail(String toEmail, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Xác nhận đăng ký tài khoản");

            String content = "Xin chào,\n\n" +
                    "Cảm ơn bạn đã đăng ký tài khoản với chúng tôi.\n\n" +
                    "Thông tin đăng nhập của bạn:\n" +
                    "Email: " + toEmail + "\n" +
                    "Mật khẩu: " + password + "\n\n" +
                    "Vui lòng đăng nhập và đổi mật khẩu ngay sau khi đăng nhập lần đầu.\n\n" +
                    "Trân trọng,\n" +
                    "Đội ngũ hỗ trợ";

            message.setText(content);

            Transport.send(message);
            System.out.println("Email xác nhận đã được gửi đến " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
            throw new RuntimeException("Không thể gửi email xác nhận", e);
        }
    }
}

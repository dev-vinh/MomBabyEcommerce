package hcmuaf.fit.mombabyecommerce.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailService {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String USERNAME = "Vinhphanngoc61@gmail.com";
    private static final String PASSWORD = "sfqi fzci hkrv iqsp";
    private static final Properties properties = new Properties();
    private static Session session;

    public EmailService() {
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties");
            if (input != null) {
                properties.load(input);
            }
            Properties mailProps = new Properties();
            mailProps.put("mail.smtp.auth", "true");
            mailProps.put("mail.smtp.starttls.enable", "true");
            mailProps.put("mail.smtp.host", properties.getProperty("mail.smtp.host", "smtp.gmail.com"));
            mailProps.put("mail.smtp.port", properties.getProperty("mail.smtp.port", "587"));

            session = Session.getInstance(mailProps, new jakarta.mail.Authenticator() {
                @Override
                protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new jakarta.mail.PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendEmail(String to, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email: " + e.getMessage());
        }
    }
    public String generateOTP() {
        int otp = (int) (Math.random() * 90000) + 10000;
        return String.valueOf(otp);
    }
    public void sendEmailWithOTP(String toEmail, String otp) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Mã OTP xác thực");

            String emailContent = "<h3>Toi da gui ma OTP cho ban. Ma OTP cua ban la: " + otp + "</h3>"
                    + "<p>Vui lòng không chia sẻ mã OTP này voi bat ki ai.</p>";
            message.setContent(emailContent, "text/html");
            Transport.send(message);
            System.out.println("Email đã được gửi thành công đến " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Có lỗi xảy ra khi gửi email");
        }
    }
}

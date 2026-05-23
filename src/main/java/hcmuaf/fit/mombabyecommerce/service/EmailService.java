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
        private static final String PASSWORD = "lbns hhaw qgdt fsbg";
        private static final Properties properties = new Properties();
        private static Session session;

        private static String serverUrl = System.getProperty("server.url", "http://localhost:8080");

        private void sendBaseEmail(String toEmail, String subject, String content) throws MessagingException {
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
            message.setFrom(new InternetAddress(USERNAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject, "UTF-8");
            message.setContent(content, "text/html; charset=UTF-8");

            Transport.send(message);
        }

        public void sendEmailWithOTP(String toEmail, String otp) throws MessagingException {
            String subject = "Mã OTP xác thực";
            String content = "<h3>Tôi đã gửi mã OTP cho bạn. Mã OTP của bạn là: <span style='color:red;'>" + otp + "</span></h3>"
                    + "<p>Vui lòng không chia sẻ mã OTP này với bất kỳ ai.</p>";
            sendBaseEmail(toEmail, subject, content);
            System.out.println("Email OTP đã được gửi thành công đến " + toEmail);
        }

        public void sendConfirmationEmail(String toEmail, String sessionId, String contextPath) throws MessagingException {
            String subject = "Xác nhận đăng ký tài khoản";
            String confirmLink = serverUrl + contextPath + "/confirm?token=" + sessionId;
            String content = "<h3>Xin Chào!,</h3>"
                    + "<p>Vui lòng nhấn vào liên kết dưới đây để xác nhận tài khoản của bạn:</p>"
                    + "<a href=\"" + confirmLink + "\" style='padding:10px 20px; color:white; background:#ff66a1; text-decoration:none; border-radius:5px;'>Xác nhận ngay</a>";

            sendBaseEmail(toEmail, subject, content);
            System.out.println("Email xác nhận đã được gửi đến " + toEmail);
        }

        public void sendLoginNotification(String toEmail, String fullName) {
            try {
                String subject = "Thông báo đăng nhập thành công - Shop Mẹ & Bé";
                String content = "<div style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>"
                        + "<h2 style='color: #ff66a1;'>Chào " + fullName + ",</h2>"
                        + "<p>Bạn vừa đăng nhập thành công vào website <b>Mom & Baby Ecommerce</b> thông qua tài khoản Google.</p>"
                        + "<div style='background: #f9f9f9; padding: 15px; border-left: 4px solid #ff66a1;'>"
                        + "<p style='margin: 0;'><b>Thời gian:</b> " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "</p>"
                        + "<p style='margin: 0;'><b>Phương thức:</b> Google OAuth2 (Đăng nhập nhanh)</p>"
                        + "</div>"
                        + "<p>Nếu <b>không phải là bạn</b> thực hiện hành động này, vui lòng liên hệ với chúng tôi ngay lập tức để bảo vệ tài khoản.</p>"
                        + "<br>"
                        + "<p>Trân trọng,<br><b>Đội ngũ hỗ trợ Shop Mẹ & Bé</b></p>"
                        + "</div>";

                sendBaseEmail(toEmail, subject, content);
                System.out.println("Email thông báo đăng nhập đã gửi đến: " + toEmail);
            } catch (MessagingException e) {
                System.err.println("Lỗi khi gửi email thông báo đăng nhập: " + e.getMessage());
                e.printStackTrace();
            }
        }

        public String generateOTP() {
            int otp = (int) (Math.random() * 900000) + 100000;
            return String.valueOf(otp);
        }
    }

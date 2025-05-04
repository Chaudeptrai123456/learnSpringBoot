package se.chau.microservices.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import se.chau.microservices.api.core.User.User;
import se.chau.microservices.api.core.order.OtpTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class EmailService  {

    private final JavaMailSender mailSender; // Inject MailSender

    private final String authUrl; // Sử dụng hằng số, và tiêm nó vào.

    public EmailService(JavaMailSender mailSender, @Value("${auth_url}") String authUrl) { // Constructor Injection
        this.mailSender = mailSender;
        this.authUrl = authUrl;
    }

    // Lớp OtpTemplate nên ở file riêng, nhưng để ở đây cho tiện theo dõi
    private static class OtpTemplate {
        private final String content;

        public OtpTemplate(User request, String otp, String authUrl) {
            String encodedEmail = URLEncoder.encode(request.getEmail(), StandardCharsets.UTF_8);
            String verificationLink = authUrl + "/oauth2/user/verify?email=" + encodedEmail + "&otp=" + otp;

            this.content = "<div style=\"font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; min-width: 200px; overflow: auto; line-height: 1.6;\">\n" +
                    "    <div style=\"background-color: #f7f7f7; margin: 40px auto; width: 95%; max-width: 600px; padding: 30px; border-radius: 12px; box-shadow: 0 8px 16px rgba(0,0,0,0.08); border: 1px solid #e0e0e0;\">\n" +
                    "        <div style=\"text-align: center; margin-bottom: 30px;\">\n" +
                    "            <h2 style=\"color: #333; margin: 0 0 15px; font-size: 28px; font-weight: 700;\">Xác nhận đăng ký tài khoản</h2>\n" +
                    "            <p style=\"color: #555; font-size: 16px;\">Chào mừng <strong style=\"font-weight: 600;\">" + request.getUsername() + "</strong>,</p>\n" +
                    "            <p style=\"color: #555; font-size: 16px; margin-bottom: 20px;\">Cảm ơn bạn đã đăng ký tài khoản tại hệ thống của chúng tôi. Để hoàn tất quá trình đăng ký, vui lòng sử dụng mã OTP hoặc nhấp vào nút xác nhận bên dưới:</p>\n" +
                    "        </div>\n" +
                    "        <div style=\"background-color: #007bff; color: #fff; padding: 18px; text-align: center; border-radius: 8px; font-size: 22px; letter-spacing: 8px; margin-bottom: 30px; font-weight: 600;\">" + otp + "</div>\n" +
                    "        <div style=\"text-align: center; margin-bottom: 30px;\">\n" +
                    "            <a href=\"" + verificationLink + "\" style=\"background-color: #28a745; color: #fff; padding: 14px 24px; border-radius: 8px; text-decoration: none; font-size: 18px; font-weight: 500; display: inline-block; transition: background-color 0.3s ease;\">\n" +
                    "                <span style=\"display: inline-block; vertical-align: middle; margin-right: 8px;\">✔️</span> Xác nhận tài khoản\n" +
                    "            </a>\n" +
                    "            <p style=\"color: #777; font-size: 14px; margin-top: 10px;\">Hoặc sao chép và dán liên kết sau vào trình duyệt của bạn:</p>\n" +
                    "            <p style=\"color: #777; font-size: 12px; word-break: break-all;\"><a href=\"" + verificationLink +"hoặc " + "http://14.225.206.109:9999/oauth2/authorize?response_type=code&client_id=chau&redirect_uri=https://14.225.206.109:8443/oauth2/code&scope=openid%20product:read%20product:write" +"\" style=\"color: #007bff; text-decoration: underline;\">" + verificationLink + "</a></p>\n" +
                    "        </div>\n" +
                    "        <p style=\"color: #555; font-size: 16px; margin-bottom: 20px;\">Nếu bạn không thực hiện việc đăng ký này, xin vui lòng bỏ qua email này. Tài khoản của bạn sẽ không được kích hoạt.</p>\n" +
                    "        <hr style=\"border: 1px solid #e0e0e0; margin-bottom: 20px;\">\n" +
                    "        <div style=\"text-align: center; color: #777; font-size: 14px;\">\n" +
                    "            <p style=\"margin-bottom: 5px;\">Trân trọng,</p>\n" +
                    "            <p style=\"margin-bottom: 0;\">Đội ngũ hỗ trợ khách hàng</p>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</div>";
        }

        public String getContent() {
            return content;
        }
    }

    public void sendOpt(User request, String otp) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // Thêm encoding UTF-8
            helper.setTo(request.getEmail()); // Sử dụng email từ request
            helper.setSubject("Xác nhận tài khoản "); // Chủ đề rõ ràng
            helper.setText(new OtpTemplate(request, otp, authUrl).getContent(), true); // Truyền authUrl
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e); // Thông báo lỗi cụ thể hơn
        }
    }
}

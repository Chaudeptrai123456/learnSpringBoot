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

        public OtpTemplate(User request, String otp, String authUrl) { // Thêm authUrl
            // Encode các tham số URL để tránh lỗi
            String encodedEmail = URLEncoder.encode(request.getEmail(), StandardCharsets.UTF_8);
            String verificationLink = authUrl + "/oauth2/user/verify?email=" + encodedEmail + "&otp=" + otp;

            this.content = "<div style=\"font-family:Arial,sans-serif;min-width:200px;overflow:auto;line-height:1.5\">\n" +
                    "    <div style=\"background-color:#f2f2f2;margin:50px auto;width:90%;max-width:600px;padding:20px 30px;border-radius:8px;box-shadow:0 4px 8px rgba(0,0,0,.05)\">\n" +
                    "        <h2 style=\"color:#111;margin:0 0 20px;font-size:24px;font-weight:bold\">Xác nhận đơn hàng của bạn</h2>\n" +
                    "        <p style=\"margin:0 0 20px;font-size:16px\">Xin chào " + request.getUsername() + ",</p>\n" +
                    "        <p style=\"margin:0 0 20px;font-size:16px\">Cảm ơn bạn đã đặt hàng. Vui lòng xác nhận đơn hàng của bạn bằng cách sử dụng mã OTP sau:</p>\n" +
                    "        <div style=\"background:#00466a;color:#fff;padding:16px;text-align:center;border-radius:4px;font-size:20px;letter-spacing:6px;margin-bottom:20px\">" + otp + "</div>\n" +
                    "        <p style=\"margin:0 0 20px;font-size:16px\">Hoặc bạn có thể nhấp vào liên kết sau để xác nhận:</p>\n" +
                    "        <a href=\"" + verificationLink + "\" style=\"background:#20e070;color:#fff;padding:12px 20px;border-radius:4px;text-decoration:none;font-size:16px;margin-bottom:20px;display:inline-block\">Xác nhận đơn hàng</a>\n" + //Thêm link xác nhận
                    "        <p style=\"margin:0 0 20px;font-size:16px\">Nếu bạn không thực hiện đơn hàng này, vui lòng bỏ qua email này.</p>\n" +
                    "        <p style=\"margin:0 0 10px;font-size:16px\">Trân trọng,</p>\n" +
                    "        <p style=\"margin:0 0 0;font-size:16px\">Đội ngũ hỗ trợ khách hàng</p>\n" +
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
            helper.setSubject("Xác nhận đơn hàng"); // Chủ đề rõ ràng
            helper.setText(new OtpTemplate(request, otp, authUrl).getContent(), true); // Truyền authUrl
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e); // Thông báo lỗi cụ thể hơn
        }
    }
}

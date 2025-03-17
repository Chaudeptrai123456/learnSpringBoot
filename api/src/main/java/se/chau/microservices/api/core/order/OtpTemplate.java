package se.chau.microservices.api.core.order;

import se.chau.microservices.api.core.User.User;

public class OtpTemplate {
    private User user;
    private String opt;

    private String url;

    public OtpTemplate(User user, String opt, String url) {
        this.user = user;
        this.opt = opt;
        this.url = url;
    }

    public String getContent() {
        String verifyUrl = url+"/oauth2/user/verify?email=" + this.user.getEmail() + "&otp=" + opt.toString();
        return "<!DOCTYPE html>\n" +
                "<html lang=\"vi\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Xác Thực OTP</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h2>Xác thực tài khoản</h2>\n" +
                "    <p>Chào bạn,</p>\n" +
                "    <p>Nhấn vào nút bên dưới để xác thực tài khoản:</p>\n" +
                "    <a href=\"" + verifyUrl + "\" style=\"display:inline-block;padding:10px 20px;background:#28a745;color:white;text-decoration:none;border-radius:5px;\">Xác nhận OTP</a>\n" +
                "    <p>Nếu bạn không yêu cầu xác thực, vui lòng bỏ qua email này.</p>\n" +
                "</body>\n" +
                "</html>";
    }


}

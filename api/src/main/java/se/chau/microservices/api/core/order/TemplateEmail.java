package se.chau.microservices.api.core.order;

import java.time.LocalDate;

public class TemplateEmail {

    private String content;

    public String getContent() {
        return content;
    }

    public TemplateEmail(Email email) {
        this.content = setContent(email);
    }

    public String setContent(Email email) {
        String orderId = String.valueOf(email.getOrderId());
        String total = String.valueOf(email.getTotalCost());
        System.out.println("test " + orderId);
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Order Confirmation</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f9f9f9;\n" +
                "            color: #333;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #4CAF50;\n" +
                "            font-size: 24px;\n" +
                "        }\n" +
                "        .order-id {\n" +
                "            font-size: 18px;\n" +
                "            font-weight: bold;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        .order-details {\n" +
                "            background-color: #f1f1f1;\n" +
                "            padding: 15px;\n" +
                "            border-radius: 6px;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "        .order-details p {\n" +
                "            margin: 5px 0;\n" +
                "        }\n" +
                "        .thank-you {\n" +
                "            font-size: 16px;\n" +
                "            color: #555;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            background-color: #4CAF50;\n" +
                "            color: #fff;\n" +
                "            padding: 10px 20px;\n" +
                "            border-radius: 5px;\n" +
                "            text-decoration: none;\n" +
                "            font-weight: bold;\n" +
                "            margin-top: 30px;\n" +
                "        }\n" +
                "        .button:hover {\n" +
                "            background-color: #45a049;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Order Confirmation</h1>\n" +
                "        <p>Hello "+ email.getUsername() +",</p>\n" +
                "        <p>Thank you for your order! We're excited to let you know that we've received it and are preparing it for shipping.</p>\n" +
                "\n" +
                "        <div class=\"order-details\">\n" +
                "            <p class=\"order-id\">Order ID:" +orderId+ "</p>\n" +
                "            <p><strong>Order Date:</strong> "+ LocalDate.now()+"</p>\n" +
                "            <p><strong>Total Amount:</strong> "+total+"</p>\n" +
                "            <p><strong>Shipping Address:</strong> Thu Duc </p>\n" +
                "        </div>\n" +
                "\n" +
                "        <p class=\"thank-you\">Thank you for shopping with us. We will notify you once your order has been shipped.</p>\n" +
                "\n" +
                "        <a href=\"[OrderTrackingLink]\" class=\"button\">Track Your Order</a>\n" +
                "    </div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

    }
}

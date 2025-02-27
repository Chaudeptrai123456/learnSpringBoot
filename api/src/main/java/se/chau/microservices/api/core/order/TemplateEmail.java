package se.chau.microservices.api.core.order;

import org.springframework.beans.factory.annotation.Value;
import se.chau.microservices.api.discount.Discount;

import java.time.LocalDate;

public class TemplateEmail {
    @Value("${url-order}")
    private String linkOrder;

    private String content;

    private Discount discount;


    public String getContent() {
        return content;
    }

    public TemplateEmail(Email email) {
        this.content = setContent(email);
    }


    public String setDiscountEmail(Email email){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Exclusive Discount Just for You!</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        .email-container {\n" +
                "            width: 100%;\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        .header {\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "            border-top-left-radius: 8px;\n" +
                "            border-top-right-radius: 8px;\n" +
                "        }\n" +
                "\n" +
                "        .header h1 {\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "\n" +
                "        .body-content {\n" +
                "            padding: 20px;\n" +
                "            color: #333333;\n" +
                "        }\n" +
                "\n" +
                "        .body-content h2 {\n" +
                "            color: #4CAF50;\n" +
                "        }\n" +
                "\n" +
                "        .product-details {\n" +
                "            background-color: #f9f9f9;\n" +
                "            border-radius: 5px;\n" +
                "            padding: 15px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .product-details p {\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "\n" +
                "        .discount-code {\n" +
                "            background-color: #ff6f61;\n" +
                "            color: white;\n" +
                "            font-weight: bold;\n" +
                "            padding: 10px;\n" +
                "            border-radius: 5px;\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "\n" +
                "        .cta-button {\n" +
                "            display: block;\n" +
                "            width: 100%;\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            padding: 15px;\n" +
                "            text-align: center;\n" +
                "            text-decoration: none;\n" +
                "            font-size: 16px;\n" +
                "            border-radius: 5px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            background-color: #f4f4f4;\n" +
                "            padding: 10px;\n" +
                "            font-size: 12px;\n" +
                "            color: #777;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Exclusive Discount Just for You!</h1>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"body-content\">\n" +
                "            <h2>Dear "+email.getUserId() +",</h2>\n" +
                "            <p>We're excited to offer you an exclusive <strong>"+discount.getValue()+"%</strong> discount on your favorite product!</p>\n" +
                "\n" +
                "            <div class=\"product-details\">\n" +
                "                <p><strong>Product:</strong> "+ discount.getProductId()+"</p>\n" +
                "                <p><strong>Discount:</strong>"+discount.getValue()+"% off</p>\n" +
                "                <p><strong>Your User ID:</strong> "+email.getUsername()+"</p>\n" +
                "                <p><strong>Offer Valid Until:</strong> [Discount Period End Date]</p>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"discount-code\">\n" +
                "                <p>Use Code: <strong>[Discount Code]</strong></p>\n" +
                "                <p>Apply at checkout to get your discount!</p>\n" +
                "            </div>\n" +
                "\n" +
                "            <a href=\"[Link to Product]\" class=\"cta-button\">Shop Now</a>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"footer\">\n" +
                "            <p>If you have any questions, feel free to contact us at [Contact Info].</p>\n" +
                "            <p>&copy; 2025 [Your Company Name]. All rights reserved.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }

    public void setDiscount(Discount discount) {this.discount = discount;}

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
                " <script>\n" +
                "        // Sample Authorization token (You would typically get this from login/session or other methods)\n" +
                "        const token = 'your_authorization_token_here';\n" +
                "        const orderTrackingLink = '"+email.getLink()+"'; // Replace with your actual order tracking endpoint\n" +
                "\n" +
                "        // Track Order Button Click Event\n" +
                "        document.getElementById('trackOrderButton').addEventListener('click', function(event) {\n" +
                "            event.preventDefault(); // Prevent default behavior (if any)\n" +
                "\n" +
                "            // Send a GET request to track the order with the Authorization token in the headers\n" +
                "            fetch(orderTrackingLink, {\n" +
                "                method: 'GET',\n" +
                "                headers: {\n" +
                "                    'Authorization': `"+email.getToken()+"`,  // Adding the Authorization header with the token\n" +
                "                    'Content-Type': 'application/json'   // Optional, depending on the API\n" +
                "                }\n" +
                "            })\n" +
                "            .then(response => response.json()) // Assuming the API returns JSON data\n" +
                "            .then(data => {\n" +
                "                console.log('Order Tracking Info:', data);  // Handle the response here\n" +
                "                alert('Order Tracking Info: ' + JSON.stringify(data));\n" +
                "            })\n" +
                "            .catch(error => {\n" +
                "                console.error('Error:', error);\n" +
                "                alert('Failed to track the order. Please try again.');\n" +
                "            });\n" +
                "        });\n" +
                "    </script>"
                +
                "    </div>\n" +
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
                "        <Button id=\"trackOrderButton\">Track Your Order</Button>\n" +


                "</body>\n" +
                "</html>";

    }
}

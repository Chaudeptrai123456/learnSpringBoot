package se.chau.microservices.api.core.order;

import se.chau.microservices.api.discount.Discount;

public class DiscountEmail {
    private String userName;
    private String email;

    public DiscountEmail(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String setDiscountEmail(Discount discount){
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
                    "            <h2>Dear "+this.userName +",</h2>\n" +
                    "            <p>We're excited to offer you an exclusive <strong>"+discount.getValue()+"%</strong> discount on your favorite product!</p>\n" +
                    "\n" +
                    "            <div class=\"product-details\">\n" +
                    "                <p><strong>Product:</strong> "+ discount.getProductId()+"</p>\n" +
                    "                <p><strong>Discount:</strong>"+discount.getValue()+"% off</p>\n" +
                    "                <p><strong>Offer Valid from:</strong>: " + discount.getStartDate()+" <strong>To</strong> "+discount.getEndDate()+"</p>\n" +
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
}

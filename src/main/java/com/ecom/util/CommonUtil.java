package com.ecom.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ecom.model.ProductOrder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends a password reset email to the specified recipient.
     * @param url The reset link to be included in the email.
     * @param email The recipient's email address.
     * @return Boolean indicating success or failure of the operation.
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public Boolean sendMail(String url, String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom("nitinbone39@gmail.com", "Shopping Cart");
            helper.setTo(email);

            String content = "<p>Hello,</p>" +
                             "<p>You have requested to reset your password.</p>" +
                             "<p>Click the link below to change your password:</p>" +
                             "<p><a href=\"" + url + "\">Change my password</a></p>";

            helper.setSubject("Password Reset");
            helper.setText(content, true);

            mailSender.send(message);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace(); // Replace with proper logging in production
            return false;
        }
    }

    /**
     * Generates the base site URL from the HttpServletRequest.
     * @param request The HTTP servlet request.
     * @return The base site URL.
     */
    public String generateUrl(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }
    
	String msg=null;;
    
    public Boolean sendMailForProductOrder(ProductOrder order,String status) throws Exception
	{
		
		msg="<p>Hello [[name]],</p>"
				+ "<p>Thank you order <b>[[orderStatus]]</b>.</p>"
				+ "<p><b>Product Details:</b></p>"
				+ "<p>Name : [[productName]]</p>"
				+ "<p>Category : [[category]]</p>"
				+ "<p>Quantity : [[quantity]]</p>"
				+ "<p>Price : [[price]]</p>"
				+ "<p>Payment Type : [[paymentType]]</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("nitinbone39@gmail.com", "Shooping Cart");
		helper.setTo(order.getOrderAddress().getEmail());

		msg=msg.replace("[[name]]",order.getOrderAddress().getFirstName());
		msg=msg.replace("[[orderStatus]]",status);
		msg=msg.replace("[[productName]]", order.getProduct().getTitle());
		msg=msg.replace("[[category]]", order.getProduct().getCategory());
		msg=msg.replace("[[quantity]]", order.getQuantity().toString());
		msg=msg.replace("[[price]]", order.getPrice().toString());
		msg=msg.replace("[[paymentType]]", order.getPaymentType());
		
		helper.setSubject("Order Status");
		helper.setText(msg, true);
		mailSender.send(message);
		return true;
	}
}

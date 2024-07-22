package com.lamdangfixbug.qmshoe.utils;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final String HTML_TEMPLATE = """
                <div>
                  <p style="text-align: center;">Welcome to QMShoe!</p>
                  <h1 style="margin: 24px 0;text-align:center;">
                    <span style="border-bottom: 1px solid aqua;">Hi ${name},</span>
                  </h1>
                  <h3 style="text-align: center; padding-bottom: 24px">
                    Thanks for registering on our site.<br />
                    Let's start to shopping in our website
                  </h3>
                  <p style="text-align: center;">
                    <a href="https://facebook.com" style="background-color: rgb(25, 179, 202);border: none;padding: 8px 24px 8px 24px;color: white;text-decoration: none;">
                        Shop Now
                    </a>
                  </p>
                  <hr style="margin: 24px 0 12px 0" />
                  <p style="font-size: 14px;text-align: center;">
                    You are receiving this email because you have visited our site or asked us about regular newsletter.
                  </p>
                  <p style="text-align: center;font-size: 14px;">
                    <a style="font-size: 12px;text-decoration: none;" href="#">Unsubscribe</a> | <a style="font-size: 12px;text-decoration: none;" href="#">Update Preferences</a> |
                    <a style="font-size: 12px;text-decoration: none;" href="#">Customer Support</a>
                  </p>
                </div>
            """;
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

//    public void sendEmail(String to, String subject, String text) {
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//        String htmlMsg = "<h3 style='color:red;'>Hello World !</h3>";
//        try {
//            helper.setText(htmlMsg, true);
//            helper.setTo("kaninem307@sablecc.com");
//            helper.setSubject("Welcome to QMShoe!");
//            helper.setFrom("qmshoe@gmail.com");
//            mailSender.send(mimeMessage);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public void sendWelcomeEmail(String to, String name) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = HTML_TEMPLATE.replace("${name}", name);
        try {
            helper.setText(htmlMsg, true);
            helper.setTo(to);
            helper.setSubject("Welcome to QMShoe!");
            helper.setFrom("qmshoe@gmail.com");
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

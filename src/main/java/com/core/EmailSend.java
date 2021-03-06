package com.core;

import org.testng.Reporter;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class EmailSend {

    public static void mailsend() throws IOException {
        Properties pro = TestUtils.getMessagePropery();
        String host = pro.getProperty("mail.host");
        String port = pro.getProperty("mail.port");
        String auth = pro.getProperty("mail.auth");
        final String username = pro.getProperty("mail.username");
        final String password = pro.getProperty("mail.password");
        String to = pro.getProperty("mail.to");
        String cc = pro.getProperty("mail.cc");
        java.util.Date date = new java.util.Date();
        String subject = pro.getProperty("mail.subject") + " " + date;


        String content = pro.getProperty("mail.content");
        boolean status = Boolean.parseBoolean(pro.getProperty("mail.send"));
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.port", port);

        if (status) {
            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                String[] recipientList = to.split(",");
                InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
                int counter = 0;
                for (String recipient : recipientList) {
                    recipientAddress[counter] = new InternetAddress(recipient.trim());
                    counter++;
                }
                message.setRecipients(Message.RecipientType.TO, recipientAddress);
                message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
                message.setSubject(subject);
                message.setText(content);
                Multipart multipart = new MimeMultipart();
                addAttachment(multipart, System.getProperty("user.dir") + "/" + "test-output/emailable-report.html");
                addAttachment(multipart, ZipFileCreate.creteZipFile());
                message.setContent(multipart);
                //Below code for attachment in email

//		         // This mail has 2 part, the BODY and the embedded image
//  	         MimeMultipart multipart = new MimeMultipart("related");
//
//		         // first part (the html)
//		         BodyPart messageBodyPart = new MimeBodyPart();
//		         String htmlText = "<H1>Hello</H1><img src=\"cid:image\">";
//		         messageBodyPart.setContent(htmlText, "text/html");
//		         // add it
//		         multipart.addBodyPart(messageBodyPart);
//		         
//		      // second part (the image)
//		         messageBodyPart = new MimeBodyPart();
//		         DataSource fds = new FileDataSource("C:\\Users\\Ashiwani\\Downloads\\download.jpg");
//		         messageBodyPart.setDataHandler(new DataHandler(fds));
//		         messageBodyPart.setHeader("Content-ID", "<image>");
//
//		         // add image to the multipart
//		         multipart.addBodyPart(messageBodyPart);
//                // put everything together
//		         message.setContent(multipart);
//		         


                Transport.send(message);

                System.out.println("Email sent successfully");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        } else {
            Reporter.log("Email sent is Off", true);
        }
    }

    private static void addAttachment(Multipart multipart, String filename) throws MessagingException {
        DataSource source = new FileDataSource(filename);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);
    }
}


package services;

import classes.CourierDetails;
import interfaces.EmailCredentials;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class Mailer extends CourierDetails implements EmailCredentials {
    public void sendInvoicwMail(String receiverEmail, int userId, double parcelWeight, String toAddress,
            String fromAddress, String senderName, String receiverName, String bookingDate) {
        String host = "smtp.gmail.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress());
            System.out.println("Sending mail!!!");

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
            message.setSubject("Hi, your invoice!!!");

            message.setText("Sender Name " + senderName + "\n" + "Receiver Name " + receiverName + "\nReceiver Email "
                    + receiverEmail + "\nParcel Weight " + parcelWeight + "\nFrom Address " + fromAddress
                    + "\nTo Address " + toAddress + "\nBooking Data " + bookingDate);
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDeliveryMail(int parcelID, String email) {
        String host = "smtp.gmail.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress());
            System.out.println("Sending mail!!!");

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Hi, this is for success delivery!!!");
            message.setText("Your Parcel <Parcel ID>" + parcelID + " has been delivered successfully!!!");
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

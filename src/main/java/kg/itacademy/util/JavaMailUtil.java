//package kg.itacademy.util;
//
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Properties;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class JavaMailUtil {
//
//    public static void sendMail(String recepient) throws MessagingException {
//        System.out.println("Preparing to send email");
//        Properties properties = new Properties();
//
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.host", "smtp.gmail.com");
//        properties.put("mail.smtp.port", "587");
//
//        String myAccountEmail = "chestarpro@gmail.com";
//        String password = "TetriS2Cgoogle";
//
//        Session session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(myAccountEmail, password);
//            }
//        });
//
//        Message message = prepareMessage(session, myAccountEmail, recepient);
//        Transport.send(message);
//        System.out.println("Message sent successfully");
//    }
//
//    private static Message prepareMessage(Session session, String myAccountEmail, String recepient) {
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(myAccountEmail));
//            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
//            message.setSubject("hello world!");
//            message.setText("hello world!");
//            return message;
//        } catch (Exception e) {
//            Logger.getLogger(JavaMailUtil.class.getName()).log(Level.SEVERE, null, e);
//        }
//        return null;
//    }
//}
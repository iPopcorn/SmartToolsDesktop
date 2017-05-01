package main;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Properties;

/**
 * Created by Taylor on 2/2/2017.
 *
 * Code borrowed from:
 * https://www.tutorialspoint.com/javamail_api/javamail_api_send_email_with_attachment.htm
 *
 * EmailHandler - This class sends an email with a CSV attachment from a gmail account.
 */
public class EmailHandler {

    /** The email address that sends out the email from the system. Used in sendEmail()*/
    private String from = "uta.smart.tools@gmail.com";

    /** The username of the email account that sends emails from the system. Used in sendEmail()*/
    private String username = "uta.smart.tools@gmail.com";

    /** The password of the email account that sends emails from the system. Used in sendEmail()*/
    private String password = "spring17";

    /** The mail server to be used to send emails from the system. Used in sendEmail()*/
    private String host = "smtp.gmail.com";

    /** Empty constructor
     *
     *  EmailHandler() - constructs a new EmailHandler object, takes no parameters.
     *  @return Returns an instance of the EmailHandler class
     */
    public EmailHandler(){}

    /** Debugging method that Prints out the file attachment line by line.
     *
     *  void printFile() - Prints the given file line by line. Used as a debugging method by the sendEmail() method.
     *  @Param File object
     */
    public void printFile(File file){
        System.out.println("Begin EmailHandler.printFile()");
        try{
            FileInputStream stream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(stream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console
                System.out.println (strLine);
            }
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("End EmailHandler.printFile()");

    }


    /** Sends an email with a file attachment to the given email address.
     *
     *  void sendEmail() - Sends the credentials to the Gmail server to be authenticated. Then compiles an email and
     *  attaches the file to it. Finally it sends the email. Used by the ReportController.genReportEmail() method.
     *
     *  @param inventoryReport The file that is attached is assumed to be an inventory report from the ReportController
     *                         class.
*       @param toAddress The email address to send the email to.
     */
    public void sendEmail(String toAddress, File inventoryReport){
        System.out.println("EmailHandler.sendEmail()");
        this.printFile(inventoryReport);
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        String uName = this.username;
        String pass = this.password;

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(uName, pass);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(this.from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAddress));

            // Set Subject: header field
            message.setSubject("Testing Subject");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("This is message body");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = "InventoryReport.csv";
            DataSource source = new FileDataSource(inventoryReport);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("End EmailHandler.SendEmail()");
    }

    /** Sends an email with a file attachment to the given email address.
     *
     *  void sendEmail() - Sends the credentials to the Gmail server to be authenticated. Then compiles an email and
     *  attaches the file to it. Finally it sends the email. Used by the ReportController.genReportEmail() method.
     *
     *       @param toAddress The email address to send the email to.
     */
    public void sendEmail2(String toAddress, String subject, String body){
        System.out.println("EmailHandler.sendEmail()");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        String uName = this.username;
        String pass = this.password;

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(uName, pass);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(this.from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAddress));

            // Set Subject: header field
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(body);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);


            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("End EmailHandler.SendEmail()");
    }
}


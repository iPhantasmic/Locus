package com.cs203.locus.util;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
@EnableAsync
public class EmailUtil {

    @Autowired
    JavaMailSender mailSender;
    // FreeMarker Templates Config
    @Autowired
    Configuration fmConfiguration;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.sendgrid.api-key}")
    private String sendGridAPIKey;

    // Reset Password Email
    @Async
    public void sendResetEmail(Map<String,Object> formModel) throws IOException {
        // Initialise Email Content
        String recipientEmailAddress = (String) formModel.get("recipientEmailAddress");
        String mailSubject = "Reset Your Password - Locus ";
        String templatePath = "./src/main/resources/templates/forgot-pw-template.ftl";
        Path fileName = Path.of(templatePath);
        String htmlContent = Files.readString(fileName).replace("USERNAMEPLACEHOLDER", (String) formModel.get("userName")).replace("RESETPASSWORDLINKPLACEHOLDER", (String) formModel.get("resetPasswordLink"));
        Content content = new Content("text/html", htmlContent);

        // Send Customised Email
        sendGridEmail(recipientEmailAddress, mailSubject, content);
    }


    // Upon successful account signup, get User to verify and Welcome them in!
    @Async
    public void sendWelcomeEmail(Map<String,Object> formModel) throws IOException {
        // Initialise Email Content
        String recipientEmailAddress = (String) formModel.get("recipientEmailAddress");
        String mailSubject = "Welcome to the Locus Fam, " + formModel.get("userName");
        String templatePath = "./src/main/resources/templates/welcome-template.ftl";
        Path fileName = Path.of(templatePath);
        String htmlContent = Files.readString(fileName).replace("USERNAMEPLACEHOLDER", (String) formModel.get("userName")).replace("CONFIRMLINKPLACEHOLDER", (String) formModel.get("confirmEmailLink"));
        Content content = new Content("text/html", htmlContent);

        // Send customised Email
        sendGridEmail(recipientEmailAddress, mailSubject, content);
    }

    // Upon successful event signup, let Participants know they've successfully signed up for the following event
    @Async
    public void sendEventSignUpEmail(Map<String,Object> formModel) throws IOException {
        // Initialise Email Content
        String recipientEmailAddress = (String) formModel.get("recipientEmailAddress");
        String eventID = (String) formModel.get("eventId");
        String eventName = (String) formModel.get("eventName");
        String mailSubject = "Event #" + eventID + " " + eventName + " - You're in!";
        String templatePath = "./src/main/resources/templates/event-signed-up-template.ftl";
        Path fileName = Path.of(templatePath);
        String htmlContent = Files.readString(fileName).replace("USERNAMEPLACEHOLDER", (String) formModel.get("userName")).replace("EVENTNAMEPLACEHOLDER", eventName).replace("EVENTIDPLACEHOLDER", eventID);
        Content content = new Content("text/html", htmlContent);

        // Send customised Email
        sendGridEmail(recipientEmailAddress, mailSubject, content);
    }

    // Upon successful event creation, let Organisers know they have successfully created the event.
    @Async
    public void sendEventCreationEmail(Map<String,Object> formModel) throws MessagingException, IOException, TemplateException {
        // Initialise Email Content
        String recipientEmailAddress = (String) formModel.get("recipientEmailAddress");
        String eventID = (String) formModel.get("eventId");
        String eventName = (String) formModel.get("eventName");
        String mailSubject = "Event " + eventID + " " + eventName + " - We're all set!";
        String templatePath = "./src/main/resources/templates/event-created-template.ftl";
        Path fileName = Path.of(templatePath);
        String htmlContent = Files.readString(fileName).replace("USERNAMEPLACEHOLDER", (String) formModel.get("userName")).replace("EVENTNAMEPLACEHOLDER", eventName).replace("EVENTIDPLACEHOLDER", eventID);
        Content content = new Content("text/html", htmlContent);

        // Send customised Email
        sendGridEmail(recipientEmailAddress, mailSubject, content);
    }

    // Upon successful event creation, let Organisers know they have successfully created the event.
    @Async
    public void sendForgotUsernameEmail(Map<String,Object> formModel) throws MessagingException, IOException, TemplateException {
        // Initialise Email Content
        String recipientEmailAddress = (String) formModel.get("recipientEmailAddress");
        String mailSubject = "Your Locus Username";
        String templatePath = "./src/main/resources/templates/forgot-username-template.ftl";
        Path fileName = Path.of(templatePath);
        String htmlContent = Files.readString(fileName).replace("USERNAMEPLACEHOLDER", (String) formModel.get("userName")).replace("NAMEOFUSERPLACEHOLDER", (String) formModel.get("nameOfUser"));
        Content content = new Content("text/html", htmlContent);

        // Send customised Email
        sendGridEmail(recipientEmailAddress, mailSubject, content);
    }

    // Upon successful event creation, let Organisers know they have successfully created the event.
//    @Async
//    public void sendEventTypeUpdate(Map<String,Object> formModel) throws MessagingException, IOException, TemplateException {
//        // Initialise Email Content
//        String recipientEmailAddress = (String) formModel.get("recipientEmailAddress");
//        String mailSubject = "Updated Events:";
//        Template template = fmConfiguration.getTemplate("admin-updated-event.ftl");
//
//        // Send Customised Email
//        sendEmail(recipientEmailAddress, mailSubject, template, formModel);
//    }

    public void sendGridEmail(String recipientEmailAddress, String mailSubject, Content content) throws IOException{
        Email from = new Email(fromEmail);
        String subject = mailSubject;
        Email to = new Email(recipientEmailAddress);

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridAPIKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }

    // Upon any changes to their account details, let Users know of the change made
//    @Async
//    public void sendChangeMadeNoti(Map<String,Object> formModel) throws MessagingException, IOException, TemplateException {
//        // Initialise Email Content
//        String recipientEmailAddress = (String) formModel.get("recipientEmailAddress");
//        String changeMade = (String) formModel.get("changeMade");
//        String mailSubject = "Locus : A change has been made to your account details";
//        Template template = fmConfiguration.getTemplate("generic-change-template.ftl");
//
//        // Send Customised Email
//        sendGridEmail(recipientEmailAddress, mailSubject, content);
//    }

    // The following format is used to pass in details into the FreeMarker template
    // Example of usage of formModel
    // Map<String, Object> formModel = new HashMap<>();
    // formModel.put("eventName", eventName);
    // model.put("organiserCompanyName", organiserCompanyName);

//    public void sendEmail(String recipientEmailAddress, String mailSubject, Template template, Map<String,Object> formModel) throws MessagingException, TemplateException, IOException {
//        // Arrange Email Content
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
//        helper.setFrom(fromEmail);
//        helper.setTo(recipientEmailAddress);
//        helper.setSubject(mailSubject);
//
//        // formModel contains the custom information in the email, i.e name of recipient etc for personalization
//        helper.setText(FreeMarkerTemplateUtils.processTemplateIntoString(template, formModel), true);
//
//        // Send Email
//        mailSender.send(message);
//    }
}

package com.base.aws;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AwsSesUtil {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    /**
     * Send Mail using aws simple email service
     *
     * @param url
     * @param emailFrom
     * @param emailTo
     * @param emailBody
     * @param emailSubject
     */
    public void sendMail(String url, String emailFrom, String emailTo, String emailBody, String emailSubject) {
        emailBody = emailBody.replace("{link}", url);
        SendEmailRequest request =
                new SendEmailRequest()
                        .withDestination(new Destination().withToAddresses(emailTo))
                        .withMessage(
                                new Message()
                                        .withBody(
                                                new Body()
                                                        .withHtml(
                                                                new Content().withCharset("UTF-8").withData(emailBody)))
                                        .withSubject(new Content().withCharset("UTF-8").withData(emailSubject)))
                        .withSource(emailFrom);
        amazonSimpleEmailService.sendEmail(request);
        log.info("Email sent from {} to {}!", emailFrom, emailTo);
    }
}

package com.base.service.config.aws;

import javax.jms.TextMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AWSPublisher {
    private final JmsTemplate defaultJmsTemplate;

    public void publishMessageToQueue(String queueName, String message) {
        defaultJmsTemplate.send(queueName, session -> session.createTextMessage(message));
    }

    public void publishMessageToFIFOQueue(String queueName, String message, String groupId) {
        defaultJmsTemplate.send(
                queueName,
                session -> {
                    TextMessage textMessage = session.createTextMessage(message);
                    textMessage.setStringProperty("JMSXGroupID", groupId);
                    return textMessage;
                });
    }

    public void publishMessageToFIFOQueue(String queueName, String message, String groupId, String deduplicationId) {
        defaultJmsTemplate.send(
                queueName,
                session -> {
                    TextMessage textMessage = session.createTextMessage(message);
                    textMessage.setStringProperty("JMSXGroupID", groupId);
                    textMessage.setStringProperty("JMS_SQS_DeduplicationId", deduplicationId);
                    return textMessage;
                });
    }
}

package com.base.aws;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.base.commons.codes.BaseMessages;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AwsSNSUtil {
    private final AmazonSNS snsClient;

    /**
     * Publish Message to FIFO SNS Topic
     *
     * @param message
     * @param topicArn
     * @param messageGroupId
     * @param stringAttributes
     */
    public void publishToFIFOTopic(
            String message, String topicArn, String messageGroupId, Map<String, String> stringAttributes) {
        Map<String, MessageAttributeValue> attributes = mapToMessageAttributes(stringAttributes);
        PublishRequest request =
                new PublishRequest()
                        .withTopicArn(topicArn)
                        .withMessage(message)
                        .withMessageGroupId(messageGroupId)
                        .withMessageAttributes(attributes);
        PublishResult result = snsClient.publish(request);
        log.info(
                BaseMessages.MESSAGE_PUBLISHED_SUCCESSFULLY.get(
                        result.getMessageId(), result.getSdkHttpMetadata().getHttpStatusCode()));
    }

    /**
     * Subscribe an endpoint to SNS Message Attributes
     *
     * @param topicArn
     * @param protocol
     * @param endpoint
     */
    public void subscribeEndpointToTopic(String topicArn, String protocol, String endpoint) {
        SubscribeRequest request =
                new SubscribeRequest()
                        .withProtocol(protocol)
                        .withEndpoint(endpoint)
                        .withReturnSubscriptionArn(true)
                        .withTopicArn(topicArn);

        SubscribeResult result = snsClient.subscribe(request);
        log.info(
                BaseMessages.SUBSCRIBED_SUCCESSFULLY.get(
                        result.getSubscriptionArn(), result.getSdkHttpMetadata().getHttpStatusCode()));
    }

    /**
     * Converts a map of string, string to SNS Message
     *
     * @param stringAttributes
     * @return
     */
    private Map<String, MessageAttributeValue> mapToMessageAttributes(Map<String, String> stringAttributes) {
        if (Objects.isNull(stringAttributes) || stringAttributes.isEmpty()) {
            return null;
        }
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        stringAttributes.forEach(
                (attributeName, attributeValue) ->
                        attributes.put(
                                attributeName,
                                new MessageAttributeValue().withDataType("String").withStringValue(attributeValue)));
        return attributes;
    }
}

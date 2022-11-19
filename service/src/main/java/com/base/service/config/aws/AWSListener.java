package com.base.service.config.aws;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import javax.annotation.PostConstruct;
import javax.jms.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

@Slf4j
@EnableJms
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AWSListener {

    @Value("${aws.region}")
    private String region;

    @Value("${listener.concurrency}")
    private String concurrency;

    @Value("${listener.maxMessages.per.task}")
    private int maxMessagesPerTask;

    private AmazonSQS sqsClient;

    SQSConnectionFactory connectionFactory;

    /**
     * Build all the variables that will be needed by the class including the SQS Client and the connectionFactory for
     * AWS
     */
    @PostConstruct
    private void initializeAmazon() {
        sqsClient = AmazonSQSAsyncClientBuilder.standard().withRegion(region).build();

        connectionFactory = new SQSConnectionFactory(new ProviderConfiguration(), sqsClient);
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        /**
         * Set connection factory to use the SQS connection factory which was initiated in the post construct. JMS
         * Factory will use this to create JMS Connections
         */
        factory.setConnectionFactory(connectionFactory);
        /**
         * Sets the number of concurrent consumers that can run in parallel. Starts with the min value (default 1). As
         * messages increase this number ramps up to the upper value
         */
        factory.setConcurrency(concurrency);
        /**
         * This is the maximum number of messages that a task can handle before it is shut down. Keeping this value
         * since after number of threads gets ramped up the threads will continue to exist until the service gets
         * restarted. This property ensures that after processing 10 messages the thread will be killed and a new one
         * gets started only if message volume is still huge
         */
        factory.setMaxMessagesPerTask(maxMessagesPerTask);
        /** Setting Destination resolver as dynamic destination resolver to determine the 'Destination' references */
        factory.setDestinationResolver(new DynamicDestinationResolver());
        /**
         * Consumer must explicitly call the message's acknowledge method once it has successfully processed the
         * message. If Exception is thrown, the message will be released back to the queue and made visible again.
         */
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        /*
         * Log the errors when there is an unhandled exception in the JMSListener
         */
        factory.setErrorHandler(throwable -> log.error("JMSListener error: " + throwable.getMessage(), throwable));
        return factory;
    }

    /**
     * JMSTemplate will used to send messages to queue. The bean is created and instantiated for use, when needed.
     *
     * @return
     */
    @Bean
    public JmsTemplate defaultJmsTemplate() {
        return new JmsTemplate(this.connectionFactory);
    }
}

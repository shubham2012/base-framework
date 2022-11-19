package com.base.service.config.aws;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** AWS Simple mail service configuration */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AwsMailConfig {

    @Value("${aws.region}")
    private String AWS_REGION;

    /**
     * AWS simple email service bean
     *
     * @return
     */
    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        return AmazonSimpleEmailServiceClientBuilder.standard().withRegion(AWS_REGION).build();
    }
}

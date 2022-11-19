package com.base.service.config.aws;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AwsSNSConfig {

    @Value("${aws.region}")
    private String AWS_REGION;

    /**
     * SNS client bean
     *
     * @return
     */
    @Bean
    public AmazonSNS getAmazonSNSClient() {
        return AmazonSNSClientBuilder.standard().withRegion(AWS_REGION).build();
    }
}

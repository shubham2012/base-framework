package com.base.service.config.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** AWS S3 configuration */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AwsS3Config {

    @Value("${aws.region}")
    private String AWS_REGION;

    /**
     * S3 client bean
     *
     * @return
     */
    @Bean
    public AmazonS3 getAmazonS3Client() {
        return AmazonS3ClientBuilder.standard().withRegion(AWS_REGION).build();
    }
}

package com.base.commons.codes;

import java.text.MessageFormat;

public enum BaseMessages {
    DATA_NOT_FOUND("Data not found for id {0}"),
    FETCH_LIMIT_EXCEEDED("You can not fetch records more than {0}"),
    OFFSET_LESS_THEN_0("Offset can not be < 0"),
    SUCCESSFULLY_UPLOADED_TO_S3("Successfully uploaded the file to S3 bucket: Path: {0}  Result: {1}"),
    FAILED_S3_UPLOAD("Failed to Upload to S3  . File {0} :: Directory {1} :: Error :: {2}"),
    FAILED_S3_DOWNLOAD("Failed to Download from S3  . File {0} :: Directory {1} :: Error :: {2}"),
    S3_STREAM_FAILED("Failed to Upload to S3  . File {0} :: Error :: {2}"),
    LIMIT_LESS_THEN_1("Limit can not be < 1"),
    DATA_EMPTY("Data can not be empty"),
    MESSAGE_PUBLISHED_SUCCESSFULLY("Message sent. MessageId: {0}. Status: {1}"),
    SUBSCRIBED_SUCCESSFULLY("Subscription ARN: {0}. Status is {1}"),
    REFRESH_CONFIG_FAILED("Error occurred during Refresh Config: {0}"),
    REFRESH_CONFIG_SUCCESS("Refreshed Config for key & values {0}"),
    CONFIGURATION_KEY_UPDATE_SUCCESS("Updated key {0} to value {1}"),
    CONFIGURATION_KEY_NOT_IN_CONFIG_MAP(
            "key \"{0}\" not present in the service config so no need to capture the update"),
    MESSAGE_RECEIVED_REDIS_PUB_SUB("Message received from silhouette channel: {0}, message: {1}"),
    CONFIGURATION_UPDATE_FAILED(
            "Unable to update the config value from silhouette: message: {0}, errorMessage: {1}, error: {2}"),
    CRITERIA_VALIDATOR_FAILED("Criteria Validator failed for criteria :: {1}");

    public final String value;

    BaseMessages(String value) {
        this.value = value;
    }

    public String get(Object... args) {
        return new MessageFormat(this.value).format(args);
    }
}

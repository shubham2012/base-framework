package com.base.commons.entry;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadEntry {
    @NotNull(message = "File Name Should not be empty")
    private String fileName;

    @NotNull(message = "Upload Directory cannot be empty")
    private String uploadDirectory;

    private String contentType;

    @NotNull(message = "Bucket value cannot be empty")
    private String bucket;

    private Long expiry;
}

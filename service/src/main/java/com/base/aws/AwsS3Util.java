package com.base.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.base.commons.codes.BaseMessages;
import com.base.commons.entry.FileUploadEntry;
import com.csvreader.CsvWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AwsS3Util {

    private final AmazonS3 amazonS3;

    private static final Long DEFAULT_EXPIRY = 60 * 60 * 60L;

    /**
     * The file take in fileUpload params like file and bucket name, uploads to S3 using a helper function and return
     * preSignedUrl
     *
     * @param fileUploadEntry
     * @return
     */
    public String uploadAndGetPreSignedUrl(FileUploadEntry fileUploadEntry) {
        try {
            String uploadPath =
                    new StringBuilder(fileUploadEntry.getUploadDirectory())
                            .append("/")
                            .append(fileUploadEntry.getFileName())
                            .toString();
            byte[] bytes = Files.readAllBytes(Paths.get(fileUploadEntry.getFileName()));
            InputStream inputStream = new ByteArrayInputStream(bytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(fileUploadEntry.getContentType());
            metadata.setContentLength(bytes.length);

            return streamDataToS3(
                    inputStream, uploadPath, fileUploadEntry.getBucket(), metadata, fileUploadEntry.getExpiry());
        } catch (IOException e) {
            log.error(
                    BaseMessages.FAILED_S3_UPLOAD.get(
                            fileUploadEntry.getFileName(), fileUploadEntry.getUploadDirectory(), e.getMessage()));
        }
        return null;
    }

    /**
     * Upload File to AWS s3 with default expiry
     *
     * @param streamByteArray
     * @param uniqueFileName
     * @param bucketName
     * @return
     */
    public String uploadFileToS3(byte[] streamByteArray, String uniqueFileName, String bucketName) {
        return uploadFileToS3(streamByteArray, uniqueFileName, bucketName, DEFAULT_EXPIRY);
    }

    /**
     * Upload File to AWS s3 with expiry
     *
     * @param streamByteArray
     * @param uniqueFileName
     * @param bucketName
     * @param expiry
     * @return
     */
    public String uploadFileToS3(byte[] streamByteArray, String uniqueFileName, String bucketName, Long expiry) {
        log.info("Uploading file with name = " + uniqueFileName);
        InputStream inputStream = new ByteArrayInputStream(streamByteArray);
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(streamByteArray.length);
        final PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucketName, uniqueFileName, inputStream, metaData);
        amazonS3.putObject(putObjectRequest);
        return generatePreSignedURL(uniqueFileName, bucketName, expiry).toString();
    }

    /**
     * Upload File to AWS s3 with expiry as multipart Do not store the complete file in memory, instead send file by
     * part By default single part can contain 5MB of data if expiry is null or zero, file is generated with public
     * access
     *
     * @param file
     * @param uniqueFileName
     * @param bucketName
     * @param expiry
     * @return
     */
    public String uploadFileToS3AsMultipart(File file, String uniqueFileName, String bucketName, Long expiry)
            throws InterruptedException {
        try {
            TransferManager tm = TransferManagerBuilder.standard().withS3Client(amazonS3).build();

            PutObjectRequest por = new PutObjectRequest(bucketName, uniqueFileName, file);
            boolean isFilePublic = false;
            if (Objects.isNull(expiry) || Long.valueOf(0).equals(expiry)) {
                isFilePublic = true;
                por.withCannedAcl(CannedAccessControlList.PublicRead);
            }

            // TransferManager processes all transfers asynchronously,
            // so this call returns immediately.
            Upload upload = tm.upload(por);
            log.info("Uploading file with name = " + uniqueFileName + "started");
            upload.waitForCompletion();
            log.info("Upload file successfully completed - " + uniqueFileName);
            if (isFilePublic) {
                return amazonS3.getUrl(bucketName, uniqueFileName).toString();
            } else {
                return generatePreSignedURL(uniqueFileName, bucketName, expiry).toString();
            }
        } catch (InterruptedException e) {
            log.error("Upload operation failed withe error  - " + e.getMessage());
            throw e;
        }
    }

    /**
     * Generate Pre signed URL with expiry to get the file
     *
     * @param fileName
     * @param bucketName
     * @param expiry
     * @return
     */
    public URL generatePreSignedURL(String fileName, String bucketName, Long expiry) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(new Date(System.currentTimeMillis() + expiry));
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    /**
     * Generate Pre signed URL with expiry to put the file
     *
     * @param fileName
     * @param bucketName
     * @param expiry
     * @return
     */
    public URL generatePreSignedURLForUpload(String fileName, String bucketName, Long expiry) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(new Date(System.currentTimeMillis() + expiry));
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    /**
     * Function takes in the header and records and uploads the file to S3 in the give bucket and returns the preSigned
     * S3 Url
     *
     * @param headers
     * @param records
     * @param fileName
     * @param bucket
     * @return
     * @throws IOException
     */
    public String generateCsvAndGetPreSignedUrl(
            String[] headers, List<String[]> records, String fileName, String bucket) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        CsvWriter writer = new CsvWriter(stream, ',', StandardCharsets.ISO_8859_1);

        writer.writeRecord(headers);
        for (String[] record : records) {
            writer.writeRecord(record);
        }
        writer.close();
        stream.close();
        return uploadFileToS3(stream.toByteArray(), fileName, bucket);
    }

    /**
     * Writes data to s3 from an input stream directly
     *
     * @param streamData The input stream containing the data
     * @param fileNameWithPath The s3 path where the file needs to be stored
     * @param bucketName The bucket in which the file should be persisted
     * @param metadata The metadata pertaining to the file, if null uploads with empty metadata object
     * @param expiry The expiry period for the URL generated
     * @return a pre-signed URL for the file that is uploaded
     */
    private String streamDataToS3(
            InputStream streamData, String fileNameWithPath, String bucketName, ObjectMetadata metadata, Long expiry) {
        try {
            boolean isFilePublic = false;
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, fileNameWithPath, streamData, metadata);
            if (Objects.isNull(expiry) || Long.valueOf(0).equals(expiry)) {
                isFilePublic = true;
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            PutObjectResult result = this.amazonS3.putObject(putObjectRequest);
            log.debug(BaseMessages.SUCCESSFULLY_UPLOADED_TO_S3.get(fileNameWithPath, result.toString()));
            if (isFilePublic) {
                return amazonS3.getUrl(bucketName, fileNameWithPath).toString();
            } else {
                return generatePreSignedURL(fileNameWithPath, bucketName, expiry).toString();
            }
        } catch (AmazonServiceException e) {
            log.error(BaseMessages.S3_STREAM_FAILED.get(fileNameWithPath), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Streams data to s3 from an input stream
     *
     * @param streamData The input stream containing the data
     * @param fileNameWithPath The s3 path where the file needs to be stored
     * @param bucketName The bucket in which the file should be persisted
     * @param expiry The expiry period for the URL generated
     * @return a pre-signed URL for the file that is uploaded
     */
    public String streamDataToS3(InputStream streamData, String fileNameWithPath, String bucketName, Long expiry) {
        return streamDataToS3(streamData, fileNameWithPath, bucketName, new ObjectMetadata(), expiry);
    }

    /**
     * Streams data from s3 as input stream
     *
     * @param bucketName
     * @param fileNameWithPath
     * @return
     */
    public S3ObjectInputStream downloadFileFromS3(String bucketName, String fileNameWithPath) {
        log.info("Downloading file with name: {}", fileNameWithPath);
        try {
            S3Object s3object = amazonS3.getObject(bucketName, fileNameWithPath);
            return s3object.getObjectContent();
        } catch (AmazonServiceException e) {
            log.error(BaseMessages.FAILED_S3_DOWNLOAD.get(fileNameWithPath), bucketName, e.getMessage());
            throw e;
        }
    }
}

package com.sap.pls.samples.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3control.S3ControlClient;
import software.amazon.awssdk.services.s3control.model.ListAccessPointsRequest;
import software.amazon.awssdk.services.s3control.model.ListAccessPointsResponse;

@RestController
public class Controller {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final S3Client s3Client;
    private final S3ControlClient s3ControlClient;
    private final S3Client s3AccessPointClient;
    private final Config config;

    public Controller(S3Client s3Client,
                      S3ControlClient s3ControlClient,
                      S3Client s3AccessPointClient,
                      Config config) {
        this.s3Client = s3Client;
        this.s3ControlClient = s3ControlClient;
        this.s3AccessPointClient = s3AccessPointClient;
        this.config = config;
    }

    @GetMapping(value = "/buckets", produces = "application/json; charset=UTF-8")
    public ResponseEntity listBuckets() {
        ListBucketsRequest request = ListBucketsRequest
                .builder()
                .build();
        try {
            ListBucketsResponse response = s3Client.listBuckets(request);
            logger.info("Listing buckets with id '{}'. Status: {} Data: {}",
                    response.responseMetadata().requestId(), response.sdkHttpResponse().statusCode(), response.buckets());
            return ResponseEntity
                    .status(response.sdkHttpResponse().statusCode())
                    .body(response.sdkHttpResponse());

        } catch (S3Exception e) {
            logger.info("Listing bucket failed with id '{}'. Status: {} Error: {}", e.requestId(), e.statusCode(), e.getMessage());
            return ResponseEntity
                    .status(e.statusCode())
                    .body(e.getMessage());
        }
    }

    @GetMapping(value = "/bucket/{bucketName}", produces = "application/json; charset=UTF-8")
    public ResponseEntity listObjectsFromBucket(@PathVariable("bucketName") String bucketName) {
        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();
        try {
            ListObjectsResponse response = s3Client.listObjects(request);
            logger.info("Listing objects of the bucket: '{}' with id '{}'. Status: {}",
                    bucketName, response.responseMetadata().requestId(), response.sdkHttpResponse().statusCode());
            return ResponseEntity
                    .status(response.sdkHttpResponse().statusCode())
                    .body(response.sdkHttpResponse());
        } catch (S3Exception e) {
            logger.error("Listing objects of the bucket: '{}' with id '{}'. Status: {} Error: {}",
                    bucketName, e.requestId(), e.statusCode(), e.getMessage());
            return ResponseEntity
                    .status(e.statusCode())
                    .body(e.getMessage());
        }
    }

    @GetMapping(value = "/bucket/{bucketName}/{fileName}", produces = "application/json; charset=UTF-8")
    public ResponseEntity getObjectsFromBucket(@PathVariable("bucketName") String bucketName, @PathVariable("fileName") String fileName) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        try {
            GetObjectResponse response = s3Client.getObject(request).response();
            logger.info("Getting object: '{}' from the bucket: '{}' with id '{}'. Status: {}",
                    fileName, bucketName, response.responseMetadata().requestId(), response.sdkHttpResponse().statusCode());
            return ResponseEntity
                    .status(response.sdkHttpResponse().statusCode())
                    .body(response.sdkHttpResponse());
        } catch (S3Exception e) {
            logger.error("Getting objects of the bucket: '{}' with id '{}'. Status: {} Error: {}",
                    bucketName, e.requestId(), e.statusCode(), e.getMessage());
            return ResponseEntity
                    .status(e.statusCode())
                    .body(e.getMessage());
        }
    }

    @GetMapping(value = "/control/list-ap/{bucketName}", produces = "application/json; charset=UTF-8")
    public ResponseEntity listAccessPoints(@PathVariable("bucketName") String bucketName) {
        ListAccessPointsRequest request = ListAccessPointsRequest.builder()
                .bucket(bucketName)
                .accountId(config.getAccountId())
                .build();
        try {
            ListAccessPointsResponse response = s3ControlClient.listAccessPoints(request);
            logger.info("Listing access points of the bucket: '{}' with id '{}'. Status: {}",
                    bucketName, response.responseMetadata().requestId(), response.sdkHttpResponse().statusCode());
            return ResponseEntity
                    .status(response.sdkHttpResponse().statusCode())
                    .body(response.sdkHttpResponse());
        } catch (S3Exception e) {
            logger.error("Listing access points of the bucket: '{}' with id '{}'. Status: {} Error: {}",
                    bucketName, e.requestId(), e.statusCode(), e.getMessage());
            return ResponseEntity
                    .status(e.statusCode())
                    .body(e.getMessage());
        }
    }

    @GetMapping(value = "/accesspoint/{fileName}", produces = "application/json; charset=UTF-8")
    public ResponseEntity getObjectAccessPoint(@PathVariable("fileName") String fileName) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(config.getAccessPointArn())
                .key(fileName)
                .build();
        try {
            GetObjectResponse response = s3AccessPointClient.getObject(request).response();
            logger.info("Getting object: '{}' using access point ARN: '{}' with id '{}'. Status: {}",
                    fileName, config.getAccessPointArn(), response.responseMetadata().requestId(), response.sdkHttpResponse().statusCode());
            return ResponseEntity
                    .status(response.sdkHttpResponse().statusCode())
                    .body(response.sdkHttpResponse());
        } catch (S3Exception e) {
            logger.error("Getting object: '{}' using access point bucket ARN: '{}' with id '{}'. Status: {} Error: {}",
                    fileName, config.getAccessPointArn(), e.requestId(), e.statusCode(), e.getMessage());
            return ResponseEntity
                    .status(e.statusCode())
                    .body(e.getMessage());
        }
    }
}
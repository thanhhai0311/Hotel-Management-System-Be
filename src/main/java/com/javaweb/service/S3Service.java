package com.javaweb.service;

import java.io.IOException;
import java.time.Instant;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3Service {

	private S3Client s3;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	@Value("${aws.credentials.access-key}")
	private String ACCESS_KEY;

	@Value("${aws.credentials.secret-key}")
	private String SECRET_KEY;

	@PostConstruct
	public void init() {
		System.out.println("=== AWS CONFIG CHECK ===");
		System.out.println("Bucket Name: " + bucketName);
		System.out.println("Access Key: " + ACCESS_KEY);
		System.out.println("Secret Key: " + SECRET_KEY);
		System.out.println("========================");

		AwsBasicCredentials awsCreds = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
		this.s3 = S3Client.builder().region(Region.AP_SOUTHEAST_1)
				.credentialsProvider(StaticCredentialsProvider.create(awsCreds)).build();
	}

	public String uploadFile(MultipartFile file) throws IOException {
		String key = "images/" + Instant.now().getEpochSecond() + "-" + file.getOriginalFilename();

		try {
			s3.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).acl("public-read") // Cho phép xem công
																									// khai
					.contentType(file.getContentType()).build(),
					software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

			return "https://" + bucketName + ".s3.amazonaws.com/" + key;

		} catch (S3Exception e) {
			throw new RuntimeException("Lỗi upload ảnh: " + e.getMessage());
		}
	}
}

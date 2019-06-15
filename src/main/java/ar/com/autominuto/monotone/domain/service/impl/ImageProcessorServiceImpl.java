package ar.com.autominuto.monotone.domain.service.impl;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import ar.com.autominuto.monotone.domain.service.ImageProcessorService;
import ar.com.autominuto.monotone.utils.AutominutoS3Credentials;

@Service
public class ImageProcessorServiceImpl implements ImageProcessorService {

	private AmazonS3 s3Client;
	
	
	public ImageProcessorServiceImpl() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(AutominutoS3Credentials.AWS_KEY, AutominutoS3Credentials.AWS_SECRET);
		s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.SA_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
	}
	
	public InputStream getImage(String bucket, String key){
		S3Object object = s3Client.getObject(bucket, key);
		return (InputStream)object.getObjectContent();
	}
	
	
	public void saveImage(String bucket, String key, InputStream image) throws AmazonServiceException, AmazonClientException, InterruptedException{
		s3Client.putObject(new PutObjectRequest(bucket, key, image, null).withCannedAcl(CannedAccessControlList.PublicRead));
	}
	
	
	
}

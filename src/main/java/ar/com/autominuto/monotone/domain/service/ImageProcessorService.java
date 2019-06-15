package ar.com.autominuto.monotone.domain.service;

import java.io.InputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

public interface ImageProcessorService {

	InputStream getImage(String bucket, String key);
	
	void saveImage(String bucket, String key, InputStream image) throws AmazonServiceException, AmazonClientException, InterruptedException;
		
}

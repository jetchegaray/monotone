package ar.com.autominuto.monotone.service.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;

import ar.com.autominuto.monotone.domain.service.ImageProcessorService;
import ar.com.autominuto.monotone.utils.AutominutoS3BucketEnv;
import ar.com.autominuto.monotone.utils.BucketsType;
import ar.com.autominuto.monotone.utils.ImageWorker;

@RestController
@RequestMapping(value = "/images")
public class ImageController {

	private static final String ENVIRONMENT = "environment";
	private static final String SEPARATOR = "/";
	@Autowired
	private ImageProcessorService processorService;
	@Autowired
	private ImageWorker imageWorker;
	
	
	
	
	@RequestMapping(value="/{type}/{folderName}/{imageName:.+}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource>  getImage(@PathVariable String type, @PathVariable String folderName, @PathVariable String imageName, 
			@RequestParam(required=false) Integer width, @RequestParam(required=false) Integer height) {
		
		AutominutoS3BucketEnv envBucket = AutominutoS3BucketEnv.valueOf(System.getProperty(ENVIRONMENT));
	
		String selectedBucket = envBucket.getEnvironment();
		InputStream image = null;
		String fullName = org.apache.commons.lang3.StringUtils.EMPTY;
		try {
			
			fullName = getFullName(type, folderName, imageName, width, height);	
			
			image = processorService.getImage(selectedBucket , fullName);
			MediaType mediaType = (imageName.contains("png")) ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
			
			return ResponseEntity.ok()
		            .contentType(mediaType)
		            .body(new InputStreamResource(image));
			
		}catch(AmazonS3Exception e){
			
			try {
				InputStream imageResize = postProcess(type, folderName, imageName, image, width, height, selectedBucket);
				
				processorService.saveImage(selectedBucket, fullName, imageResize);
				InputStream result = processorService.getImage(selectedBucket, fullName);
				
				MediaType mediaType = (imageName.contains("png")) ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
				
				return ResponseEntity.ok()
			            .contentType(mediaType)
			            .body(new InputStreamResource(result));
			
			} catch (Exception e1) {
				return ResponseEntity.ok()
						.contentType(MediaType.IMAGE_PNG)
						.body(new InputStreamResource(getDefaultImage(type, width, height)));
			}
			
			
		} 		
	}


	
	
	
	private InputStream getDefaultImage(String type, Integer width, Integer height) {
		BucketsType typeBucket = BucketsType.getByName(type);
		InputStream is = null;
		if (typeBucket == BucketsType.COMMERCES){
			is = getClass().getClassLoader().getResourceAsStream("commerce.png");
		}else if (typeBucket == BucketsType.PRODUCTS){
			is = getClass().getClassLoader().getResourceAsStream("product.png");
		}else if (typeBucket == BucketsType.USERS){
			is = getClass().getClassLoader().getResourceAsStream("driver.png");
		}else if (typeBucket == BucketsType.COUPONS){
			is = getClass().getClassLoader().getResourceAsStream("coupon.png");
		}
		
		try {
			return imageWorker.cropAndResize(is, width, height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}





	private InputStream postProcess(String type, String folderName, String imageName, InputStream image, Integer width, Integer height, String selectedBucket) throws IOException, AmazonServiceException, AmazonClientException, InterruptedException {
		
		if (image == null){
			if (width != null && height != null){
				
				String fullNameWithoutSize = this.getFullName(type, folderName, imageName, null, null);
				InputStream imageWithoutSize = processorService.getImage(selectedBucket , fullNameWithoutSize);
				InputStream cropAndResizeImage = imageWorker.cropAndResize(imageWithoutSize, width, height);
			//	InputStream withWatermark = imageWorker.addWatermark(cropAndResizeImage);
				
				return cropAndResizeImage;
			}
		}
		return null;
		
	}





	private String getFullName(String type, String folderName, String imageName, Integer width, Integer height) {
		
		StringBuilder builder = new StringBuilder(BucketsType.getByName(type).getName());
		builder.append(SEPARATOR).append(StringUtils.capitalize(folderName)).append(SEPARATOR);
		
		if (width != null  && height != null){
			builder.append("Rw_").append(width).append("h_").append(height).append("_");
		}
		
		builder.append(imageName);
		
		return builder.toString().toLowerCase();
	}
	
	
	
	
	
	    
}

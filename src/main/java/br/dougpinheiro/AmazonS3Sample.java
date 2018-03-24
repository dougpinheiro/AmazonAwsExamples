package br.dougpinheiro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class AmazonS3Sample {

	public static void main(String[] args) {
		/*
		 * You need to subscribe at AWS and as well you are logged you can access "My Security Credentials" 
		 * to generate both "Access Key ID" and "Secret Access Key" as well. 
		 * Generating the is so easy. Just hit the accordion "Access Keys..." and then "Create New Access Key"
		 * Finally, you can copy both values and use them below
		 * */

		//1. Getting Credentials.
		System.out.println("Getting credentials...");

		BasicAWSCredentials awsCreds = new BasicAWSCredentials("<Replace here with your Access Key ID>", "<Replace here with your Secret Access Key>");

		System.out.println("Everything configured!");

		//2. Creating a client
		AmazonS3 client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.withRegion(Regions.SA_EAST_1)
				.build();

		//3. If new bucket name does not exists, so create the new one.
		Bucket myBucket;
		System.out.println("Creating a new bucket...");
		myBucket = client.createBucket("test");

		//4. Listing buckets.
		System.out.println("Listing all buckets...");

		client.listBuckets().stream().forEach((b) -> {
			System.out.println(b.getName()+" - "+b.getOwner().getDisplayName()+" - "+b.getCreationDate().toString());
		});

		//5. Send a file to the new bucket.
		File myFile = new File("d:/image-test.png");

		String s3FileKey = myFile.getName().toLowerCase();

		System.out.println("Sending the file '"+myFile.getName()+"'...");

		client.putObject("test", s3FileKey, myFile);
		System.out.println("File '"+myFile.getName()+"' sent!");		

		//6. Now you can also retrieve 
		S3Object fileFromBucket = client.getObject("test", "image-test.png");

		S3ObjectInputStream is = fileFromBucket.getObjectContent();
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("D:/anotherName.png");
			byte[] buffer = new byte[4096];
			int nRead;
			while ((nRead = is.read(buffer, 0, buffer.length)) != -1) {
				fos.write(buffer, 0, nRead);
			}
			is.close();
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

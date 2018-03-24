<!DOCTYPE html>
<head>
	<meta name="keywords" content="Java,AWS,S3,Amazon">
</head>

# How to use Amazon AWS S3 with Java

Hey everybody! <br/><br/>
Here you will learn how to create folders, called "buckets" from Amazon AWS S3 and also how to create, delete and retrieve files or "objects" from this buckets. <br/><br/>
First of all, you need to subscribe at AWS <a href="https://portal.aws.amazon.com/billing/signup#/start">(click here)</a>  and as well you are logged you can access "My Security Credentials" to generate both "Access Key ID" and "Secret Access Key" as well. <br/><br/> 
Generating the key is so easy. Just hit the accordion "Access Keys..." and then "Create New Access Key"<br/><br/>
Finally, you can copy both values and use them below.

# Requirements:
	
*	Java 8
*	AWS Java SDK
*	Maven

# Maven Dependency

	<dependency>
		<groupId>com.amazonaws</groupId>
		<artifactId>aws-java-sdk-s3</artifactId>
		<version>1.11.251</version>
	</dependency>

# Getting Credentials.

		System.out.println("Getting credentials...");
		BasicAWSCredentials awsCreds = new BasicAWSCredentials("<Replace here with your Access Key ID>", "<Replace here with your Secret Access Key>");
		System.out.println("Everything configured!");

# Creating a client

		AmazonS3 client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.withRegion(Regions.SA_EAST_1)
				.build();

# If new bucket name does not exists, so create the new one.

		Bucket myBucket;
		System.out.println("Creating a new bucket...");
		myBucket = client.createBucket("test");

# Listing buckets.
	
		System.out.println("Listing all buckets...");
		client.listBuckets().stream().forEach((b) -> {
			System.out.println(b.getName()+" - "+b.getOwner().getDisplayName()+" - "+b.getCreationDate().toString());
		});

# Send a file to the new bucket.

		File myFile = new File("d:/image-test.png");
		String s3FileKey = myFile.getName().toLowerCase();
		System.out.println("Sending the file '"+myFile.getName()+"'...");
		client.putObject("test", s3FileKey, myFile);
		System.out.println("File '"+myFile.getName()+"' sent!");	

# Now you can also retrieve the file.

		S3Object fileFromBucket = client.getObject("test", "image-test.png");
		S3ObjectInputStream is = fileFromBucket.getObjectContent();
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("d:/anotherName.png");
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

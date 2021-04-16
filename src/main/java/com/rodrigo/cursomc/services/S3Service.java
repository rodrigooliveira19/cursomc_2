package com.rodrigo.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.rodrigo.cursomc.exception.FileException;


@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class); 
	
	@Autowired
	private AmazonS3 s3Client; 
	
	@Value("${s3.bucket}")
	private String bucketName; 
	
	public URI uploadFile(MultipartFile multipartFile) {
		try {
			
			String fileName = multipartFile.getOriginalFilename(); 
			InputStream is = multipartFile.getInputStream(); 
			String contentType  =  multipartFile.getContentType(); 
			return uploadFile(fileName, is, contentType); 
		}catch(IOException e) {
			throw new FileException("Erro de ID: "+ e.getMessage()); 
		}
	}
	
	
	
	public URI uploadFile(String fileName,InputStream is,String contentType ) {
		try {
			ObjectMetadata meta =  new ObjectMetadata(); 
			meta.setContentType(contentType);
			LOG.info("Iniciando upload");
			s3Client.putObject(bucketName, fileName, is, meta); 
			LOG.info("Upload finalizado");
			return s3Client.getUrl(bucketName, fileName).toURI(); 
		} catch (Exception e) {
			throw new FileException("Erro ao converter URL para URI"); 
		}
	}
	
}

package com.rodrigo.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rodrigo.cursomc.exception.FileException;

@Service
public class ImageService {

	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		String ext =  FilenameUtils.getExtension(uploadedFile.getOriginalFilename()); 
		if(!"png".equals(ext) && !"jpg".equals(ext) ) {
			throw new FileException("Apenas imagens PNG ou JPG são permitidas"); 
		}
		
		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			if("png".equals(ext)) {
				img = pngToJpg(img); 
			}
			return img; 
		} catch (IOException e) {
			throw new FileException("Erro ao ler o arquivo"); 
		} 
	}

	private BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgimage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		jpgimage.createGraphics().drawImage(img, 0, 0,Color.WHITE, null); 
		return jpgimage; 
	}
	
	public InputStream getInputStream(BufferedImage img, String extension) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream(); 
			ImageIO.write(img, extension, os); 
			return new ByteArrayInputStream(os.toByteArray()); 
		} catch (Exception e) {
			throw new FileException("Erro ao ler arquivo"); 
		}
	}
}

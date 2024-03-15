package Controller;

import java.awt.image.BufferedImage;
import java.util.Iterator;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;

public class Image_Compression{
	public File Compression(String FileName) throws IOException{
		
		File input = new File(FileName);
		BufferedImage image = ImageIO.read(input);
		
		File compressedImageFile = new File("compress.jpg");
		OutputStream os = new FileOutputStream(compressedImageFile);
		
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter writer = (ImageWriter) writers.next();
		
		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		writer.setOutput(ios);
		
		ImageWriteParam param = writer.getDefaultWriteParam();
		
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(0.2f);
		writer.write(null, new IIOImage(image, null, null), param);
		
		os.close();
		ios.close();
		writer.dispose();
		return compressedImageFile;		
	}
	
}

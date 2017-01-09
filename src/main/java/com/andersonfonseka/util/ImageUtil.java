package com.andersonfonseka.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtil {
	
private static ImageUtil instance;
	
	public static ImageUtil getInstance(){
		if (instance == null) {
			instance = new ImageUtil();
		}
		return instance;
	}
	
	private ImageUtil(){}
	
	
	public ImageIcon getImageIcon(Class<?> clazz, String path) {
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(clazz.getResourceAsStream(path));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return new ImageIcon(img);
	}

}

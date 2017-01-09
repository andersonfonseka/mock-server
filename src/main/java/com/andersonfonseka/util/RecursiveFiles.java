package com.andersonfonseka.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecursiveFiles {
	
	List<String> paths = new ArrayList<String>();
	
	
	public void checkFiles(File file, String extension){
		
		File[] fileNames = file.listFiles();
		
		if (fileNames != null){
			for (File file2 : fileNames) {
				if (file2.isDirectory()){
					checkFiles(file2, extension);
				} else {
					if (file2.getName().indexOf(extension) > 0) {
						paths.add(file2.getAbsolutePath());
					}
				}
			}
		} else {
			paths.add(file.getAbsolutePath());
		}
	}
	
	public List<String> getPaths() {
		return paths;
	}



	public static void main(String[] args) {
		
		File file = new File("C:\\temp\\WADL_DEV1\\re-designinterface - soa");
		RecursiveFiles recursiveFiles = new RecursiveFiles();
		recursiveFiles.checkFiles(file, ".wadl");
		List<String> result = recursiveFiles.getPaths();
		
		for (String string : result) {
			System.out.println(string);
		}
		
	}

}

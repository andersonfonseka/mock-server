package com.andersonfonseka.generators;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateGenerator extends DataGenerator {

	@Override
	public String generate() {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss");
		return String.valueOf(dateFormat.format(new Date()));
	}

}

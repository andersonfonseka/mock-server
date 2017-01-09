package com.andersonfonseka.generators;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeGenerator extends DataGenerator {

	@Override
	public String generate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");
		return String.valueOf(dateFormat.format(new Date()));
	}

}

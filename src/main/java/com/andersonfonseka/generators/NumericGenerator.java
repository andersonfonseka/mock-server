package com.andersonfonseka.generators;

import java.util.Random;

public class NumericGenerator extends DataGenerator {

	@Override
	public String generate() {
		int rand = new Random().nextInt(1000);
		return String.valueOf(rand);
	}

}

package com.andersonfonseka.generators;

import java.util.Random;

public class DecimalGenerator extends DataGenerator {

	@Override
	public String generate() {
		double rand = (new Random().nextDouble()*1000) + 1;
		return String.valueOf(rand);
	}

}

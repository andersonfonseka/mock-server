package com.andersonfonseka.generators;

public class DataGeneratorFactory {

	private static DataGeneratorFactory instance;
	
	private DataGeneratorFactory() {}
	
	public static DataGeneratorFactory getInstance(){
		if (instance == null) {
			instance = new DataGeneratorFactory();
		}

		return instance;
	}
	
	public DataGenerator getGenerator(int type){
		
		switch(type){
			case 1:
				return new StringGenerator();
			case 2: case 3: case 4: case 9:
				return new NumericGenerator();
			case 5: case 6:
				return new DecimalGenerator();
			case 7:
				return new DateGenerator();
			case 8:
				return new DateTimeGenerator();
			default:
				return new StringGenerator();
		}

	}
	
}

package util;

import model.InputModel;

public class ModelConvertor {
	
	public static Object convertToInModel(String data){
		InputModel model = new InputModel();
		model.setName(data);
		return model;
	}
	public static Object convertToOutModel(String data){
		return null;
	}
}

package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

public class ResourceHandler {
	public static String getResource(String filePath) {
		String result = "";
		Reader reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath) );
		
		BufferedReader br = new BufferedReader(reader);
		
		try {
			String test = br.readLine();
			while(test!=null) {
				//읽은거 처리
				result += test;
				test = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static String getProperties(String filePath, String key) {
		String value = "";
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
	    Properties props = new Properties();
	    try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    value = (String) props.get(key);
	    
	    return value;
	}
}

package process.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import util.ModelConvertor;

public class FReader<T> implements ReaderIF<T> {
	private File inputFile = null;
	
	public FReader(String file) {
		this.inputFile = new File(file);
	}
	
	public ArrayList<T> read() {
//		String test = new String(Files.readAllBytes(this.inputFile.toPath()));
		ArrayList<T> result = new ArrayList<T>();
		BufferedReader fr = null;
		try{
			fr = new BufferedReader(new FileReader(this.inputFile));
			String line = "";
			while((line = fr.readLine()) != null) {
				result.add((T)ModelConvertor.convertToInModel(line));
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}finally {
			try {
				if(fr!=null)
					fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}

package process.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FWriter<T> implements WriterIF<T>{
	File outFile = null;
	
	public FWriter(String file) {
		outFile = new File(file);
	}
	public void print(ArrayList<T> content) {
		BufferedWriter bw  = null;
		try {
			bw = new BufferedWriter(new FileWriter(outFile));
			
			for(T t : content) {
				bw.append(t.toString());
				bw.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
				try {
					if (bw != null)
						bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}

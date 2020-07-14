package process.writer;

import java.util.ArrayList;

public class OutWriter<T> implements WriterIF<T>{
	
	public void print(ArrayList<T> content) {
		System.out.println("####### content #######");
		for(T t: content) {
			System.out.println(t.toString());
		}
	}

}

package process.reader;

import java.util.ArrayList;
import java.util.Scanner;

import model.BaseModel;
import util.ModelConvertor;

public class InputReader<T extends BaseModel> implements ReaderIF<T> {
	public ArrayList<T> read() {
		ArrayList<T> result = new ArrayList<T>();
		Scanner scan = new Scanner(System.in);
		
		while(scan.hasNext()) {
			String input = scan.nextLine();
			T t = (T)ModelConvertor.convertToInModel(input);
			result.add(t);
		}
		scan.close();
		return result;
	}
}

package process;

import java.util.ArrayList;

import model.BaseModel;
import process.reader.CHReader;
import process.reader.ReaderIF;
import process.writer.CHWriter;
import process.writer.WriterIF;

public abstract class AbstractMainProcess<T extends BaseModel,E extends BaseModel> implements ProcessIF{
//	private final String inputFilePath = "";
//	private final String outputFilePath = "";
	private final int listenPort = 9090;
	private final int serverPort = 9091;
	
	ReaderIF<T> reader = null;
	WriterIF<E> writer = null;
	
	public void init() {
		//file의 경우
//		reader = new FReader<T>(inputFilePath);
//		writer = new FWriter<E>(outputFilePath);
		
		//socket channel사용
		reader = new CHReader<T>(listenPort);
		writer = new CHWriter<E>(serverPort);
	}
	
	//reader,writer setting  필요

	public void start() {
		init();
		ArrayList<T> list = reader.read();
		ArrayList<E> result = this.process(list);
		writer.print(result);
	}
	
	public abstract ArrayList<E> process(ArrayList<T> source);
}

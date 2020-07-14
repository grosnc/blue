package process.writer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

import process.reader.ReaderIF;
import util.ModelConvertor;

public class CHWriter<T> implements WriterIF<T> {
	private int lPort = 0;
	Selector selector = null;
	SocketChannel sc = null;
	
	public CHWriter(int listenPort) {
		this.lPort = listenPort;
//		connect();
	}
	
	private void connect() {
		try {
			selector = Selector.open();
			sc = SocketChannel.open(new InetSocketAddress(this.lPort));//server로 연결
			sc.configureBlocking(false);
			sc.register(selector, SelectionKey.OP_READ);//이 채널 read key등록
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<T> read() {
		ArrayList<T> result = new ArrayList<T>();
		try {
			while(true && result.size() == 0) {
				selector.select();
				Iterator<SelectionKey> iter = selector.keys().iterator();
				
				while(iter.hasNext()) {
					SelectionKey key = iter.next();
					
					if(key.isReadable()) {
						result = this.readData(key);
					}
					iter.remove();
				}
			}
		} catch (IOException e) {			
			System.err.println("CHReader.read fails(" + e.getMessage());
		}
		
		return result;
	}
	
	//서버에서 읽을 때
	private ArrayList<T> readData(SelectionKey key){
		ArrayList<T> result = new ArrayList<T>();
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		
		SocketChannel readCh = (SocketChannel)key.channel();
		try {
			readCh.read(buffer);
		} catch (IOException e) {
			System.err.println("CHReader.readData fails(" + e.getMessage());
			if(readCh != null)
				try {
					readCh.close();
				} catch (IOException e1) {
					System.err.println("CHReader.readData fails(" + e1.getMessage());
				}
		}
		byte[] bufBytes = new byte[buffer.position()];
		buffer.flip();
		buffer.get(bufBytes);
		
		String strResult = new String(bufBytes);
		
		for(String str : strResult.split("\n")) {
			T t = (T)ModelConvertor.convertToInModel(str);
			result.add(t);
		}
		
		return result;
	}
	
	//서버로 쓸때
	public void print(ArrayList<T> data) {
		this.connect();
		
		StringBuffer strBuf = new StringBuffer();
		for (T t:data) {
			byte[] dataBytes = (t.toString() + "\n").getBytes();
//			ByteBuffer buffer = ByteBuffer.wrap(dataBytes);
//			ByteBuffer buffer = ByteBuffer.wrap(dataBytes);
			try {
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				buffer.clear();
				buffer.put(dataBytes);
				buffer.flip();
				sc.write(buffer);
				buffer.clear();
			} catch (IOException e) {
				System.err.println("CHReader.write fails(" + e.getMessage());
			}
		}		
		/*아니면 한번에
		 * ByteBuffer buf = ByteBuffer.allocateDirect(str.length());
		 * buf = ByteBuffer.wrap(str.toString().getBytes());
		 * sc.write(buf);
		 */
		
		try {
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

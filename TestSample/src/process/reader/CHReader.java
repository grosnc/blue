package process.reader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import util.ModelConvertor;

public class CHReader<T> implements ReaderIF<T> {
	private int lPort = 0;
	Selector selector = null;
	ServerSocketChannel ssc = null;
	
	public CHReader(int listenPort) {
		this.lPort = listenPort;
		init();
	}
	private void init() {
		//�ʱ�ȭ
		try {
			selector = Selector.open();
			ssc = ServerSocketChannel.open();//server�� ����
			ssc.bind(new InetSocketAddress(this.lPort));
			ssc.configureBlocking(false);
			ssc.register(selector, SelectionKey.OP_ACCEPT);//�� ä�� read key���
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<T> read() {
		ArrayList<T> result = new ArrayList<T>();
		//���� ó��
		while(true && result.size() == 0) {
			try {
				selector.select();
				Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
				
				while(iter.hasNext()) {
					SelectionKey key = iter.next();
					
					if (key.isAcceptable()) {
	                    accept(key);
	                }
	                else if (key.isReadable()) {
	                	result = readData(key);
	                }

					iter.remove();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	private void accept(SelectionKey key) throws IOException{
		ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
		
		SocketChannel sc = ssc.accept();
		
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
		
        System.out.println(sc.toString() + "Ŭ���̾�Ʈ�� �����߽��ϴ�.");
	}

	public ArrayList<T> readData(SelectionKey key) throws IOException{
		ArrayList<T> result = new ArrayList<T>();
		
		SocketChannel sc = (SocketChannel)key.channel();
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		sc.read(buffer);
		byte[] bytes = new byte[buffer.position()];
		buffer.flip();
		buffer.get(bytes);
		String recvData = new String(bytes);
		
		for(String str : recvData.split("\n")) {
//			if (str.equals("@@")) {//��ӵ� ���Ṯ�ڰ� ������
//				sc.close();
//				break;
//			}
			T t = (T)ModelConvertor.convertToInModel(str);
			result.add(t);
		}
		
		
		return result;
	}
}

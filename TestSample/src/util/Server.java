package util;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class Server {
	private final int listenPort = 9091;
    private Selector selector = null;
    private Vector room = new Vector();

    public void initServer() throws IOException {
        selector = Selector.open(); // Selector 열고
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // 채널 열고
        serverSocketChannel.configureBlocking(false); // Non-blocking 모드 설정
        serverSocketChannel.bind(new InetSocketAddress(listenPort)); // 12345 포트를 열어줍니다.

        // 서버소켓 채널을 셀렉터에 등록한다.
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void startServer() throws IOException {
        System.out.println("Server Start");

        while (true) {
            selector.select(); //select() 메소드로 준비된 이벤트가 있는지 확인한다.

            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) iterator.next();

                if (selectionKey.isAcceptable()) {
                    accept(selectionKey);
                }
                else if (selectionKey.isReadable()) {
                    read(selectionKey);
                }

                iterator.remove();
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();

        // 서버소켓 accept() 메소드로 클라이언트가 접속할 소켓을 생성한다.
        SocketChannel socketChannel = server.accept();

        // 생성된 소켓채널을 비 블록킹과 읽기 모드로 셀렉터에 등록한다.
        if (socketChannel == null)
            return;

        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        room.add(socketChannel); // 접속자 추가
        System.out.println(socketChannel.toString() + "클라이언트가 접속했습니다.");
    }

    private void read(SelectionKey key) {
        // SelectionKey 로부터 소켓채널을 얻는다.
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024); // buffer 생성

        try {
            int byteNum = socketChannel.read(byteBuffer); // 클라이언트 소켓으로부터 데이터를 읽음
            if(byteNum == -1) {
            	key.cancel();
            	socketChannel.close();
            	System.out.println("client connection is closed...");
            	return;
            }
        }
        catch (IOException ex) {
            try {
                socketChannel.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            room.remove(socketChannel);
            ex.printStackTrace();
        }

        try {
//            broadcast(byteBuffer);
        	byte[] data = new byte[byteBuffer.position()];
        	byteBuffer.flip();
        	byteBuffer.get(data);
        	System.out.println(new String(data));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        byteBuffer.clear();
    }

    private void broadcast(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.flip();
        Iterator iterator = room.iterator();

        while (iterator.hasNext()) {
            SocketChannel socketChannel = (SocketChannel) iterator.next();

            if (socketChannel != null) {
                socketChannel.write(byteBuffer);
                byteBuffer.rewind();
            }
        }
    }
    
    public static void main(String[] args) {
    	Server server = new Server();
    	try {
			server.initServer();
			server.startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
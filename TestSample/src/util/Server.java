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
        selector = Selector.open(); // Selector ����
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // ä�� ����
        serverSocketChannel.configureBlocking(false); // Non-blocking ��� ����
        serverSocketChannel.bind(new InetSocketAddress(listenPort)); // 12345 ��Ʈ�� �����ݴϴ�.

        // �������� ä���� �����Ϳ� ����Ѵ�.
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void startServer() throws IOException {
        System.out.println("Server Start");

        while (true) {
            selector.select(); //select() �޼ҵ�� �غ�� �̺�Ʈ�� �ִ��� Ȯ���Ѵ�.

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

        // �������� accept() �޼ҵ�� Ŭ���̾�Ʈ�� ������ ������ �����Ѵ�.
        SocketChannel socketChannel = server.accept();

        // ������ ����ä���� �� ���ŷ�� �б� ���� �����Ϳ� ����Ѵ�.
        if (socketChannel == null)
            return;

        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        room.add(socketChannel); // ������ �߰�
        System.out.println(socketChannel.toString() + "Ŭ���̾�Ʈ�� �����߽��ϴ�.");
    }

    private void read(SelectionKey key) {
        // SelectionKey �κ��� ����ä���� ��´�.
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024); // buffer ����

        try {
            int byteNum = socketChannel.read(byteBuffer); // Ŭ���̾�Ʈ �������κ��� �����͸� ����
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
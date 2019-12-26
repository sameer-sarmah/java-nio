package selector.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerUsingSelector {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 8080));
      //before registering a selectable channel to a selector, we must first set it to non-blocking mode
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
 
                if (key.isAcceptable()) {
                	ServerOperationHandler.handleAcceptEvent(key);
                }
 
                if (key.isReadable() || key.isWritable()) {
                	ServerOperationHandler.handleRequestEvent(key);
                }
            }
        }
    }
	
}

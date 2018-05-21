package nonblocking_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Product;

public class SingleThreadedNonBlockingServer {

	public static void main(String[] args) {
		ServerSocketChannel serverSocketChannel;

		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(8080));
			serverSocketChannel.configureBlocking(false);
			Selector selector = Selector.open();
			Map<SocketChannel,String> responseMap=new HashMap<>();
			/*
			 * registered the server to listen to accept event ,that is when clients have
			 * connected to the server now the selector will monitor the channel for
			 * "Accept" events the register method returns a SelectionKey which in turn
			 * contains corresponding Selector as well as Channel info
			 */
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			while (true) {
				/*
				 * This method blocks until at least one channel is ready for an operation. The
				 * integer returned represents the number of keys whose channels are ready for
				 * an operation.
				 * 
				 */
				selector.select();
				// each key represents a registered channel which is ready for an operation.
				Set<SelectionKey> keys = selector.selectedKeys();

				for (Iterator<SelectionKey> it = keys.iterator(); it.hasNext();) {
					SelectionKey key = it.next();
					it.remove();
					if (key.isValid()) {
						if (key.isAcceptable()) {
							RequestHandlerUtil.handleAcceptEvent(key,responseMap);
						} else if (key.isReadable()) {
							RequestHandlerUtil.handleReadEvent(key,responseMap);
						} else if (key.isWritable()) {
							RequestHandlerUtil.handleWriteEvent(key,responseMap);
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	


}

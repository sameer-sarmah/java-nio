package nonblocking_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedNonBlockingServer {
	public static void main(String[] args) {
		ServerSocketChannel serverSocketChannel;

		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(8080));
			serverSocketChannel.configureBlocking(false);
			Selector selector = Selector.open();
			Map<SocketChannel,String> responseMap=new HashMap<>();
		    ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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
							threadPool.submit(()->{
								try {
									RequestHandlerUtil.handleAcceptEvent(key,responseMap);
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
						} else if (key.isReadable()) {
							threadPool.submit(()->{
								try {
									RequestHandlerUtil.handleReadEvent(key,responseMap);
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
						} else if (key.isWritable()) {
							threadPool.submit(()->{
								try {
									RequestHandlerUtil.handleWriteEvent(key,responseMap);
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}

package selector.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

import model.Product;

public class ClientUsingSelector {

	public static void main(String[] args) {

		try {
			InetSocketAddress addr = new InetSocketAddress("localhost", 8080);
			SocketChannel clientChannel = SocketChannel.open(addr);
			
			Selector clientSelector = Selector.open();

			// before registering a selectable channel to a selector, we must first set it
			// to non-blocking mode
			clientChannel.configureBlocking(false);
			int supportedOperations = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
			clientChannel.register(clientSelector, supportedOperations);
			Instant startTime = Instant.now();
			while (true) {
				clientSelector.select();
				Set<SelectionKey> selectedKeys = clientSelector.selectedKeys();

				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

				while (keyIterator.hasNext()) {

					SelectionKey key = keyIterator.next();
					if (key.isReadable()) {
						ClientOperationHandler.readResponse(key);
					} else if (key.isWritable()) {
						ClientOperationHandler.sendRequest(key);
					}
					else if(key.isConnectable()) {
						System.out.println("Connected to server");
					}
				}
			}

		} catch (IOException ioe) {
			System.err.println("I/O error: " + ioe.getMessage());
		}

	}

}
package selector.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Product;
import service.ProductService;
/*
 * we can use one thread instead of several to manage multiple channels
 * */
public class ServerOperationHandler {
	
	public static void handleAcceptEvent(SelectionKey key) throws IOException {

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		// client which has connected with the server
		SocketChannel socketChannel = serverSocketChannel.accept();
		if (socketChannel != null) {
			System.out.println("Someone connected: " + socketChannel);
			socketChannel.configureBlocking(false);
			// the client channel is set in listening mode
			// the selector is handling the client SocketChannel along with the ServerSocketChannel
			int supportedOperations = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
			socketChannel.register(key.selector(), supportedOperations);
		}
	}

	public static void handleRequestEvent(SelectionKey key) throws IOException {
		/*the SelectionKey is corresponding to the read event for the client socket
		* it is generated as we had registered the SocketChannel in the same selector as ServerSocketChannel
		* we are listening to the select 
		**/
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer bufferForProductID = ByteBuffer.allocateDirect(100);
		int read = socketChannel.read(bufferForProductID);
		bufferForProductID.flip();
		System.out.println("bytes read " + read);
		if (read > 0) {
			byte[] bytes = new byte[bufferForProductID.remaining()];
			if (bufferForProductID.hasArray()) {
				bytes = bufferForProductID.array();
			} else {
				bufferForProductID.get(bytes);
			}
			int productID = Integer.parseInt(new String(bytes).trim());
			System.out.println(socketChannel + " request for product " + productID + " received in server");
			Product product = ProductService.getProduct(productID);
			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(product);
			ByteBuffer responseBuffer = ByteBuffer.wrap(json.getBytes());
			socketChannel.write(responseBuffer);
			System.out.println("response sent for product id "+productID);
		}
	}


}

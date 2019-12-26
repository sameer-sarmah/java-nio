package selector.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Product;
import service.ProductService;

public class ClientOperationHandler {
	
	public static void readResponse(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer bufferForProductResponse = ByteBuffer.allocateDirect(1000);
		int read = socketChannel.read(bufferForProductResponse);
		bufferForProductResponse.flip();
		System.out.println("bytes read " + read);
		if (read > 0) {
			byte[] bytes = new byte[bufferForProductResponse.remaining()];
			if (bufferForProductResponse.hasArray()) {
				bytes = bufferForProductResponse.array();
			} else {
				bufferForProductResponse.get(bytes);
			}
			String jsonResponse = new String(bytes).trim();
			Gson gson = new GsonBuilder().create();
			Product product = gson.fromJson(jsonResponse, Product.class);
			System.out.println("response received from server");
			System.out.println(product.getProductName());
		}
	}

	public static void sendRequest(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		Random random = new Random();
		int productID = random.nextInt(77);
		String productIDStr = String.valueOf(productID);
		ByteBuffer buffer = ByteBuffer.wrap(productIDStr.getBytes());
		socketChannel.write(buffer);
		System.out.println("request sent for product id "+productID);
	}
	
	
	public static boolean isOperationReady(Set<SelectionKey> selectedKeys,Predicate<SelectionKey> predicate) {
		Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
		while(keyIterator.hasNext()) {
		    SelectionKey key = keyIterator.next();
		    if(predicate.test(key)){
		    	return true;
		    }
		}
		return false;
	}
}

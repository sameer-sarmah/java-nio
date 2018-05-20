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

public class NonBlockingServer {

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
							handleAcceptEvent(key,responseMap);
						} else if (key.isReadable()) {
							handleReadEvent(key,responseMap);
						} else if (key.isWritable()) {
							handleWriteEvent(key,responseMap);
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	 private static void handleAcceptEvent(SelectionKey key,Map<SocketChannel,String> responseMap) throws IOException {
		 
		    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		    SocketChannel socketChannel = serverSocketChannel.accept(); 
		    System.out.println("Someone connected: " + socketChannel);
		    socketChannel.configureBlocking(false);
		    socketChannel.register(key.selector(), SelectionKey.OP_READ);
	 }
	 
	 private static void handleReadEvent(SelectionKey key,Map<SocketChannel,String> responseMap) throws IOException {
		    SocketChannel socketChannel = (SocketChannel) key.channel();
		    ByteBuffer bufferForProductID = ByteBuffer.allocateDirect(100);
		    int read = socketChannel.read(bufferForProductID);
		    bufferForProductID.flip();
		    System.out.println("bytes read "+read);
		    if (read == -1) {
		      responseMap.remove(socketChannel);
		      return;
		    }
		    if (read > 0) {
		      byte[] bytes=new byte[bufferForProductID.remaining()];
		      if(bufferForProductID.hasArray()) {
		    	  bytes=bufferForProductID.array();
		      }
		      else {
		    	 bufferForProductID.get(bytes); 
		      }
		      int productID = Integer.parseInt(new String(bytes, Charset.defaultCharset()).trim());
		      System.out.println(socketChannel + " request for product "+productID+" received in server" );
			  Product product=ProductService.getProduct(productID);
			  Gson gson = new GsonBuilder().create();
			  String json=gson.toJson(product);
			  responseMap.put(socketChannel, json);
		      key.interestOps(SelectionKey.OP_WRITE);
		    }
		  }
	  
	 private static void handleWriteEvent(SelectionKey key,Map<SocketChannel,String> responseMap) throws IOException {
		    SocketChannel socketChannel = (SocketChannel) key.channel();
		    String json= responseMap.get(socketChannel);
		    ByteBuffer productJSON = ByteBuffer.wrap(json.getBytes(Charset.defaultCharset()));
		    socketChannel.write(productJSON);
		    key.interestOps(SelectionKey.OP_READ);
		    responseMap.remove(socketChannel);
		  }

}

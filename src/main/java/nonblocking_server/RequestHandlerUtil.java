package nonblocking_server;

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

public class RequestHandlerUtil {
	 public static void handleAcceptEvent(SelectionKey key,Map<SocketChannel,String> responseMap) throws IOException {
		 
		    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		    SocketChannel socketChannel = serverSocketChannel.accept(); 
		    if(socketChannel != null) {
		    System.out.println("Someone connected: " + socketChannel);
		    socketChannel.configureBlocking(false);
		    socketChannel.register(key.selector(), SelectionKey.OP_READ);
		    }
	 }
	 
	 public static void handleReadEvent(SelectionKey key,Map<SocketChannel,String> responseMap) throws IOException {
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
	  
	 public static void handleWriteEvent(SelectionKey key,Map<SocketChannel,String> responseMap) throws IOException {
		    SocketChannel socketChannel = (SocketChannel) key.channel();
		    String json= responseMap.get(socketChannel);
		    ByteBuffer productJSON = ByteBuffer.wrap(json.getBytes(Charset.defaultCharset()));
		    socketChannel.write(productJSON);
		    key.interestOps(SelectionKey.OP_READ);
		    responseMap.remove(socketChannel);
		  }
}

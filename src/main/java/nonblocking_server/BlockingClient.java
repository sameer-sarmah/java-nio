package nonblocking_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Product;

public class BlockingClient implements Callable<Product>{
	 private Optional<Product> handleReadEvent(SocketChannel socketChannel) throws IOException {
		    Optional<Product> product =Optional.empty();
		    ByteBuffer bufferForProductID = ByteBuffer.allocateDirect(500);
		    //blocking call
		    int read = socketChannel.read(bufferForProductID);
		    bufferForProductID.flip();
		    System.out.println("read "+read+" bytes in client,in thread "+Thread.currentThread().getName());
		    if (read == -1) {
		      return product;
		    }
		    if (read > 0) {
		      byte[] bytes=new byte[bufferForProductID.remaining()];
		      if(bufferForProductID.hasArray()) {
		    	  bytes=bufferForProductID.array();
		      }
		      else {
		    	 bufferForProductID.get(bytes); 
		      }
		      String json = new String(bytes, Charset.defaultCharset()).trim();
			  Gson gson = new GsonBuilder().create();
			  Product deserializedProduct=gson.fromJson(json, Product.class);
			  System.out.println("read product in client,in thread "+Thread.currentThread().getName());
		      System.out.println(deserializedProduct);
		      product=Optional.of(deserializedProduct);
		    }
		    return product;
		  }
	  
	 private void handleWriteEvent(SocketChannel socketChannel) throws IOException {
		    Random random=new Random();
			int productID=random.nextInt(77);
			String productIDStr=String.valueOf(productID);
			ByteBuffer buffer=ByteBuffer.wrap(productIDStr.getBytes(Charset.defaultCharset()));
			//blocking call
			socketChannel.write(buffer);
			System.out.println("querying product id "+productID+" in thread "+Thread.currentThread().getName());
		  }
	@Override
	public Product call() throws Exception {
		
	      try
	      {
	         SocketChannel socketChannel = SocketChannel.open();
	         InetSocketAddress addr = new InetSocketAddress("localhost", 8080);
	         socketChannel.connect(addr);
	         handleWriteEvent( socketChannel);
	         handleReadEvent(socketChannel);
	         socketChannel.close();
	      }     
	      catch (IOException ioe)
	      {
	         System.err.println("I/O error: " + ioe.getMessage());
	      }
		return null;
	}

}

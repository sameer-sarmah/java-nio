package nonblocking_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Product;

public class NonBlockingClient implements Callable<Product> {
	 private Optional<Product> handleReadEvent(SelectionKey key) throws IOException {
		    Optional<Product> product =Optional.empty();
		    SocketChannel socketChannel = (SocketChannel) key.channel();
		    ByteBuffer bufferForProductID = ByteBuffer.allocateDirect(500);
		    int read = socketChannel.read(bufferForProductID);
		    bufferForProductID.flip();
		    System.out.println("read "+read+" bytes,in thread "+Thread.currentThread().getName());
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
		      String json = new String(bufferForProductID.array(), Charset.defaultCharset()).trim();
			  Gson gson = new GsonBuilder().create();
			  Product deserializedProduct=gson.fromJson(json, Product.class);
			  System.out.println("read product,in thread "+Thread.currentThread().getName());
		      System.out.println(deserializedProduct);
		      product=Optional.of(deserializedProduct);
		    }
		    return product;
		  }
	  
	 private void handleWriteEvent(SelectionKey key) throws IOException {
		    SocketChannel socketChannel = (SocketChannel) key.channel();
		    Random random=new Random();
			int productID=random.nextInt(77);
			String productIDStr=String.valueOf(productID);
			ByteBuffer buffer=ByteBuffer.wrap(productIDStr.getBytes(Charset.defaultCharset()));
			socketChannel.write(buffer);
			System.out.println("querying product id "+productID+" in thread "+Thread.currentThread().getName());
		    key.interestOps(SelectionKey.OP_WRITE);
		  }

	@Override
	public Product call() throws Exception {
		Optional<Product> product =Optional.empty();
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		InetSocketAddress addr = new InetSocketAddress("localhost", 8080);
		socketChannel.connect(addr);
		Selector selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_WRITE);
		while(!socketChannel.finishConnect()) {
			System.out.println("waiting for connection to be established");
		}
		boolean requestSent=false;
		boolean responseRetrieved=false;
		while (!responseRetrieved) {
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
					if (key.isReadable() ) {
						product=handleReadEvent(key);
						responseRetrieved=true;
						break;
					} else if (key.isWritable() && !requestSent) {
						handleWriteEvent(key);
						requestSent=true;
					}
				}
			}
		}
		return product.get();
	}
}

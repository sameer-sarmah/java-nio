package async;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.Callable;

import model.Product;

public class AsyncClient implements Callable<Product> {
	@Override
	public Product call() throws Exception {

		try {
			AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();
			InetSocketAddress addr = new InetSocketAddress("localhost", 8080);
			clientChannel.connect(addr, null, new ClientConnectionHandler());
			Random random = new Random();
			int productID = random.nextInt(77);
			String productIDStr = String.valueOf(productID);
			ByteBuffer buffer = ByteBuffer.wrap(productIDStr.getBytes(Charset.defaultCharset()));
			ClientReqResObject reqResObj=new ClientReqResObject(productID,buffer,null,null);
			clientChannel.write(buffer, reqResObj, new ClientWriteHandler());
			ByteBuffer bufferForProduct = ByteBuffer.allocateDirect(500);
			reqResObj.setBufferForProduct(bufferForProduct);
			clientChannel.read(bufferForProduct, reqResObj, new ClientReadHandler());
			clientChannel.close();
		} catch (IOException ioe) {
			System.err.println("I/O error: " + ioe.getMessage());
		}
		return null;
	}

}

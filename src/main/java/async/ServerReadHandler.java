package async;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Product;
import nonblocking_server.ProductService;

public class ServerReadHandler implements CompletionHandler<Integer, ServerReqResObject>{

	@Override
	public void completed(Integer readBytes, ServerReqResObject reqRes) {
	    System.out.println("bytes read "+readBytes);
	    ByteBuffer bufferForProductID=reqRes.getBufferForProductID();
	    bufferForProductID.flip();
	    if (readBytes > 0) {
	      byte[] bytes=new byte[bufferForProductID.remaining()];
	      if(bufferForProductID.hasArray()) {
	    	  bytes=bufferForProductID.array();
	      }
	      else {
	    	 bufferForProductID.get(bytes); 
	      }
	      int productID = Integer.parseInt(new String(bytes, Charset.defaultCharset()).trim());
	      System.out.println(" request for product "+productID+" received in server" );
	      reqRes.setRequestedProductID(productID);
	      AsynchronousSocketChannel client = reqRes.getChannelClient();
	      Product product=ProductService.getProduct(productID);
		  Gson gson = new GsonBuilder().create();
		  String json=gson.toJson(product);
		  reqRes.setProductJsonResponse(json);
		  ByteBuffer bufferForProduct=ByteBuffer.wrap(json.getBytes());
	      client.write(bufferForProduct, reqRes, new ServerWriteHandler());
	    }
		
	}

	@Override
	public void failed(Throwable exc, ServerReqResObject reqRes) {
		// TODO Auto-generated method stub
		
	}

}

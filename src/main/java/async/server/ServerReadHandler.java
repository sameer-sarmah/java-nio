package async.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Product;
import nonblocking_server.ProductService;

public class ServerReadHandler implements CompletionHandler<Integer, ServerReqResObject>{

	/*the completed method is called when read/write is completed ,in this case everytime a request is received
	 * the first argument is the number of bytes read and the second is a custom object passed to help with the processing
	 * of the request in this case
	 * 
	 * 1.we read the bufferForProductID as it contains the product id for which we have to send the entire product
	 * payload in the response.
	 * 
	 * 2.once we retrieve the product id ,we fetch the product using ProductService and serialize it.
	 * 3.We retreive the client socket and write the response.
	 * 4.ServerWriteHandler has methods to handle success and failure
	 * */
	@Override
	public void completed(Integer readBytes, ServerReqResObject reqRes) {
	    System.out.println("bytes read "+readBytes);
	    ByteBuffer bufferForProductID=reqRes.getBufferForProductID();
	    //we flip the buffer to read the contents,flipping would set position to 0 thus we can read from the beginning
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

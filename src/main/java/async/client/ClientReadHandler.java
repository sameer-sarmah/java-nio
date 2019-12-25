package async.client;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Product;
/*the completed method is called when read is completed ,in this case every time a response is fetched from the server
 * the first argument is the number of bytes read and the second is a custom object passed to help with the processing
 * of the server response in this case
 * 
 * 1.once we receive the product json response we print the received products
 * */
public class ClientReadHandler implements CompletionHandler<Integer, ClientReqResObject>{
	@Override
	public void completed(Integer result, ClientReqResObject reqResObj) {
		  ByteBuffer buffer= reqResObj.getBufferForProduct();
	      byte[] bytes=new byte[buffer.remaining()];
	      if(buffer.hasArray()) {
	    	  bytes=buffer.array();
	      }
	      else {
	    	  buffer.get(bytes); 
	      }
	      String json = new String(bytes, Charset.defaultCharset()).trim();
		  Gson gson = new GsonBuilder().create();
		  Product deserializedProduct=gson.fromJson(json, Product.class);
		  System.out.println("read product in client,in thread "+Thread.currentThread().getName());
	      System.out.println(deserializedProduct);
	}

	@Override
	public void failed(Throwable t, ClientReqResObject buffer) {
		System.out.println("Failure: " + t.toString());
	}
}

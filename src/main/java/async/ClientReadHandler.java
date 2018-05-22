package async;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Product;

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

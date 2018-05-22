package async;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ServerWriteHandler implements CompletionHandler<Integer, ServerReqResObject> {

	@Override
	public void completed(Integer result, ServerReqResObject reqResObject) {
		  int  productID= reqResObject.getRequestedProductID();
		  String productJson=reqResObject.getProductJsonResponse();
		  System.out.println("response for product id "+productID +" is: ");
		  System.out.println(productJson);
		  AsynchronousSocketChannel client = reqResObject.getChannelClient();
		  ByteBuffer bufferForProductID = ByteBuffer.allocateDirect(100);
		  reqResObject.setBufferForProductID(bufferForProductID);
		  client.read(bufferForProductID, reqResObject, new ServerReadHandler());
		 
	}

	@Override
	public void failed(Throwable exc, ServerReqResObject reqResObject) {

		
	}

}

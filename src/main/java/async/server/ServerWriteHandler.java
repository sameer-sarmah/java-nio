package async.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
/*the completed method is called when read/write is completed ,in this case every time a response is sent to the client
 * the first argument is the number of bytes read and the second is a custom object passed to help with the processing
 * of the request in this case
 * 
 * 1.once we send the product json response ,we retreive the client socket and set bufferForProductID for new client request,we may also clear the existing bufferForProductID.
 * 2.ServerReadHandler has methods to handle success and failure when a client sends any request
 * */
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

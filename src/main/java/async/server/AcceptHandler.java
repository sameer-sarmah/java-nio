package async.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
/*the completed method is called when read/write is completed ,in this case every time a response is sent to the client
 * the first argument is the connected client and the second is a custom object passed to help with the processing
 * of the request in this case
 * 
 * 1.once a client is connected ,we use the provided client socket and set bufferForProductID for new client request
 * 2.ServerReadHandler has methods to handle success and failure when a client sends any request
 * */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, ServerReqResObject>{

	@Override
	public void completed(AsynchronousSocketChannel client, ServerReqResObject reqResObj) {
		System.out.println("Someone connected: " + client);
		reqResObj.setChannelClient(client);
		ByteBuffer bufferForProductID = ByteBuffer.allocateDirect(100);
		reqResObj.setBufferForProductID(bufferForProductID);
		client.read(bufferForProductID, reqResObj, new ServerReadHandler());
	}

	@Override
	public void failed(Throwable exc, ServerReqResObject reqResObj) {
		 System.out.println("error in establishing connection");
	}

}

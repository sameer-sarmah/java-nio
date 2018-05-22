package async;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

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

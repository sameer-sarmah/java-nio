package async.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

import async.ReqResObject;

public class ServerReqResObject extends ReqResObject{
    private AsynchronousServerSocketChannel channelServer;
    private AsynchronousSocketChannel channelClient;
	public ServerReqResObject(int requestedProductID, ByteBuffer bufferForProductID, String productJsonResponse,
			ByteBuffer bufferForProduct) {
		super(requestedProductID, bufferForProductID, productJsonResponse, bufferForProduct);
	}
	
	public ServerReqResObject() {
		super(-1, null, null, null);
	}
	public AsynchronousServerSocketChannel getChannelServer() {
		return channelServer;
	}
	public void setChannelServer(AsynchronousServerSocketChannel channelServer) {
		this.channelServer = channelServer;
	}
	public AsynchronousSocketChannel getChannelClient() {
		return channelClient;
	}
	public void setChannelClient(AsynchronousSocketChannel channelClient) {
		this.channelClient = channelClient;
	}
	
	

}

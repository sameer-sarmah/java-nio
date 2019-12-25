package async;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

import async.server.AcceptHandler;
import async.server.ServerReqResObject;

public class SingleThreadedAsyncServer {

	public static void main(String[] args) {
		AsynchronousServerSocketChannel serverSocketChannel;

		try {
			serverSocketChannel = AsynchronousServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(8080));
			ServerReqResObject reqRes=new ServerReqResObject();
			reqRes.setChannelServer(serverSocketChannel);
			serverSocketChannel.accept(reqRes, new AcceptHandler());
			
			Thread.sleep(100000);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	


}

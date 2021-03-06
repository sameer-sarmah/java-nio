package async.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class SingleThreadedAsyncServer {

	public static void main(String[] args) {
		AsynchronousServerSocketChannel serverSocketChannel;

		try {
			serverSocketChannel = AsynchronousServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(8080));
			ServerReqResObject reqRes=new ServerReqResObject();
			reqRes.setChannelServer(serverSocketChannel);
			//now ready to accept connection at 8080
			serverSocketChannel.accept(reqRes, new AcceptHandler());
			
			Thread.sleep(100000);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	


}

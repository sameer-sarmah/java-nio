package async.client;

import java.nio.channels.CompletionHandler;

public class ClientConnectionHandler implements CompletionHandler<Void, Void>{

	@Override
	public void completed(Void result, Void attachment) {
		System.out.println("connection established to the server");
	}

	@Override
	public void failed(Throwable exc, Void attachment) {
		System.out.println("connection could not be established to the server");
	}

}

package async;

import java.nio.channels.CompletionHandler;

public class ClientWriteHandler implements CompletionHandler<Integer, ClientReqResObject>{

	@Override
	public void completed(Integer result, ClientReqResObject reqResObj) {
		    int productID=reqResObj.getRequestedProductID();
			System.out.println("querying product id "+productID+" in thread "+Thread.currentThread().getName());	
	}

	@Override
	public void failed(Throwable exc, ClientReqResObject reqResObj) {
	
		
	}

}

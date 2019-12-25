package async.client;

import java.nio.channels.CompletionHandler;
/* the completed method is called when request to server with a product id is completed
 * the first argument is the number of bytes actually sent to the server,the content of product id bytes
 * */
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

package async;

import java.nio.channels.CompletionHandler;

public class ServerWriteHandler implements CompletionHandler<Integer, ServerReqResObject> {

	@Override
	public void completed(Integer result, ServerReqResObject reqResObject) {
		  int  productID= reqResObject.getRequestedProductID();
		  String productJson=reqResObject.getProductJsonResponse();
		  System.out.println("response for product id "+productID +" is: ");
		  System.out.println(productJson);
		 
	}

	@Override
	public void failed(Throwable exc, ServerReqResObject reqResObject) {

		
	}

}

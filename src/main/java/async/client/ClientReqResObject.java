package async.client;

import java.nio.ByteBuffer;

import async.ReqResObject;

public class ClientReqResObject extends ReqResObject{

	public ClientReqResObject(int requestedProductID, ByteBuffer bufferForProductID, String productJsonResponse,
			ByteBuffer bufferForProduct) {
		super(requestedProductID, bufferForProductID, productJsonResponse, bufferForProduct);
	}

}

package async;

import java.nio.ByteBuffer;

public class ClientReqResObject extends ReqResObject{

	public ClientReqResObject(int requestedProductID, ByteBuffer bufferForProductID, String productJsonResponse,
			ByteBuffer bufferForProduct) {
		super(requestedProductID, bufferForProductID, productJsonResponse, bufferForProduct);
	}

}

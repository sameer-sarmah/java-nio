package async;

import java.nio.ByteBuffer;

public class ReqResObject {
	private int requestedProductID;
	private ByteBuffer bufferForProductID;
	private String productJsonResponse;
	private ByteBuffer bufferForProduct;

	public ReqResObject(int requestedProductID, ByteBuffer bufferForProductID, String productJsonResponse,
			ByteBuffer bufferForProduct) {
		super();
		this.requestedProductID = requestedProductID;
		this.bufferForProductID = bufferForProductID;
		this.productJsonResponse = productJsonResponse;
		this.bufferForProduct = bufferForProduct;
	}

	public int getRequestedProductID() {
		return requestedProductID;
	}

	public ByteBuffer getBufferForProductID() {
		return bufferForProductID;
	}

	public String getProductJsonResponse() {
		return productJsonResponse;
	}

	public ByteBuffer getBufferForProduct() {
		return bufferForProduct;
	}

	public void setRequestedProductID(int requestedProductID) {
		this.requestedProductID = requestedProductID;
	}

	public void setBufferForProductID(ByteBuffer bufferForProductID) {
		this.bufferForProductID = bufferForProductID;
	}

	public void setProductJsonResponse(String productJsonResponse) {
		this.productJsonResponse = productJsonResponse;
	}

	public void setBufferForProduct(ByteBuffer bufferForProduct) {
		this.bufferForProduct = bufferForProduct;
	}

}

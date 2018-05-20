package model;

public class Product {
	private int productId;
	private String productName, quantityPerUnit;
	private double unitPrice;

	public Product(int productId, String productName, String quantityPerUnit, double unitPrice) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.quantityPerUnit = quantityPerUnit;
		this.unitPrice = unitPrice;
	}

	public int getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public String getQuantityPerUnit() {
		return quantityPerUnit;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + productId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (productId != other.productId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", quantityPerUnit="
				+ quantityPerUnit + ", unitPrice=" + unitPrice + "]";
	}
	
	

}

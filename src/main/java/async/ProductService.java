package async;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import database.CSVReader;
import model.Product;

public class ProductService {
	private static List<Product> products;
	static {
		String csvFilePathStr="."+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"northwind"+File.separator+"products.csv";
		Path path=Paths.get(csvFilePathStr);
		try {
			products=CSVReader.read(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Product getProduct(int productID) {
		Optional<Product> productRetrieved= products.stream()
		.filter((product)->{
			if(product.getProductId() == productID) {
				return true;
			}
			else
				return false;
		})
		.findFirst();
		return productRetrieved.get();
	}
}

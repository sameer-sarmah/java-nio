package database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import model.Product;

public class CSVReader {
  
	public static List<Product> read(Path path) throws IOException {
		List<String> lines=Files.readAllLines(path);
		Function<String, Product> linetoProduct=(line)->{
			List<String> attributes= Arrays.asList(line.split(","));
			int productID=Integer.parseInt(attributes.get(0));
			double price = Double.parseDouble(attributes.get(5));
			return new Product(productID,attributes.get(1),attributes.get(4),price);
		};
		
		List<Product> products=lines.stream().filter((line)-> !line.contains("productID"))
		.map(linetoProduct)
		.collect(Collectors.toList());
		return products;
		
	}
	
}

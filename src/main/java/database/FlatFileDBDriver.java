package database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import model.Product;

public class FlatFileDBDriver {

	public static void main(String[] args) {
		String csvFilePathStr="."+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"northwind"+File.separator+"products.csv";
		String serPathStr="."+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"db"+File.separator+"products.ser";
		ProductDAO dao ;
		if(Files.exists(Paths.get(serPathStr), new LinkOption[]{ LinkOption.NOFOLLOW_LINKS})) {
			try {
				Files.delete(Paths.get(serPathStr));	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Files.createFile(Paths.get(serPathStr));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Path path=Paths.get(csvFilePathStr);
		try {
			 dao=new ProductDAO(serPathStr);
			List<Product> products=CSVReader.read(path);
			products.forEach((product)->{
				try {
					dao.appendRow(product);
				} catch (IOException e) {
					e.printStackTrace();
					dao.close();
				}
				
			});
			
			System.out.println("Number of products stored "+dao.rows());
			IntStream.range(0, dao.rows()).forEach((row)->{
				try {
					Product product=dao.readRow(row);
					System.out.println(product);
				} catch (IOException e) {
					e.printStackTrace();
					dao.close();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

	}

}

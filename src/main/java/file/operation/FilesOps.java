package file.operation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesOps {

	public static void main(String[] args) {
		Path currentDir = Paths.get(".");
		String fileName = "content.txt";
		Path filePath = Paths.get(currentDir+File.separator+fileName);
		System.out.println(currentDir);
		try {
			Files.createFile(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				Files.delete(filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

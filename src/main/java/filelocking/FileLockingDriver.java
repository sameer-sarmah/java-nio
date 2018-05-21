package filelocking;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class FileLockingDriver {

	public static void main(String[] args) {
		String csvFilePathStr="."+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"northwind"+File.separator+"products.csv";
		ExecutorService executor=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		

		executor.submit(new RegionLocking(0,200,csvFilePathStr));
		executor.submit(new RegionLocking(0,200,csvFilePathStr));
		executor.submit(new RegionLocking(250,350,csvFilePathStr));
		
		executor.submit(new WholeFileLocking(csvFilePathStr));
		
		executor.shutdown();
		
		
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}

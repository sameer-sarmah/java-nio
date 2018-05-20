package nonblocking_server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


public class ClientDriver {

	public static void main(String[] args) {
		ExecutorService executor=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		IntStream.rangeClosed(0, 2).forEach((int num)->{
			executor.submit(new BlockingClient());
		});
		//executor.submit(new NonBlockingClient());
		executor.shutdown();
		
		System.out.println("all tasks are submitted");
		
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("all tasks are completed");

	}

}

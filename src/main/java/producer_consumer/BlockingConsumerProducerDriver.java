package producer_consumer;

import java.io.IOException;
import java.nio.channels.Pipe;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BlockingConsumerProducerDriver {

	public static void main(String[] args) {
		try {
			Pipe pipe= Pipe.open();
			ExecutorService executor=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			executor.submit(new BlockingConsumer(pipe));
			executor.submit(new BlockingProducer(pipe));
			executor.shutdown();

			executor.awaitTermination(1, TimeUnit.DAYS);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

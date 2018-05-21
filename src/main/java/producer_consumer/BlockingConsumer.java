package producer_consumer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;

import org.fluttercode.datafactory.impl.DataFactory;

public class BlockingConsumer implements Runnable {
	Pipe pipe;
	
	public BlockingConsumer(Pipe pipe) {
		super();
		this.pipe = pipe;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		long meantToRunFor = 10000;
		while (System.currentTimeMillis() <= startTime + meantToRunFor) {
		DataFactory factory=new DataFactory();
		SinkChannel sink=pipe.sink();
		String city=factory.getCity();
		System.out.println("In thread "+Thread.currentThread().getName() +" sending "+city);
		ByteBuffer buffer=ByteBuffer.wrap(city.getBytes());
		try {
			sink.write(buffer);
			Thread.sleep(1000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
	}

}

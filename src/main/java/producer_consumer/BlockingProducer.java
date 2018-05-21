package producer_consumer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SourceChannel;

import org.fluttercode.datafactory.impl.DataFactory;

public class BlockingProducer implements Runnable {

	Pipe pipe;

	public BlockingProducer(Pipe pipe) {
		super();
		this.pipe = pipe;
	}

	@Override
	public void run() {
		SourceChannel source = pipe.source();
		long startTime = System.currentTimeMillis();
		long meantToRunFor = 10000;
		while (System.currentTimeMillis() <= startTime + meantToRunFor) {
			try {
				ByteBuffer buffer = ByteBuffer.allocate(50);
				byte[] bytes = new byte[buffer.remaining()];
				source.read(buffer);
				if (buffer.hasArray()) {
					bytes = buffer.array();
				} else {
					buffer.get(bytes);
				}
				String city = new String(bytes);
				System.out.println("In thread " + Thread.currentThread().getName() + " received " + city);
				Thread.sleep(1000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

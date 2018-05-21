package async;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AsyncFileReader {

	public static void main(String[] args) {
		String csvFilePathStr = "." + File.separator + "src" + File.separator + "main" + File.separator + "resources"
				+ File.separator + "northwind" + File.separator + "products.csv";
		Path path = Paths.get(csvFilePathStr);
		try {
			AsynchronousFileChannel asyncFileChannel = AsynchronousFileChannel.open(path,StandardOpenOption.READ);
			ByteBuffer buffer = ByteBuffer.allocate(10240);
			asyncFileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
				@Override
				public void completed(Integer result, ByteBuffer buffer) {
					System.out.println("Bytes read = " + result);
					byte[] bytes = new byte[buffer.remaining()];
					if (buffer.hasArray()) {
						bytes = buffer.array();
					} else {
						buffer.get(bytes);
					}
					String content = new String(bytes);
					System.out.println(content);
				}

				@Override
				public void failed(Throwable t, ByteBuffer buffer) {
					System.out.println("Failure: " + t.toString());
				}
			});
			//this sleep is to keep the main thread alive till all the processing is done
			Thread.currentThread().sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

package async.file;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AsyncFileReaderInBatches {
	public static void main(String[] args) {
		String csvFilePathStr = "." + File.separator + "src" + File.separator + "main" + File.separator + "resources"
				+ File.separator + "northwind" + File.separator + "products.csv";
		// this sleep is to keep the main thread alive till all the processing is done
		new AsyncFileReaderInBatches().readFileInBatches(csvFilePathStr);
		try {
			Thread.currentThread().sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private AsynchronousFileChannel asyncFileChannel;
	private int BATCH_SIZE;

	public void readFileInBatches(String csvFilePathStr) {
		Path path = Paths.get(csvFilePathStr);
		try {
			asyncFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
			BATCH_SIZE = 1000;
			ByteBuffer buffer = ByteBuffer.allocate(BATCH_SIZE);
			// we want to read the file content in batches
			asyncFileChannel.read(buffer, 0, buffer, new FileReaderCompletionHandler());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class FileReaderCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

		private StringBuilder totalContent = new StringBuilder("");
		private int totalBytesRead = 0;

		@Override
		public void completed(Integer bytesRead, ByteBuffer buffer) {

			if (bytesRead > 0) {
				String content = new String(buffer.array());
				totalContent.append(content);
				totalBytesRead += buffer.position();
				buffer.clear();
				asyncFileChannel.read(buffer, totalBytesRead, buffer, this);
			} else {
				System.out.println(totalContent.toString());
			}

		}

		@Override
		public void failed(Throwable t, ByteBuffer buffer) {
			System.out.println("Failure: " + t.toString());
		}
	}
}

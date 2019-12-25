package async.file;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AsyncFileReaderAtOnce {

	public static void main(String[] args) {
		String csvFilePathStr = "." + File.separator + "src" + File.separator + "main" + File.separator + "resources"
				+ File.separator + "northwind" + File.separator + "products.csv";
		Path path = Paths.get(csvFilePathStr);
		try {
			AsynchronousFileChannel asyncFileChannel = AsynchronousFileChannel.open(path,StandardOpenOption.READ);
			ByteBuffer buffer = ByteBuffer.allocate(10240);
			//we want to read the file content at once,it can be done for small files but not ideal approach for large files
			asyncFileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
				@Override
				public void completed(Integer result, ByteBuffer buffer) {
					System.out.println("Bytes read = " + result);
					System.out.println("position: "+buffer.position());
					System.out.println("remaining: "+buffer.remaining());
					/*
					 * capacity:10240
					 * bytes read:4553
					 * position:4553
					 * remaining:(10240-4553)=5687
					 * */
					String content = new String(buffer.array());
					System.out.println(content);
				}

				@Override
				public void failed(Throwable t, ByteBuffer buffer) {
					System.out.println("Failure: " + t.toString());
				}
			});
			//this sleep is to keep the main thread alive till all the processing is done
			Thread.currentThread().sleep(50000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

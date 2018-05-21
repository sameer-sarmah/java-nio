package filelocking;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class WholeFileLocking implements Runnable {
	private String filePath;

	public WholeFileLocking(String filePath) {
		super();
		this.filePath = filePath;
	}

	@Override
	public void run() {
		RandomAccessFile raf;
		FileLock lock = null;
		try {
			System.out.println("trying to acquire lock for the whole file,in thread "+Thread.currentThread().getName());
			raf = new RandomAccessFile(filePath, "rw");
			FileChannel fileChannel = raf.getChannel();
			lock = fileChannel.tryLock();
			if (lock == null) {
				System.out.println("could not acquire lock for the whole file,in thread "+Thread.currentThread().getName());
			} else {
				System.out.println("lock acquired for the whole file"+Thread.currentThread().getName());
			}
			Thread.sleep(3000);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		catch (OverlappingFileLockException e) {
			System.out.println("could not acquire lock for the whole file as it is already acquired by another thread,in thread "+Thread.currentThread().getName());
		}finally {
			if (lock != null) {
				try {
					lock.release();
					System.out.println("lock released ,"+Thread.currentThread().getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}

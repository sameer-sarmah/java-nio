package filelocking;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class RegionLocking implements Runnable {

	private int from, to;
	private String filePath;

	public RegionLocking(int from, int to, String filePath) {
		super();
		this.from = from;
		this.to = to;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		RandomAccessFile raf;
		FileLock lock = null;
		try {
			System.out.println("trying acquire lock in region "+from+" to "+to+",in thread "+Thread.currentThread().getName());
			raf = new RandomAccessFile(filePath, "rw");
			FileChannel fileChannel = raf.getChannel();
			lock = fileChannel.tryLock(from, to, true);
			if (lock == null) {
				System.out.println("could not acquire lock in region "+from+" to "+to+",in thread "+Thread.currentThread().getName());
			} else {
				System.out.println("lock acquired in region "+from+" to "+to+",in thread "+Thread.currentThread().getName());
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
			System.out.println("could not acquire lock in region "+from+" to "+to+" as it is already acquired by another thread,in thread "+Thread.currentThread().getName());
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

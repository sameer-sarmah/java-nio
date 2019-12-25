package bytebuffer;

import java.nio.ByteBuffer;

public class ByteBufferOps {

	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		buffer.put("A Java NIO Pipe is a one-way data connection between two threads".getBytes());
		/*
		 * When you write data into a buffer, the buffer keeps track of how much data you have written. 
		 * Once you need to read the data,you need to switch the buffer from writing mode 
		 * into reading mode using the flip() method call
		 * */
		//at this point buffer position 64 ,capacity 100,remaining 36 
		buffer.flip();
		//at this point buffer position 0 ,capacity 100,remaining 36
		/*
		 * Once you have read all the data, you need to clear the buffer, to make it ready for writing again. 
		 * You can do this in two ways: By calling clear() or by calling compact(). 
		 * The clear() method clears the whole buffer. 
		 * The compact() method only clears the data which you have already read. 
		 * Any unread data is moved to the beginning of the buffer, and data will now be written into 
		 * the buffer after the unread data.
		 * */
		
		printByIteration(buffer);
		//at this point buffer position 64 ,capacity 100,remaining 0
		//printMetadata(buffer);
		
		buffer.flip();
		//printMetadata(buffer);
		//at this point buffer position 0 ,capacity 100,remaining 64
		print(buffer);
		//at this point buffer position 0 ,capacity 100,remaining 64
		//printMetadata(buffer);
		buffer.put("A Java NIO Pipe is a one-way data connection between two threads".getBytes());
		buffer.flip();
		printByIteration(buffer);
		printMetadata(buffer);
		buffer.compact();//since we have read allthe contents of the buffer clear() will have same behaviour
		//at this point buffer position 0 ,capacity 100,remaining 100
		printMetadata(buffer);

		
		buffer.put("A Java NIO Pipe is a one-way data connection between two threads".getBytes());
		buffer.flip();
		int markedPosition = 10;
		buffer.position(markedPosition);
		//at this point buffer position 10 ,capacity 100,remaining 54
		buffer.mark();
		System.out.println("After marking the buffer at position "+markedPosition);
		printMetadata(buffer);
		buffer.position(20);
		//at this point buffer position 20 ,capacity 100,remaining 44
		System.out.println("After marking the buffer at position "+20);
		printMetadata(buffer);
		buffer.reset();
		//at this point buffer position 10 ,capacity 100,remaining 54
		System.out.println("After resetting the buffer to the marked position at "+markedPosition);
		printMetadata(buffer);
		
	

		
	}
	
	private static void printByIteration(ByteBuffer buffer) {
		StringBuilder str = new StringBuilder("");
		while(buffer.hasRemaining()) {
			Character ch =(char)buffer.get();
			//System.out.println("Char at position "+position+" is "+ch.toString());
			str.append(ch.toString());
		}
		System.out.println(str.toString());
	}
	
	private static void print(ByteBuffer buffer) {
		if(buffer.hasArray()) {
			System.out.println(new String(buffer.array()));
		}
		
	}
	
	private static void printMetadata(ByteBuffer buffer) {
		System.out.println("buffer limit "+buffer.limit());
		System.out.println("buffer position "+buffer.position());
		System.out.println("buffer capacity "+buffer.capacity());
		System.out.println("buffer remaining "+buffer.remaining());
	}

}

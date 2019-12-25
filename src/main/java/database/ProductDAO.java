package database;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;

import model.Product;

public class ProductDAO {
	//column sizes
	private final static int PRODUCT_ID = 10;
	private final static int PRODUCT_NAME = 50;
	private final static int QUAN_PER_UNIT = 50;
	private final static int PRICE = 10;

	//row size
	private final static int ROW_LENGTH = PRODUCT_ID + PRODUCT_NAME + QUAN_PER_UNIT + PRICE;

	private RandomAccessFile randomAccessFile;
	private FileChannel fileChannel;

	public ProductDAO(String path) throws IOException {
		randomAccessFile = new RandomAccessFile(path, "rw");
		fileChannel = randomAccessFile.getChannel();
	}

	public void appendRow(Product product) throws IOException {
		write(product, rows()*ROW_LENGTH);
	}

	public Product readRow(int row) throws IOException {
		return read(row);
	}

	public void updateRow(Product product, int row) throws IOException {
		write(product, row * ROW_LENGTH);
	}

	private void write(Product product, long startingPos) throws IOException {
		//FileLock lock = fileChannel.lock(startingPos, startingPos+ROW_LENGTH-1, true);
		FileLock lock = fileChannel.lock();
		try {
		String productID = String.valueOf(product.getProductId());
		String prductName = product.getProductName();
		String quanPerUnit = product.getQuantityPerUnit();
		String unitPrice = String.valueOf(product.getUnitPrice());
		ByteBuffer bufferForProductID = ByteBuffer.wrap(productID.getBytes(Charset.defaultCharset()));
		ByteBuffer bufferForPrductName = ByteBuffer.wrap(prductName.getBytes(Charset.defaultCharset()));
		ByteBuffer bufferForQuanPerUnit = ByteBuffer.wrap(quanPerUnit.getBytes(Charset.defaultCharset()));
		ByteBuffer bufferForUnitPrice = ByteBuffer.wrap(unitPrice.getBytes(Charset.defaultCharset()));
		fileChannel.write(bufferForProductID, startingPos);
		fileChannel.write(bufferForPrductName, startingPos + PRODUCT_ID);
		fileChannel.write(bufferForQuanPerUnit, startingPos + PRODUCT_ID + PRODUCT_NAME);
		fileChannel.write(bufferForUnitPrice, startingPos + PRODUCT_ID + PRODUCT_NAME + QUAN_PER_UNIT);
		}
		finally {
			lock.release();
		}
	}

	public int rows() throws IOException {
		double val=new Integer((int) randomAccessFile.length()).doubleValue() / ROW_LENGTH;
		double rowCount=Math.ceil(val);
		return (int)rowCount ;
	}

	private Product read(int row) throws IOException {
		if (row < 0 || row >= rows())
			throw new IllegalArgumentException("Invalid row number");
		
		long startingPos = row * ROW_LENGTH;
		ByteBuffer bufferForProductID = ByteBuffer.allocate(PRODUCT_ID);
		ByteBuffer bufferForPrductName = ByteBuffer.allocate(PRODUCT_NAME);
		ByteBuffer bufferForQuanPerUnit = ByteBuffer.allocate(QUAN_PER_UNIT);
		ByteBuffer bufferForUnitPrice = ByteBuffer.allocate(PRICE);
		fileChannel.read(bufferForProductID, startingPos);
		fileChannel.read(bufferForPrductName, startingPos + PRODUCT_ID);
		fileChannel.read(bufferForQuanPerUnit, startingPos + PRODUCT_ID + PRODUCT_NAME);
		fileChannel.read(bufferForUnitPrice, startingPos + PRODUCT_ID + PRODUCT_NAME + QUAN_PER_UNIT);
		int productID = Integer.parseInt(new String(bufferForProductID.array(), Charset.defaultCharset()).trim());
		String productName = new String(bufferForPrductName.array(), Charset.defaultCharset()).trim();
		String quanPerUnit = new String(bufferForQuanPerUnit.array(), Charset.defaultCharset()).trim();
		double unitPrice = Double.parseDouble(new String(bufferForUnitPrice.array(), Charset.defaultCharset()).trim());
		return new Product(productID, productName, quanPerUnit, unitPrice);


	}
	
	   public void close()
	   {
	      try
	      {
	         randomAccessFile.close();
	         fileChannel.close();
	      }
	      catch (IOException ioe)
	      {
	         System.err.println(ioe);
	      }
	   }
}

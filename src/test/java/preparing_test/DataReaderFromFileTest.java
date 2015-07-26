package preparing_test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Assert;
import org.junit.Test;

import preparing.DataReader;

public class DataReaderFromFileTest extends Assert{
	private String srcPath = "src\\test\\resources\\check_reading";
	
	@Test
	public void testReadLicense() throws IOException {
		String s = DataReader.readLicense(srcPath + "\\testFile.txt");
		assertEquals(s, "There is a test text here. ");
	}
	
	@Test
	public void testReadInvalid() throws IOException {
		File testFile = new File(srcPath + "\\temp.txt");
		try (PrintWriter pw = new PrintWriter(testFile)) {
			pw.print("2015$^_10");
		} catch (IOException e) {
			
		}
		
		String expected = null;
		
		String path = testFile.getAbsolutePath();
		String actual = DataReader.readLicense(path);
		
		assertEquals(expected, actual);
		
		testFile.delete();
	}
	
	@Test
	public void testReadLicenseFromUnexisting() throws IOException {
		String s = DataReader.readLicense("unexisted.txt");
		assertNull(s);
	}
	
	@Test
	public void testReadLicenseEmpty() throws IOException {
		String s = DataReader.readLicense(srcPath + "\\empty.txt");
		assertNull(s);
	}
}
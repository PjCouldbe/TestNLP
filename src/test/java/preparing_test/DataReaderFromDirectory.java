package preparing_test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import preparing.DataReader;

public class DataReaderFromDirectory extends Assert {
	private String srcPath = "src\\test\\resources\\check_reading";
	
	/*
	 * Testing of readData method according to a completely normal situation e. g.
	 * reading from folder with inner folders and non-empty .txt files
	 */
	@Test
	public void testReadData() throws IOException {
		String[] files = {srcPath + "\\innerFolder\\1.txt",
				srcPath + "\\innerFolder\\2.txt",
				srcPath + "\\testFile.txt"};
		
		String[] actual = DataReader.readAllData(files); 
		
		String[] expected = new String[3];
		expected[0] = "It's a 1.txt file. ";
		expected[1] = "It's a 2.txt file. ";
		expected[2] = "There is a test text here. ";
		
		boolean b = Arrays.deepEquals(expected, actual);
		assertTrue(b);
	}
	
	@Test
	public void testReadFromSingleFile() throws IOException {		
		String[] expected = new String[1];
		expected[0] = "There is a test text here. ";
		
		String[] paths = {srcPath + "\\testFile.txt"};
		String[] actual = DataReader.readAllData(paths); 
		
		boolean b = Arrays.deepEquals(expected, actual);
		assertTrue(b);
	}
	
	@Test
	public void testReadInvalid() throws IOException {
		File testFile = new File(srcPath + "\\temp.txt");
		try (PrintWriter pw = new PrintWriter(testFile)) {
			pw.print("2015$^_10");
		} catch (IOException e) {
			
		}
		
		String[] expected = { null };
		
		String[] paths = { testFile.getAbsolutePath() };
		String[] actual = DataReader.readAllData(paths);
		
		boolean b = Arrays.deepEquals(expected, actual);
		assertTrue(b);
		
		testFile.delete();
	}
	
	@Test
	public void testReadFromUnexisted() throws IOException {
		String[] paths = {"unexisted.txt"};
		
		String[] data = DataReader.readAllData(paths);
		
		boolean b = data.length == 1 && data[0] == null;
		assertTrue(b);
	}
	
	@Test 
	public void testReadFromEmpty() throws IOException {
		String[] paths = {srcPath + "\\empty.txt"};
		
		String[] data = DataReader.readAllData(paths);
		
		boolean b = data.length == 1 && data[0] == null;
		assertTrue(b);
	}
	
	@Test (expected = NullPointerException.class)
	public void testReadFromNull() throws IOException {
		DataReader.readAllData(null);
	}
}

package preparing_test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import preparing.PathExtractor;

@RunWith( JUnit4.class )
public class PathExtractorTest extends Assert {
	private String srcPath = "src\\test\\resources\\check_reading";
	
	@Test
	public void testGetFilePaths() throws IOException {
		File file1 = new File(srcPath + "\\innerFolder\\1.txt");
		File file2 = new File(srcPath + "\\innerFolder\\2.txt");
		File fileEmpty = new File(srcPath + "\\empty.txt");
		File fileTest = new File(srcPath + "\\testFile.txt");
		
		String[] expected = {fileEmpty.getAbsolutePath(), 
				file1.getAbsolutePath(), 
				file2.getAbsolutePath(), 
				fileTest.getAbsolutePath()};
		String[] actual = PathExtractor.getFilePaths( new File(srcPath) );
		
		boolean b = Arrays.deepEquals(expected, actual);
		assertTrue(b);
	}
	
	@Test
	public void testGetSingleFilePath() throws IOException {
		File testFile = new File(srcPath + "\\testFile.txt");
		
		String[] expected = { testFile.getAbsolutePath() };
		String[] actual = PathExtractor.getFilePaths(testFile);
		
		
		boolean b = Arrays.deepEquals(expected, actual);
		assertTrue(b);
	}
	
	@Test (expected = IOException.class)
	public void testGetFromUnexisted() throws IOException {
		File testFile = new File("\\unexisted.txt");
		PathExtractor.getFilePaths(testFile);
	}
}
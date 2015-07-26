package extracting_test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import extracting.KeyWordsExtractor;
import extracting.ModelFactory;
import extracting.Presenter;

@RunWith( JUnit4.class )
public class PresenterTest extends Assert {
	private static final String OUTPUT_DIR = "src\\test\\resources\\output"; 
	private static final String[] DATA = {"Mother washed the window.", "I love my mother!", "My dog's name is Jack."};
	
	@BeforeClass
	public static void createoutputDir() {
		File outputDir = new File(OUTPUT_DIR);
		outputDir.mkdir();
		ModelFactory.setLanguage("en");
	}
	
	@Test
	public void testPresentFile() throws IOException {
		KeyWordsExtractor extractor = new KeyWordsExtractor(DATA);
		double[][] tf_idf = extractor.extractWords();
		
		String[] keyWords = extractor.getWords();
		
		Presenter presenter = new Presenter(OUTPUT_DIR);
		String[] filenames = new String[tf_idf.length];
		final String NAME_TEMPLATE = "testOutput";
		for (int i = 0; i < filenames.length; i++) {
			filenames[i] = NAME_TEMPLATE + i + ".txt";
		}
		
		presenter.presentKeyWords(filenames, keyWords, tf_idf);
		
		File outputDir = new File(OUTPUT_DIR);
		boolean b = outputDir.exists();
		
		File[] listFiles = outputDir.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			b = b && listFiles[i].getName().equals(NAME_TEMPLATE + i + ".txt");
			listFiles[i].delete();
		}
		
		assertTrue(b);
	}
	
	@Test
	public void testPresentSingleFile() throws IOException {
		KeyWordsExtractor extractor = new KeyWordsExtractor(DATA);
		double[][] tf_idf = extractor.extractWords();
		
		String[] keyWords = extractor.getWords();
		
		Presenter presenter = new Presenter(OUTPUT_DIR);
		final String NAME_TEMPLATE = "testOutput";
		presenter.presentKeyWords(NAME_TEMPLATE + "0.txt", keyWords, tf_idf[0]);
		
		File outputDir = new File(OUTPUT_DIR);
		boolean b = outputDir.exists();
		
		File[] listFiles = outputDir.listFiles();
		b = b && listFiles.length == 1 && listFiles[0].getName().equals(NAME_TEMPLATE + "0.txt");
		
		assertTrue(b);
		listFiles[0].delete();
	}
	
	@Test
	public void testPresentFileWithNoParent() throws IOException {
		double[] tf_idf = {0.5, 0.2, 0.1};
		String[] keyWords = {"mother", "washed", "machine"};
		
		Presenter presenter = new Presenter("D:\\");
		presenter.presentKeyWords("test0.txt", keyWords, tf_idf);
		
		File outputDir = new File("D:\\");
		
		boolean b = false;
		File[] listFiles = outputDir.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			if (listFiles[i].getName().equals("test0.txt")) {
				b = true;
				listFiles[i].delete();
			}
		}
		
		assertTrue(b);
		
		File testFile = new File(outputDir + "test0.txt");
		testFile.delete();
	}
	
	@Test
	public void testPresentFileWithNulls() throws IOException {
		String[] dataWithNulls = {DATA[0], null};
		KeyWordsExtractor extractor = new KeyWordsExtractor(dataWithNulls);
		double[][] tf_idf = extractor.extractWords();
		
		String[] keyWords = extractor.getWords();
		
		Presenter presenter = new Presenter(OUTPUT_DIR);
		String[] filenames = new String[tf_idf.length];
		final String NAME_TEMPLATE = "testOutput";
		for (int i = 0; i < filenames.length; i++) {
			filenames[i] = NAME_TEMPLATE + i + ".txt";
		}
		
		presenter.presentKeyWords(filenames, keyWords, tf_idf);
		
		File outputDir = new File(OUTPUT_DIR);
		boolean b = outputDir.exists();
		
		File[] listFiles = outputDir.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			b = b && listFiles[i].getName().equals(NAME_TEMPLATE + i + ".txt");			
			listFiles[i].delete();
		}
		
		assertTrue(b);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testWithNull() throws IOException {
		Presenter pres = new Presenter(null);
	}
	
	@AfterClass
	public static void deleteOutputDir() {
		File outputDir = new File(OUTPUT_DIR);
		if (outputDir.exists()) {
			outputDir.delete();
		}
	}
}
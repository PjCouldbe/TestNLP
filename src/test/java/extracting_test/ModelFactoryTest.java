package extracting_test;

import java.io.FileNotFoundException;
import java.io.IOException;

import opennlp.tools.postag.POSModel;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import extracting.ModelFactory;

@SuppressWarnings("unused")
@RunWith( JUnit4.class )
public class ModelFactoryTest extends Assert {
	@Before
	public void setUp() {
		ModelFactory.setLanguage("en");
	}
	
	@Test
	public void testCreateSentenceModel() throws IOException {
		SentenceModel model = ModelFactory.createSentenceModel();
	}
	
	@Test
	public void testCreateTokenModel() throws IOException {
		TokenizerModel model = ModelFactory.createTokenModel();
	}
	
	@Test
	public void testCreatePOSModel() throws IOException {
		POSModel model = ModelFactory.createPOSModel();
	}
	
	@Test (expected = IOException.class)
	public void testForException() throws IOException {
		ModelFactory.setLanguage("ru");
		POSModel model = ModelFactory.createPOSModel();
		ModelFactory.setLanguage("en");
	}
}
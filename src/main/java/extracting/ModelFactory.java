package extracting;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerModel;

public class ModelFactory {
	private static final String RESOURCE_PATH = "src\\main\\resources\\";
	private static String language = "en";
	
	public static SentenceModel createSentenceModel() throws IOException {
		InputStream in = new FileInputStream(RESOURCE_PATH + language + "-sent.bin");
		SentenceModel res = new SentenceModel(in);
		in.close();
		
		return res;
	}
	
	public static TokenizerModel createTokenModel() throws IOException {
		InputStream in = new FileInputStream(RESOURCE_PATH + language + "-token.bin");
		TokenizerModel res = new TokenizerModel(in);
		in.close();
		
		return res;
	}
	
	public static POSModel createPOSModel() throws IOException {
		InputStream in = new FileInputStream(RESOURCE_PATH + language + "-pos-maxent.bin");
		POSModel res = new POSModel(in);
		in.close();
		
		return res;
	}
	
	public static String getLanguage() {
		return language;
	}
	
	public static void setLanguage(String language) {
		ModelFactory.language = language;
	}
}
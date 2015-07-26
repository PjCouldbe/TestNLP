import java.io.File;
import java.io.IOException;

import preparing.DataReader;
import preparing.PathExtractor;
import extracting.KeyWordsExtractor;
import extracting.Presenter;

public class Main {
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			throw new IllegalArgumentException(" You must specify 2 program options: the input and output directories! ");
		}
		
		File inputDir = new File(args[0]);
		String[] filePaths = PathExtractor.getFilePaths(inputDir);
		String[] data = DataReader.readAllData(filePaths);
		
		KeyWordsExtractor extractor = new KeyWordsExtractor(data);
		double[][] tf_idfs = extractor.extractWords();
		
		String outputPath = args[1];
		Presenter presenter = new Presenter(outputPath);
		presenter.presentKeyWords(filePaths, extractor.getWords(), tf_idfs);
		
		
	}
}
package extracting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import com.google.common.base.Strings;

public class Presenter {
	private String outputDirectory = null;
	
	public Presenter(String outputDirectory) throws FileNotFoundException {
		if (Strings.isNullOrEmpty(outputDirectory)) {
			throw new IllegalArgumentException(" The output directory mustn't be empty! ");
		}
		if ( !( new File(outputDirectory) ).exists() ) {
			( new File(outputDirectory) ).mkdirs();
		}
		
		this.outputDirectory = outputDirectory;
	}
	
	public File presentKeyWords(String[] filePaths, String[] keyWords, double[][] tf_idf) {
		for (int i = 0; i < tf_idf.length; i++) {
			presentKeyWords(filePaths[i], keyWords, tf_idf[i]);
		}
		
		return new File(outputDirectory);
	}
	
	public File presentKeyWords(String filePath, String[] keyWords, double[] document_tf_idf) {
		double avg = avgValue( document_tf_idf.clone() );
		
		String fileName = ( new File(filePath) ).getName();
		File res = new File(outputDirectory + "\\" + fileName);
		try {
			res.getParentFile().mkdirs();
			res.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try (PrintWriter pw = new PrintWriter(new FileWriter(res), true)) { 
			for (int i = 0; i < document_tf_idf.length; i++) {
				if (document_tf_idf[i] >= avg - 1e-4) {
					pw.println(keyWords[i]);
				}
			}
		} catch (IOException e) {
			
		}
		
		return res;
	}
	
	private double avgValue(double[] d) {
		double res = 0.0;
		double sum = 0.0;
		
		Arrays.sort(d);
		for (double val : d) {
			sum += val;
		}
		
		double localSum = 0.0;
		int i = d.length - 1;
		do {
			localSum += d[i];
			res = d[i];
			i--;
		} while (localSum < 0.2 * sum && i >= 0);
		
		return res <= 1e-4 ? 1.0 : res;    //Если res близок к 0, то принимаем avg_value за 1, 
		                                   //чтобы в пустые документы не записывались ключевые слова.
		                                   //Если оставить 0, то запишутся все выделенные слова, что неправильно
	}
	
	public String getOutputDirectory() {
		return outputDirectory;
	}
	
	public void setOutputDirectory(String outputDirectory) throws FileNotFoundException {
		if (Strings.isNullOrEmpty(outputDirectory)) {
			throw new IllegalArgumentException(" The output directory mustn't be empty! ");
		}
		if ( !( new File(outputDirectory) ).exists() ) {
			throw new FileNotFoundException();
		}
		
		this.outputDirectory = outputDirectory;
	}
}
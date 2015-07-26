package extracting_test;

import java.io.IOException;
import java.util.Arrays;

import opennlp.tools.stemmer.PorterStemmer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.ObjectArrays;

import extracting.KeyWordsExtractor;

@RunWith( JUnit4.class )
public class KeyWordsExtractorTest extends Assert {
	private static final String[] DATA = {"Mother washed the window.", "I love my mother!", "My dog's name is Jack."};
	
	@Test
	public void testExtractData() throws IOException {
		Multiset<String>[] dataSets = initializeData();
		
		Multiset<String> dfSet = dataSets[0];
		Multiset<String>[] tfSets = Arrays.copyOfRange(dataSets, 1, dataSets.length);
		double[][] expected = computeTf_idf_values(dfSet, tfSets);
		
		KeyWordsExtractor extractor = new KeyWordsExtractor(DATA);
		
		double[][] actual = extractor.extractWords();
		
		assertTrue( deepEquals(expected, actual) );
	}
	
	@Test
	public void testGettingKeyWords() throws IOException {
		KeyWordsExtractor extractor = new KeyWordsExtractor(DATA);
		extractor.extractWords();
		
		Multiset<String> dfSet = initializeData()[0];
		String[] expectedWords = new String[ dfSet.elementSet().size() ];
		expectedWords = dfSet.elementSet().toArray(expectedWords);
		
		boolean b = Arrays.deepEquals(expectedWords, extractor.getWords());
		assertTrue(b);
	}
	
	@Test
	public void testWithNulls() throws IOException {
		String[] withNullsData = {DATA[0], null, DATA[2]};
		
		Multiset<String>[] sets = ObjectArrays.newArray(Multiset.class, 3);
		
		PorterStemmer stemmer = new PorterStemmer();
		
		Multiset<String> set1 = HashMultiset.create();
			set1.add( stemmer.stem("mother") ); 
			set1.add( stemmer.stem("wash") ); 
			set1.add( stemmer.stem("window") );
			
		Multiset<String> set2 = HashMultiset.create();
			
		Multiset<String> set3 = HashMultiset.create();
			set3.add( stemmer.stem("my") ); 
			set3.add( stemmer.stem("dog") );
			set3.add( stemmer.stem("is") );
			set3.add( stemmer.stem("name") ); 
			set3.add( stemmer.stem("Jack").toLowerCase() );
			
		Multiset<String> dfSet = HashMultiset.create();
			dfSet.addAll( set1.elementSet() );  
			dfSet.addAll( set2.elementSet() ); 
			dfSet.addAll( set3.elementSet() );
			
		sets[0] = set1; 
		sets[1] = set2; 
		sets[2] = set3; 
		
		double[][] expected = computeTf_idf_values(dfSet, sets);
		
		KeyWordsExtractor extractor = new KeyWordsExtractor(withNullsData);
		double[][] actual = extractor.extractWords();
		
		assertTrue( deepEquals(expected, actual) );
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNull() {
		KeyWordsExtractor extractor = new KeyWordsExtractor(null);
	}
	
	
	/*
	 * @returns Multiset<String>[4] sets, where sets[0] - dfSet of predefined data
	 * and sets[1..3] - all tfSets, because the size of predefined data is 3;
	 */
	private Multiset<String>[] initializeData() {
		Multiset<String>[] res = ObjectArrays.newArray(Multiset.class, 4);
		
		PorterStemmer stemmer = new PorterStemmer();
		
		Multiset<String> set1 = HashMultiset.create();
			set1.add( stemmer.stem("mother") ); 
			set1.add( stemmer.stem("wash") ); 
			set1.add( stemmer.stem("window") );
		
		Multiset<String> set2 = HashMultiset.create();
			set2.add( stemmer.stem("mother") ); 
			set2.add( stemmer.stem("love") ); 
			set2.add( stemmer.stem("I").toLowerCase() ); 
			set2.add( stemmer.stem("my") );
		
		Multiset<String> set3 = HashMultiset.create();
			set3.add( stemmer.stem("my") ); 
			set3.add( stemmer.stem("dog") );
			set3.add( stemmer.stem("is") );
			set3.add( stemmer.stem("name") ); 
			set3.add( stemmer.stem("Jack").toLowerCase() );
		
		Multiset<String> dfSet = HashMultiset.create();
			dfSet.addAll( set1.elementSet() );  
			dfSet.addAll( set2.elementSet() ); 
			dfSet.addAll( set3.elementSet() );
		
		res[0] = dfSet;
		res[1] = set1;
		res[2] = set2;
		res[3] = set3;
		
		return res;
	}
	
	private double[][] computeTf_idf_values(Multiset<String> dfSet, Multiset<String> ...tfSets) {
		double[][] res = new double[ tfSets.length ][ dfSet.elementSet().size() ];
		
		String[] words = new String[ res[0].length ];   //because res[0].length = dfSet.elementSet().size(), 
		                                                //e.g. the amount of keyWords
		words = dfSet.elementSet().toArray(words);
		
		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < words.length; j++) {
				double idf = Math.log10( res.length / (double)dfSet.count(words[j]) ) / Math.log10(2.0);  //двоичный логарифм
				res[i][j] = tfSets[i].count(words[j]) * idf;
			}
		}
		
		return res;
	}
	
	private boolean deepEquals(double[][] d1, double[][] d2) {
		if (d1.length != d2.length) return false;
		
		for (int i = 0; i < d1.length; i++) {
			if (d1[i].length != d2[i].length) return false;
			
			for (int j = 0; j < d1[i].length; j++) {
				if (Math.abs( d1[i][j] - d2[i][j] ) >= 1e-4) return false;
			}
		}
		
		return true;
	}
}
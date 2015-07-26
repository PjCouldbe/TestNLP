package extracting;

import java.io.IOException;

import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.TokenizerME;

import com.google.common.base.Strings;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.ObjectArrays;

public class KeyWordsExtractor {
	private String[] data;
	private String[] extractedWords;
	
	public KeyWordsExtractor(String[] data) {   //на каждый датасет предполагается отдельный обработчик!
		if (data == null || data.length <= 0) {
			throw new IllegalArgumentException(" The dataset cannot be empty! ");
		}
		this.data = data;
	}
	
	/*
	 *    WARNING!!! 
	 * The methods inside this section are intended for processing small datasets!
	 * The methods do the whole processing into RAM and tag data only one time!
	 * 
	 * ***************************************************************************
	 */
	
	public double[][] extractWords() throws IOException {
		double[][] res = new double[data.length][];
		
		//План обработки: достаём части речи и начальные формы, 
		//оставляя только знаменательные слова, кладём в мультисет df 
		//и в мультисет tf для каждого документа, после вычисляем величину. 
		
		SentenceDetectorME sentTool = new SentenceDetectorME( ModelFactory.createSentenceModel() );
		TokenizerME tokenTool = new TokenizerME( ModelFactory.createTokenModel() );
		POSTaggerME posTool = new POSTaggerME( ModelFactory.createPOSModel() );
		PorterStemmer stemmer = new PorterStemmer();
		
		Multiset<String> documentFreqSet = HashMultiset.create();
		Multiset<String>[] termFreqSets = ObjectArrays.newArray(Multiset.class, data.length);
		for (int i = 0; i < termFreqSets.length; i++) {
			termFreqSets[i] = HashMultiset.create();
		}
		
		for (int i = 0; i < data.length; i++) {
			if ( Strings.isNullOrEmpty(data[i]) ) continue;
			
			String[] sentences = sentTool.sentDetect( data[i] );
			
			for (String sentence : sentences) {
				String[] tokenSentence = tokenTool.tokenize(sentence);
				
				String[] tags = posTool.tag(tokenSentence);
				
				for (int j = 0; j < tags.length; j++) {
					if ( isContentWord(tags[j]) ) {  
						//добавляем только для одного документа! В общую копилку (dfSet) добавляем по другому принципу.
						termFreqSets[i].add( stemmer.stem(tokenSentence[j]).toLowerCase() );  
					}
				}
			}
		}
		
		//Здесь добавляем в общий список (dfSet).
		//Проходя по каждому tfSet'у, мы добавим слово в общий список только по разу за документ
		for (Multiset<String> tfSet : termFreqSets) {   
			documentFreqSet.addAll( tfSet.elementSet() );     
		}
		
		extractedWords = documentFreqSet.elementSet().toArray( new String[ documentFreqSet.elementSet().size() ] );
		
		double[] idfs = new double[extractedWords.length];       //заранее посчитаем все idf слов          
		for (int i = 0; i < idfs.length; i++) {
			double relation = data.length / (double)documentFreqSet.count( extractedWords[i] );
			idfs[i] = Math.log10(relation) / Math.log10(2.0);    //двоичный логарифм
		}
		
		for (int i = 0; i < termFreqSets.length; i++) {       //считаем tf-idf слов для каждого документа
			res[i] = new double[extractedWords.length];
			
			for (int j = 0; j < extractedWords.length; j++) {
				res[i][j] = termFreqSets[i].count( extractedWords[j] ) * idfs[j];   //tf[j] * idf[j]
			}
		}
		
		return res;
	}
	
	/* ***************************************************************************/
	
	
	/*
	 *    WARNING!!! 
	 * There is no yet methods for effective big datasets processing!
	 * This opportunity will be added at the closest time.
	 * 
	 * ***************************************************************************
	 */
	
	
	private boolean isContentWord(String tag) {
		return tag.charAt(0) == 'N'             //if it's noun
				|| tag.charAt(0) == 'V'         //if it's verb
				|| tag.charAt(0) == 'J'         //if it's adjective
				|| tag.charAt(0) == 'R'         //if it's adverb
				|| tag.startsWith("PRP")        //if it's pronoun
				|| tag.equals("WPS");           //if it's wh-pronoun
	}
	
	public String[] getData() {
		return data;
	}
	
	public String[] getWords() {
		return extractedWords;
	}
}
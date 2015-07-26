package clustering;

import java.io.File;
import java.io.IOException;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.clusterers.EM;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ClustererService {
	private Clusterer clusterer;
	private File arffFile;
	private Instances data;
	
	public ClustererService(File arffFile) {
		clusterer = new EM();
		this.arffFile = arffFile;;
	}
	
	public ClustererService(File arffFile, Clusterer c) {
		clusterer = c;
		this.arffFile = arffFile;
	}
	
	public String clusterize(int numClusters) 
	{
		//первичная проверка
		if (!arffFile.getAbsolutePath().endsWith(".arff")) {
			System.err.println("Cannot read the file because of wrong format. Needs the arff file!");
			System.exit(1);
		}
		
		//настраиваем число кластеров
		try {
			((EM)clusterer).setNumClusters(numClusters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//кластеризация
		try {	
			data = DataSource.read(arffFile.getAbsolutePath());
			
			clusterer.buildClusterer(data);
			ClusterEvaluation evaluator = new ClusterEvaluation();
			evaluator.setClusterer(clusterer);
			evaluator.evaluateClusterer(new Instances(data));
			
			return evaluator.clusterResultsToString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int[][] getClustersContent() {
		//массив, отвечающий за количество экземпляров в каждом кластере
		int[] nums = new int[((EM)clusterer).getNumClusters()]; 
		
		//массив, отвечающий за принадлежность каждого документа определённому кластеру
		int[] classes = new int[data.size()]; 
		for (int i = 0; i < data.size(); i++) {
			try {
				int classifier = clusterer.clusterInstance(data.get(i)); //какому кластеру принадлежит
				classes[i] = classifier;  //запоминаем это здесь
				nums[classifier]++; 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int[][] res = new int[nums.length][];
		for (int i = 0; i < nums.length; i++) {
			res[i] = new int[nums[i]];
			int count = 0;  
			for (int j = 0; j < classes.length; j++) {
				if (classes[j] == i) {
					res[i][count] = j;
					count++;
				}
			}
		}
		
		return res;
	}
	
	public Clusterer getClusterer() {
		return clusterer;
	}
}
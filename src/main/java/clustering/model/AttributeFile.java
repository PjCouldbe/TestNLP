package clustering.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Collections2;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;

@SuppressWarnings("serial")
public class AttributeFile extends File{
	private final static String EXTENSION = ".arff";
	private static int serialNumber = 1;
	private PrintWriter writer;
	
	private LinkedList<clustering.model.KeyWordAttribute> attributes;
	
	private boolean attributesAreFinished;
	private boolean dataIsFinished;
	
	public AttributeFile(String location) { 
		super(location + "\\ProcessedLicenses" + serialNumber + EXTENSION);
		serialNumber++;
		
		try {
			this.createNewFile();
			writer = new PrintWriter(new FileWriter(this), true);
			
			addRelationDeclaration();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		attributes = new LinkedList<>();
		attributesAreFinished = false;
		dataIsFinished = false;
	}
	
	protected void addRelationDeclaration() {
		writer.println("@relation " + this.getName().substring(0, this.getName().length() - 5) + "\n");
	}
	
	public boolean addAtributeDeclaration(KeyWordAttribute attribute) {
		if (attributesAreFinished) {
			return false;
		}
		
		writer.println("@attribute " + attribute.getName() + " " + attribute.TYPE);
		attributes.add(attribute);
		return true;
	}
	
	public boolean fillFile(Map<String, Double[]> attributeMap) {
		if (attributeMap == null || attributeMap.size() == 0) {
			throw new IllegalArgumentException(
					"Needs the classifying attributes to make valid .arff file");
		}
		
		List<Double[]> arffData = new ArrayList<>();
		for (Map.Entry<String, Double[]> entry : attributeMap.entrySet()) {
			addAtributeDeclaration( new KeyWordAttribute( entry.getKey() ) );
			arffData.add( entry.getValue() );
		}
		setAttributesFinished();
		
		addData(arffData);
		setDataFinished();
		
		return isFinished();
	}
	
	public boolean addData(List<Double[]> data) {
		if (!attributesAreFinished | dataIsFinished) {
			return false;
		}
		
		int arrLength = data.get(0).length;
		for (int i = 0; i < arrLength; i++) {
			StringBuilder s = new StringBuilder();
			
			for (Double[] d : data) {
				s.append(d[i]);
				s.append(',');
			}
			
			if (s.charAt(s.length() - 1) == ',') {
				s.deleteCharAt(s.length() - 1);
			}
			writer.println(s);
		}
		
		return true;
	}
	
	public void setAttributesFinished() {
		attributesAreFinished = true;
		writer.println("\n@data");
	}
	
	public void setDataFinished()  {
		dataIsFinished = true;
	}
	
	/*
	 * returns   true if and only if the .arff file is finished, 
	 * e.g. all of three key sections (@relation, @attribute and @data) are present and finished
	 */
	public boolean isFinished() {
		return attributesAreFinished & dataIsFinished;
	}
}
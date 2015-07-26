package arff_test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import clustering.model.AttributeFile;

@RunWith( JUnit4.class )
public class AttributeFileTest extends Assert {
	private static final String SRC = "src\\test\\resources";
	
	@Test
	public void testFillFile() {
		StringBuilder expected = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(SRC + "\\test.arff"))) {
			while (reader.ready()) {
				expected.append(reader.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AttributeFile arffFile = new AttributeFile(SRC);
		arffFile.deleteOnExit();
		
		Map<String, Double[]> map = new LinkedHashMap<>();
		Double[] d = {0.0, 0.0, 0.0};
		map.put("Attribute1", d);
		map.put("Attribute2", d);
		map.put("Attribute3", d);
		
		arffFile.fillFile(map);
		StringBuilder actual = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(arffFile))) {
			while (reader.ready()) {
				actual.append(reader.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertEquals(expected.toString(), actual.toString());
	}
}
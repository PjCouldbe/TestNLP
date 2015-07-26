package clustering_test;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import clustering.ClustererService;

@RunWith( JUnit4.class )
public class ClustererServiceTest extends Assert {
	private static final File SRC_ARFF = new File("src\\test\\resources\\test.arff");
	
	@Test
	public void testClusterize() {
		ClustererService service = new ClustererService(SRC_ARFF);
		
		String s = service.clusterize(2);
		assertNotNull(s);
	}
	
	@Test
	public void testClusterContent() {
		ClustererService service = new ClustererService(SRC_ARFF);
		service.clusterize(2);
		
		int[][] res = service.getClustersContent();
		boolean b = res.length == 2 && res[0].length == 3 && res[1].length == 0;
		
		assertTrue(b);
	}
}
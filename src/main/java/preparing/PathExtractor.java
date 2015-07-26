package preparing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathExtractor {
	public static String[] getFilePaths(File dir) throws IOException {
		if (dir == null || !dir.exists()) {
			throw new FileNotFoundException(" The " + dir.getPath() + " resource was not found! ");
		}
		
		String[] paths;
		if (!dir.isDirectory()) {    //если это файл, а НЕ директория
			paths = new String[1];
			paths[0] = dir.getAbsolutePath();
			return paths;
		}
		
		List<String> fileNames = getFilePathsRecursive(dir, new ArrayList<String>());
		String[] res = new String[ fileNames.size() ];
		res = fileNames.toArray(res);
		
		return res;
	}
	
	private static List<String> getFilePathsRecursive(File dir, List<String> list) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				getFilePathsRecursive(f, list);
			} else {
				list.add( f.getAbsolutePath() );
			}
		}
		
		return list;
	}
}
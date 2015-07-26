package preparing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.common.base.Strings;

public class DataReader {
	/*
	 * This method reads all data from specified directory. Use this method to get all input data at once
	 * You had better to use the readLicense method through cycle if you want to handle the data simultaneously.
	 * 
	 * @returns                  data represented at the array of strings
	 * @param files              array of absolute file paths
	 */
	public static String[] readAllData(String[] filePaths) {				
		String[] res = new String[filePaths.length];
		for (int i = 0; i < filePaths.length; i++) {
			res[i] = readLicense(filePaths[i]);
		}
		
		return res;
	}
	
	public static String readLicense(String filePath) {		
		StringBuilder res = new StringBuilder();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			while (reader.ready()) {
				res.append(reader.readLine() + " ");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		if (Strings.isNullOrEmpty(res.toString()) || !res.toString().matches("^.*[A-Za-zА-Яа-я]+.*$")) {
			return null;
		}
		return res.toString();
	}
}
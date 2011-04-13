/**
 * 
 */
package ca.mrvisser.propdoc.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * @author bvisser
 *
 */
public class JavaPropertyFileTokenizer {

	private File file;
	
	public JavaPropertyFileTokenizer(File file) {
		this.file = file;
	}
	
	public TokenEnumeration tokenize() throws IOException {
		List<String> lines = new LinkedList<String>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		String line = null;
		while ((line = reader.readLine()) != null)
			lines.add(line);
		return new TokenEnumeration(lines.toArray(new String[0]));
	}
	
}

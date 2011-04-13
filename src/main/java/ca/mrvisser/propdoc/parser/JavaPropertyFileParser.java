/**
 * 
 */
package ca.mrvisser.propdoc.parser;

import java.io.BufferedInputStream;
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
public class JavaPropertyFileParser {

	private File file;
	
	public JavaPropertyFileParser(File file) {
		this.file = file;
	}
	
	public TokenEnumeration parseTokens() throws IOException {
		List<String> lines = new LinkedList<String>();
		
		InputStreamReader in = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)));
		
		char[] c = new char[1];
		int lastReadSize = in.read(c);
		
		while (lastReadSize != -1) {
			StringBuilder line = new StringBuilder();
			boolean escapeMode = false;
			while (lastReadSize != -1) {
				
				if (c[0] == '\r') {
					//just ignore these dirty things
				} else if (escapeMode) {
					//the previous character was a back-slash. consume the next character, even if it's a new-line
					line.append(c[0]);
					escapeMode = false;
				} else if (c[0] == '\\') {
					//consume the back-slash and enable escape mode for the next character
					line.append(c[0]);
					escapeMode = true;
				} else if (c[0] == '\n') {
					//unescaped new-line, thus concludes this line
					lastReadSize = in.read(c);
					break;
				} else {
					//consume everything else
					line.append(c[0]);
					escapeMode = false;
				}
				
				lastReadSize = in.read(c);
			}
			lines.add(line.toString());
		}
		
		return new TokenEnumeration(lines.toArray(new String[0]));
	}
	
}

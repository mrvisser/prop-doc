/**
 * 
 */
package ca.mrvisser.propdoc;

import java.io.File;
import java.io.IOException;

import ca.mrvisser.propdoc.parser.JavaPropertyFileParser;
import ca.mrvisser.propdoc.parser.Token;
import ca.mrvisser.propdoc.parser.TokenEnumeration;

/**
 * @author bvisser
 *
 */
public class PropDocMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Loading...");
		File file = new File("C:\\Temp\\test.properties");
		JavaPropertyFileParser parser = new JavaPropertyFileParser(file);
		TokenEnumeration tokens = parser.parseTokens();
		while (tokens.hasMoreElements()) {
			Token token = tokens.nextElement();
			
		}
		System.out.println("Done.");
	}

}

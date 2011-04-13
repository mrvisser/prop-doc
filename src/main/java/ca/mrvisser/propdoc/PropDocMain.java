/**
 * 
 */
package ca.mrvisser.propdoc;

import java.io.File;
import java.io.IOException;

import ca.mrvisser.propdoc.parser.JavaPropertyFileTokenizer;
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
		JavaPropertyFileTokenizer parser = new JavaPropertyFileTokenizer(file);
		TokenEnumeration tokens = parser.tokenize();
		while (tokens.hasMoreElements()) {
			Token token = tokens.nextElement();
			System.out.println(token.getClass().toString()+": "+token.getText());
		}
		System.out.println("Done.");
	}

}

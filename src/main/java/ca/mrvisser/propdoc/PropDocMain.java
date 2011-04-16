/**
 * 
 */
package ca.mrvisser.propdoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import ca.mrvisser.propdoc.api.PropDoc;
import ca.mrvisser.propdoc.api.PropertiesFilePropDocWriter;
import ca.mrvisser.propdoc.api.Property;
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
		File inputFile = new File("C:\\Temp\\test.properties");
		File outputFile = new File("C:\\Temp\\test-out.properties");
		PropDoc doc = new PropDoc(inputFile);
		
		OutputStream out = new FileOutputStream(outputFile);
		try {
			(new PropertiesFilePropDocWriter(100)).write(doc, out);
		} finally {
			out.close();
		}
	}
	
}

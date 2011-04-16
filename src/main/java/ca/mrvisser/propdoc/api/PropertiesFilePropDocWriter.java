/**
 * 
 */
package ca.mrvisser.propdoc.api;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

/**
 * Write a PropDoc model out to a Java Properties file. Warning: No support for Unicode.
 * 
 * @author Branden
 */
public class PropertiesFilePropDocWriter implements PropDocWriter {

	private static int DEFAULT_COMMENT_WIDTH = 100;
	private static String
		FMT_COMMENT = "# %s\n",
		FMT_COMMENT_ATTRIBUTE = "# @%s %s\n",
		FMT_COMMENT_BLOCK = "##\n",
		FMT_PROPERTY_DEF = "%s=%s\n",
		FMT_EMPTY_LINE = "\n";
	private static String CHARSET = "ISO-8859-1";
	
	private int commentWidth = DEFAULT_COMMENT_WIDTH;
	
	public PropertiesFilePropDocWriter() {
	}
	
	public PropertiesFilePropDocWriter(int commentWidth) {
		this.commentWidth = commentWidth;
	}
	
	/* (non-Javadoc)
	 * @see ca.mrvisser.propdoc.api.PropDocWriter#write(ca.mrvisser.propdoc.api.PropDoc, java.io.OutputStream)
	 */
	@Override
	public void write(PropDoc propDoc, OutputStream out) throws IOException {
		Set<String> allAttributes = propDoc.getAllAttributes();
		for (Property property : propDoc.getProperties().values()) {
			writeEmptyLine(out);
			Map<String, String> attributes = property.getMetadata();
			if (attributes != null && attributes.size() > 0) {
				writeCommentBlock(out);
				writeCommentAttribute(out, Property.ATTR_PROPERTY_KEY, property.getKey());
				writeComment(out);
				for (String name : allAttributes) {
					String val = attributes.get(name);
					if (val != null) {
						//clean the string up. We don't need no stinkin' new lines either
						val = val.replaceAll("\\s+", " ");
						String[] words = val.split("\\s");
						int
							w = 0, //index for the word array
							l = 0; //what line number we're on
							
						while (w < words.length) {
							StringBuilder currentLine = new StringBuilder(words[w]);
							int lineSize = words[w].length();
							w++; l++;
							
							//not to get technical, but there is some comment pre-amble on each line..
							if (l == 1 && !name.equals(Property.ATTR_DEFAULT_KEY)) {
								lineSize += 3 + name.length();
							} else {
								lineSize += 2;
							}
							
							while (w < words.length) {
								lineSize += words[w].length()+1;
								if (lineSize > commentWidth)
									break;
								
								currentLine.append(" "+words[w]);
								w++;
							}
							
							if (l == 1 && !name.equals(Property.ATTR_DEFAULT_KEY)) {
								writeCommentAttribute(out, name, currentLine.toString());
							} else {
								writeComment(out, currentLine.toString());
							}
						}
						
						if (name.equals(Property.ATTR_DEFAULT_KEY)) {
							writeComment(out);
						}
					}
				}
				writeCommentBlock(out);
			}
			writePropertyDef(out, property.getKey(), convertPropertyValue(property.getValue()));
		}
	}

	private String convertPropertyValue(String val) {
		return val.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t").replace("\r", "\\r")
				.replace("\f", "\\f");
	}

	private void writePropertyDef(OutputStream out, String name, String val) throws IOException {
		write(out, String.format(FMT_PROPERTY_DEF, name, val));
	}
	
	private void writeCommentAttribute(OutputStream out, String name, String content) throws IOException {
		write(out, String.format(FMT_COMMENT_ATTRIBUTE, name, content));
	}
	
	private void writeComment(OutputStream out) throws IOException {
		writeComment(out, "");
	}
	
	private void writeComment(OutputStream out, String content) throws IOException {
		write(out, String.format(FMT_COMMENT, content));
	}
	
	private void writeCommentBlock(OutputStream out) throws IOException {
		write(out, FMT_COMMENT_BLOCK);
	}
	
	private void writeEmptyLine(OutputStream out) throws IOException {
		write(out, FMT_EMPTY_LINE);
	}
	
	private void write(OutputStream out, String content) throws IOException {
		out.write(content.getBytes(CHARSET));
	}
}

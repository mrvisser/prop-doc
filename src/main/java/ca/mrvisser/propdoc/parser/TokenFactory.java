/**
 * 
 */
package ca.mrvisser.propdoc.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.mrvisser.propdoc.parser.tokens.CommentAttributeToken;
import ca.mrvisser.propdoc.parser.tokens.CommentBlockToken;
import ca.mrvisser.propdoc.parser.tokens.CommentToken;
import ca.mrvisser.propdoc.parser.tokens.EndOfFileToken;
import ca.mrvisser.propdoc.parser.tokens.PropertyDefinitionToken;

/**
 * @author bvisser
 *
 */
public class TokenFactory {
	private final static TokenFactory instance = new TokenFactory();
	
	public static TokenFactory getInstance() {
		return instance;
	}
	
	private final static Pattern
			COMMENT_BLOCK_PATTERN = Pattern.compile("^[\\s]*##([\\w\\W]*)$"),
			COMMENT_ATTRIBUTE_PATTERN = Pattern.compile("^[\\s]*#[\\s]*@([\\w]+)[\\s]+([\\w\\W]*)$"),
			COMMENT_PATTERN = Pattern.compile("^[\\s]*#([\\w\\W]*)$"),
			PROPERTY_DEFINITION_PATTERN = Pattern.compile("^[\\s]*([\\w\\.])+[\\s]*=([\\w\\W]*)$");
	
	public Token createToken(String line) {
		Matcher m = COMMENT_BLOCK_PATTERN.matcher(line);
		if (m.matches())
			return createCommentBlockToken(m);
		
		m = COMMENT_ATTRIBUTE_PATTERN.matcher(line);
		if (m.matches())
			return createCommentAttributeToken(m);
		
		m = COMMENT_PATTERN.matcher(line);
		if (m.matches())
			return createCommentToken(m);
		
		m = PROPERTY_DEFINITION_PATTERN.matcher(line);
		if (m.matches())
			return createPropertyDefinitionToken(m);
		
		return null;
	}
	
	public Token createEndOfFileToken() {
		return new EndOfFileToken();
	}
	
	private CommentBlockToken createCommentBlockToken(Matcher m) {
		return new CommentBlockToken(m.group(1));
	}
	
	private CommentAttributeToken createCommentAttributeToken(Matcher m) {
		return new CommentAttributeToken(m.group(1), m.group(2));
	}
	
	private CommentToken createCommentToken(Matcher m) {
		return new CommentToken(m.group(1));
	}
	
	private PropertyDefinitionToken createPropertyDefinitionToken(Matcher m) {
		return new PropertyDefinitionToken(m.group(1), m.group(2));
	}
}

/**
 * 
 */
package ca.mrvisser.propdoc.parser.tokens;

import ca.mrvisser.propdoc.parser.Token;

/**
 * @author bvisser
 *
 */
public class CommentToken implements Token {

	private String content;
	
	public CommentToken(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	
}

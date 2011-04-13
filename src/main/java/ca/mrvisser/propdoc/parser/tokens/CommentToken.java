/**
 * 
 */
package ca.mrvisser.propdoc.parser.tokens;

/**
 * @author bvisser
 *
 */
public class CommentToken extends BaseToken {

	private String content;
	
	public CommentToken(String text, String content) {
		super(text);
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}

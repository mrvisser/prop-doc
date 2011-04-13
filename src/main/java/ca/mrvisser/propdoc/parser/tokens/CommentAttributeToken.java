/**
 * 
 */
package ca.mrvisser.propdoc.parser.tokens;

/**
 * @author bvisser
 *
 */
public class CommentAttributeToken extends CommentToken {

	private String name;
	
	public CommentAttributeToken(String name, String content) {
		super(content);
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

/**
 * 
 */
package ca.mrvisser.propdoc.parser.tokens;

/**
 * @author bvisser
 *
 */
public class PropertyDefinitionToken extends BaseToken {
	
	private String key;
	
	public PropertyDefinitionToken(String text, String key) {
		super(text);
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}

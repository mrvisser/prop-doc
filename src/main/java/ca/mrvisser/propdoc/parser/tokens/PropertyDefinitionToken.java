/**
 * 
 */
package ca.mrvisser.propdoc.parser.tokens;

import ca.mrvisser.propdoc.parser.Token;

/**
 * @author bvisser
 *
 */
public class PropertyDefinitionToken implements Token {

	private String
		name,
		value;
	
	public PropertyDefinitionToken(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}

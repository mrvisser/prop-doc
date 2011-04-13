/**
 * 
 */
package ca.mrvisser.propdoc.parser.tokens;

import ca.mrvisser.propdoc.parser.Token;

/**
 * @author Branden
 *
 */
public class BaseToken implements Token {

	private String text;
	
	BaseToken(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}

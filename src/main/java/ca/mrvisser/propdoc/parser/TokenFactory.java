/**
 * Copyright (c) 2011, Branden Visser (mrvisser at gmail dot com)
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package ca.mrvisser.propdoc.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.mrvisser.propdoc.parser.tokens.CommentAttributeToken;
import ca.mrvisser.propdoc.parser.tokens.CommentBlockToken;
import ca.mrvisser.propdoc.parser.tokens.CommentToken;
import ca.mrvisser.propdoc.parser.tokens.EmptyLineToken;
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
			COMMENT_ATTRIBUTE_PATTERN = Pattern.compile("^[\\s]*#[\\s]*@([\\w-]+)[\\s]+([\\w\\W]*)$"),
			COMMENT_PATTERN = Pattern.compile("^[\\s]*#([\\w\\W]*)$"),
			PROPERTY_DEFINITION_PATTERN = Pattern.compile("^[\\s]*([\\w_\\.]+)=[\\w\\W]*$"),
			EMPTY_LINE_PATTERN = Pattern.compile("^[\\s]*$");
	
	public Token createToken(String line) {
		/*
		 * comment block and comment attribute are by nature "comment" patterns as well. Best to check
		 * for block and attributes first to avoid being short-circuited
		 */
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
		
		m = EMPTY_LINE_PATTERN.matcher(line);
		if (m.matches())
			return createEmptyLineToken(m);
		
		return null;
	}
	
	public Token createEndOfFileToken() {
		return new EndOfFileToken("");
	}
	
	private CommentBlockToken createCommentBlockToken(Matcher m) {
		return new CommentBlockToken(m.group(0), m.group(1));
	}
	
	private CommentAttributeToken createCommentAttributeToken(Matcher m) {
		return new CommentAttributeToken(m.group(0), m.group(1), m.group(2));
	}
	
	private CommentToken createCommentToken(Matcher m) {
		return new CommentToken(m.group(0), m.group(1));
	}
	
	private PropertyDefinitionToken createPropertyDefinitionToken(Matcher m) {
		return new PropertyDefinitionToken(m.group(0), m.group(1));
	}
	
	private EmptyLineToken createEmptyLineToken(Matcher m) {
		return new EmptyLineToken(m.group(0));
	}
}

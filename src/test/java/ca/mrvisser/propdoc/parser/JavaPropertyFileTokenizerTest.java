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

import ca.mrvisser.propdoc.junit.JUnitUtil;
import ca.mrvisser.propdoc.parser.tokens.CommentAttributeToken;
import ca.mrvisser.propdoc.parser.tokens.CommentBlockToken;


import ca.mrvisser.propdoc.parser.tokens.CommentToken;
import ca.mrvisser.propdoc.parser.tokens.EmptyLineToken;
import ca.mrvisser.propdoc.parser.tokens.EndOfFileToken;
import ca.mrvisser.propdoc.parser.tokens.PropertyDefinitionToken;
import java.io.InputStream;
import java.util.Arrays;
import org.junit.Test;

/**
 *
 * @author branden
 */
public class JavaPropertyFileTokenizerTest {
	
	private final static String FILE_TOKENIZE_SIMPLE = "tokenize-simple.properties";
	
	public JavaPropertyFileTokenizerTest() {
	}
	
	@Test
	public void testTokenizeSimple() throws Exception {
		InputStream toTest = JUnitUtil.propertyFile(FILE_TOKENIZE_SIMPLE);
		TokenEnumeration tokens = JavaPropertyFileTokenizer.tokenize(toTest);
		
		Token[] expectedTokens = new Token[] {
			new EmptyLineToken(""),
			new CommentBlockToken("##", ""),
			new CommentBlockToken("  ##", ""),
			new CommentBlockToken("\t ##", ""),
			new CommentBlockToken("##content", "content"),
			new CommentBlockToken("\t\t##content", "content"),
			new CommentBlockToken("\t  ##\tcontent", "\tcontent"),
			new EmptyLineToken(""),
			new CommentToken("#", ""),
			new CommentToken("\t#", ""),
			new CommentToken("\t#content", "content"),
			new CommentAttributeToken("#@name content", "name", "content"),
			new CommentAttributeToken(" #@name\t\tcontent", "name", "content"),
			new CommentAttributeToken("#\t\t@name content", "name", "content"),
			new CommentBlockToken("## @name content", " @name content"),
			new PropertyDefinitionToken("property.key=property.value", "property.key"),
			new PropertyDefinitionToken("property.key=property.value=property-#@value", "property.key"),
			new EndOfFileToken("")
		};
		
		JUnitUtil.assertEquals(tokens, Arrays.asList(expectedTokens));
	}
	

}

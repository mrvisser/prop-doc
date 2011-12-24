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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * @author bvisser
 *
 */
public class JavaPropertyFileTokenizer {
	
	/**
	 * Tokenize the given input stream into prop-doc tokens.
	 * 
	 * @param in A java properties file input stream
	 * @return A TokenEnumeration that may be used to iterate over prop-doc {@code Token}s
	 * @throws IOException Thrown if there is an issue reading the input stream
	 */
	public static TokenEnumeration tokenize(InputStream in) throws IOException {
		List<String> lines = new LinkedList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		try {
			String line = null;
			while ((line = reader.readLine()) != null)
				lines.add(line);
			return new TokenEnumeration(lines.toArray(new String[0]));
		} finally {
			reader.close();
		}
	}
	
}

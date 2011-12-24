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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ca.mrvisser.propdoc.parser.tokens.EndOfFileToken;

/**
 * @author bvisser
 */
public final class TokenEnumeration implements Enumeration<Token>, Iterable<Token> {

	private final TokenFactory tokenFactory = TokenFactory.getInstance();
	private int currentLineIndex = 0;
	private String[] lines = null;
	private Token nextToken = null;
	private boolean hasMoreElements = true;
	
	TokenEnumeration(String[] lines) {
		this.lines = lines;
		prepareNextElement();
	}

	/* (non-Javadoc)
	 * @see java.util.Enumeration#hasMoreElements()
	 */
	public boolean hasMoreElements() {
		return hasMoreElements;
	}

	/* (non-Javadoc)
	 * @see java.util.Enumeration#nextElement()
	 */
	public Token nextElement() {
		if (!hasMoreElements())
			throw new NoSuchElementException();
		
		Token result = nextToken;
		prepareNextElement();
		return result;
	}
	
	private void prepareNextElement() {
		if (!hasMoreElements) {
			return;
		} else if (nextToken instanceof EndOfFileToken) {
			nextToken = null;
			hasMoreElements = false;
			return;
		} else if (lines == null || currentLineIndex >= lines.length) {
			nextToken = tokenFactory.createEndOfFileToken();
			return;
		}
		
		nextToken = tokenFactory.createToken(lines[currentLineIndex++]);
		while (nextToken == null) {
			if (currentLineIndex >= lines.length) {
				nextToken = tokenFactory.createEndOfFileToken();
				return;
			}
			nextToken = tokenFactory.createToken(lines[currentLineIndex++]);
		}
	}

	@Override
	public Iterator<Token> iterator() {
		return new EnumerationIterator(this);
	}
	
	private class EnumerationIterator implements Iterator<Token> {

		private Enumeration<Token> e;
		
		public EnumerationIterator(Enumeration<Token> e) {
			this.e = e;
		}
		
		@Override
		public boolean hasNext() {
			return e.hasMoreElements();
		}

		@Override
		public Token next() {
			return e.nextElement();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
}

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
	
	@Override
	public String toString() {
		return String.format("{%s, %s}", getText(), getContent());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CommentToken other = (CommentToken) obj;
		if ((this.content == null) ? (other.content != null) : !this.content.equals(other.content)) {
			return false;
		}
		if (!super.equals(obj))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 67 * hash + (this.content != null ? this.content.hashCode() : 0);
		return hash;
	}

}

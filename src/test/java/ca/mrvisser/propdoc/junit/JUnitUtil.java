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
package ca.mrvisser.propdoc.junit;

import java.io.InputStream;
import org.junit.Assert;

import java.util.Iterator;

/**
 *
 * @author branden
 */
public class JUnitUtil {

	private final static String FILE_CLASSPATH = "ca/mrvisser/propdoc/test-resources";
	
	public static void assertEquals(Iterable<?> _ones, Iterable<?> _others)
					throws AssertionError {
		if (_ones == null && _others == null)
			return;
		if (_ones == null || _others == null)
			throw new AssertionError("One collection was null and the other wasn't.");
		
		Iterator<?> ones = _ones.iterator();
		Iterator<?> others = _others.iterator();
		
		while (true) {
			if (!ones.hasNext() && !ones.hasNext())
				return;
			if (!ones.hasNext() || !others.hasNext())
				throw new AssertionError("One collection had more elements than the other.");
			
			Object one = ones.next();
			Object other = others.next();
			
			if (one == null && other == null)
				continue;
			if (one == null || other == null)
				throw new AssertionError("One collection had a null element while the other didn't.");
			
			Assert.assertEquals(one, other);
		}
	}
	
	public static InputStream propertyFile(String fileName) {
		return Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(String.format("%s/%s", FILE_CLASSPATH, fileName));
	}
}

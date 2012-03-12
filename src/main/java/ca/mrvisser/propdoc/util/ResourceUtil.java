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
package ca.mrvisser.propdoc.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author branden
 *
 */
public final class ResourceUtil {

	public final static String CLASSPATH_PROTOCOL = "classpath";
	public final static String FILE_PROTOCOL = "file";
	public final static String CLASSPATH_PROTOCOL_PREFIX = String.format("%s:", CLASSPATH_PROTOCOL);
	public final static String FILE_PROTOCOL_PREFIX = String.format("%s:", FILE_PROTOCOL);
	
	public final static InputStream createInputStreamFromUrl(String url) throws MalformedURLException, IOException {
		if (!url.contains(":"))
			url = String.format("%s%s", FILE_PROTOCOL_PREFIX, url);
		
		if (url.startsWith(CLASSPATH_PROTOCOL_PREFIX)) {
			url = url.substring(CLASSPATH_PROTOCOL_PREFIX.length());
			return Thread.currentThread().getContextClassLoader().getResourceAsStream(url);
		} else {
			return (new URL(url)).openStream();
		}
	}
}

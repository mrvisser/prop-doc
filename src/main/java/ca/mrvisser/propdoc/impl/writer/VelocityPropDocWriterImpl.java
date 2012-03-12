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
package ca.mrvisser.propdoc.impl.writer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import ca.mrvisser.propdoc.api.PropDoc;
import ca.mrvisser.propdoc.api.PropDocWriter;
import ca.mrvisser.propdoc.api.Property;
import ca.mrvisser.propdoc.util.ResourceUtil;

/**
 * @author branden
 */
public class VelocityPropDocWriterImpl implements PropDocWriter {
	private String templateFile;
	
	public VelocityPropDocWriterImpl(String templateFile) {
		this.templateFile = templateFile;
	}
	
	@Override
	public void write(PropDoc propDoc, OutputStream out) throws IOException {
		Map<String, Object> context = buildContext(propDoc);
		VelocityEngine engine = new VelocityEngine();
		
		Reader reader = null;
		Writer writer = null;
		
		try {
			reader = createTemplateReader();
			writer = new OutputStreamWriter(out);
			engine.evaluate(new VelocityContext(context), writer, String.format("[%s]", getClass().getName()), reader);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {}
			}
			
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {}
			}
		}
	}
	
	/**
	 * A class-path resource path that identifies the velocity template to use.
	 * @return
	 */
	public String getTemplateFile() {
		return templateFile;
	}
	
	private Map<String, Object> buildContext(PropDoc propDoc) {
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("propDoc", propDoc);
		context.put("allAttributes", getAllAttributes(propDoc));
		return context;
	}

	private Iterable<String> getAllAttributes(PropDoc propDoc) {
		Set<String> allAttributes = new LinkedHashSet<String>();
		for (Property property : propDoc) {
			allAttributes.addAll(property.getMetadataKeys());
		}
		return allAttributes;
	}
	
	private Reader createTemplateReader() throws MalformedURLException, IOException {
		InputStream is = ResourceUtil.createInputStreamFromUrl(getTemplateFile());
		return new InputStreamReader(is);
	}
}

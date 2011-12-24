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
package ca.mrvisser.propdoc.api;

import static org.junit.Assert.*;

import ca.mrvisser.propdoc.junit.JUnitUtil;
import java.io.InputStream;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author branden
 */
public class PropDocTest {
	private static final String FILE_PROPDOC_TEST = "parser-test.properties";
	
	@Test
	public void testPropDoc() throws Exception {
		InputStream in = JUnitUtil.propertyFile(FILE_PROPDOC_TEST);
		PropDoc doc = new PropDoc(in);
		
		Map<String, Property> properties = doc.getProperties();
		
		Property property = properties.get("profile.form.first_name");
		assertEquals("mandatory", property.getValue());
		assertEquals("This is the first paragraph in the comment block.  it should implicitly become "
						+ "the @description attribute of  the following property.",
						property.getMetadataValue("description"));
		assertEquals("3.17.1", property.getMetadataValue("introduced"));
		assertEquals("hidden,optional,mandatory", property.getMetadataValue("values"));
		assertEquals("mandatory", property.getMetadataValue("default"));
		
		property = properties.get("profile.form.middle_name");
		assertNull(property.getValue());
		assertEquals("This is the description of  the property that spans multiple lines after  the "
						+ "annotation itself.", property.getMetadataValue("description"));
		assertEquals("3.16.0", property.getMetadataValue("deprecated"));
		assertEquals("hidden,optional,mandatory", property.getMetadataValue("values"));
		assertEquals("optional", property.getMetadataValue("default"));
	}
}

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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import ca.mrvisser.propdoc.parser.JavaPropertyFileTokenizer;
import ca.mrvisser.propdoc.parser.Token;
import ca.mrvisser.propdoc.parser.TokenEnumeration;
import ca.mrvisser.propdoc.parser.tokens.*;
import java.io.ByteArrayInputStream;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;

/**
 * A model that represents a java properties file and its associated meta-data documentation. When
 * instantiated, this object immediately parses the provided input stream as a Java Properties file.
 * When complete, the java properties, values and their associated PropDoc meta-data may be
 * accessed.
 * 
 * @author Branden
 */
public class PropDoc implements Iterable<Property> {

	private Map<String, Property> properties = new TreeMap<String, Property>();
	
	public PropDoc() {
		//empty constructor for manually building a prop-doc model
	}
	
	public PropDoc(InputStream in) throws IOException {
		byte[] fileContent = IOUtils.toByteArray(in);
		
		//first seed all the java properties and values using the java properties parser
		seedProperties(new ByteArrayInputStream(fileContent));
		
		//collect the meta-data documentation for each property
		parseMetadata(new ByteArrayInputStream(fileContent));
	}
	
	public Map<String, Property> getProperties() {
		return properties;
	}
	
	private void parseMetadata(InputStream in) throws IOException {
		TokenEnumeration tokens = JavaPropertyFileTokenizer.tokenize(in);
		//poor man's state machine..
		while (tokens.hasMoreElements()) {
			Token token = tokens.nextElement();
			
			if (token instanceof CommentBlockToken) {
				String propertyName = null;
				Map<String, String> attributeMap = new HashMap<String, String>();
				String attributeName = Property.ATTR_DEFAULT_KEY;
				StringBuilder attributeContent = new StringBuilder(" ");
				StringBuilder defaultContent = new StringBuilder(" ");
				boolean blockDone = false;
				
				while (tokens.hasMoreElements()) {
					token = tokens.nextElement();
					
					if (token instanceof CommentBlockToken) {
						if (!blockDone) {
							blockDone = true;
						} else {
							//we've already finished this comment block and hit another block. exit with what we have
							break;
						}
					} else if (token instanceof CommentAttributeToken) {
						if (!blockDone) {
							CommentAttributeToken t = (CommentAttributeToken) token;
							
							//the default attribute content doesn't get added until the end, since it could still consume more content.
							if (!Property.ATTR_DEFAULT_KEY.equals(attributeName)) {
								attributeMap.put(attributeName, attributeContent.toString().trim());
							}
							
							//set the new attribute name
							attributeName = t.getName();
							
							//if the new attribute is the default attribute, append the content to the default buffer
							if (Property.ATTR_DEFAULT_KEY.equals(attributeName)) {
								defaultContent.append(" ");
								defaultContent.append(t.getContent());
								attributeContent = new StringBuilder(" ");
							} else {
								attributeContent = new StringBuilder(" ");
								attributeContent.append(t.getContent());
							}
							
							//if the new attribute is the "property" attribute, consume it immediately and revert to the default buffer
							if (Property.ATTR_PROPERTY_KEY.equals(attributeName)) {
								propertyName = attributeContent.toString().trim();
								attributeName = Property.ATTR_DEFAULT_KEY;
								attributeContent = new StringBuilder(" ");
							}
						}
					} else if (token instanceof CommentToken) {
						if (!blockDone) {
							CommentToken t = (CommentToken) token;
							if (Property.ATTR_DEFAULT_KEY.equals(attributeName)) {
								defaultContent.append(" ");
								defaultContent.append(t.getContent());
							} else {
								attributeContent.append(" ");
								attributeContent.append(t.getContent());
							}
						}
					} else if (token instanceof PropertyDefinitionToken) {
						if (propertyName == null) {
							propertyName = ((PropertyDefinitionToken) token).getKey();
						}
						break;
					} else if (token instanceof EndOfFileToken) {
						break;
					}
				}
				
				//finally apply the default buffer, as well as the last attribute content
				attributeMap.put(Property.ATTR_DEFAULT_KEY, defaultContent.toString().trim());
				if (!Property.ATTR_DEFAULT_KEY.equals(attributeName)) {
					attributeMap.put(attributeName, attributeContent.toString().trim());
				}
				
				//apply this comment block to the property metadata
				if (propertyName != null) {
					String value = null;
					if (properties.containsKey(propertyName)) {
						value = properties.get(propertyName).getValue();
					} else {
						//in this situation, there was a documented property in the property file that doesn't
						//actually have a property-value declaration. we'll just store the property with a null
						//value.
					}

					properties.put(propertyName, new Property(propertyName, value, attributeMap));
				}
			}
		}
	}
	
	@Override
	public Iterator<Property> iterator() {
		return properties.values().iterator();
	}
	
	/**
	 * Use the Java properties parser to parse the properties file and seed all properties / values
	 * without the meta-data.
	 * 
	 * @param file The java properties file
	 * @throws IOException Thrown if there is an issue reading the properties file.
	 */
	private void seedProperties(InputStream in) throws IOException {
		Properties allProperties = new Properties();
		try {
			allProperties.load(in);
		} finally {
			in.close();
		}
		
		for (Map.Entry<Object, Object> entry : allProperties.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			Property property = new Property(key, value, null);
			properties.put(key, property);
		}
	}
}

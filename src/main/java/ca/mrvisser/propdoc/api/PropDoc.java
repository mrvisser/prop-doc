/**
 * 
 */
package ca.mrvisser.propdoc.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ca.mrvisser.propdoc.parser.JavaPropertyFileTokenizer;
import ca.mrvisser.propdoc.parser.Token;
import ca.mrvisser.propdoc.parser.TokenEnumeration;
import ca.mrvisser.propdoc.parser.tokens.*;

/**
 * @author Branden
 *
 */
public class PropDoc {

	private Map<String, Property> properties = new TreeMap<String, Property>();
	private Set<String> allAttributes = new TreeSet<String>(new Comparator<String>() {
		public int compare(String one, String other) {
			return (new Property(one, null, null)).compareTo(new Property(other, null, null));
		}
	});
	private Set<Property> droppedProperties = new TreeSet<Property>();
	
	public PropDoc(File file) throws IOException {
		seedProperties(file);
		parseMetadata(file);
	}
	
	public Map<String, Property> getProperties() {
		return properties;
	}

	public Set<String> getAllAttributes() {
		return allAttributes;
	}

	public Set<Property> getDroppedProperties() {
		return droppedProperties;
	}

	private void parseMetadata(File file) throws IOException {
		JavaPropertyFileTokenizer tokenizer = new JavaPropertyFileTokenizer(file);
		TokenEnumeration tokens = tokenizer.tokenize();
		
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
					if (properties.containsKey(propertyName)) {
						properties.get(propertyName).setMetadata(attributeMap);
						allAttributes.addAll(attributeMap.keySet());
					} else {
						/*
						 * if we've just gathered metadata for a property that *Java* did not parse from the file, then possible 2 things
						 * have gone wrong: Either there was an @property attribute that referenced a property that didn't exist, or the
						 * parser mis-parsed. For either case, we store these in a special place.
						 */
						droppedProperties.add(new Property(propertyName, null, attributeMap));
					}
				}
			}
			
		}
	}
	
	private void seedProperties(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		Properties allProperties = new Properties();
		try {
			allProperties.load(in);
		} finally {
			in.close();
		}
		
		for (Map.Entry<Object, Object> entry : allProperties.entrySet()) {
			Property property = new Property(entry.getKey().toString(), entry.getValue().toString(), new HashMap<String, String>());
			properties.put(entry.getKey().toString(), property);
		}
	}
}

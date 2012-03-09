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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class that represents a property in a Java properties file and any documentation meta-data
 * associated to it.
 * 
 * @author Branden
 */
public class Property implements Comparable<Property> {

	public static String
			//the meta key for the attribute that specifies the property
			ATTR_PROPERTY_KEY = "property",
			
			//the meta key for the "default" attribute. to be used for comment content that is not
			//associated to a property
			ATTR_DEFAULT_KEY = "description";
	
	private String key;
	private String value;
	private Map<String, String> metadata = new HashMap<String, String>();
	
	public Property(String key, String value, Map<String, String> metadata) {
		this.key = key;
		this.value = value;
		
		if (metadata == null) {
			this.metadata = new HashMap<String, String>();
		} else {
			this.metadata = metadata;
		}
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	Map<String, String> getMetadata() {
		return metadata;
	}
	
	public Set<String> getMetadataKeys() {
		return new HashSet<String>(metadata.keySet());
	}
	
	public String getMetadataValue(String key) {
		return metadata.get(key);
	}


	@Override
	public int compareTo(Property o) {
		if (o == null)
			return 1;
		if (o.getKey() == null && getKey() == null)
			return 0;
		if (o.getKey() == null && getKey() != null)
			return 1;
		if (o.getKey() != null && getKey() == null)
			return -1;
		if (equals(o))
			return 0;
		if (getKey().equals(ATTR_DEFAULT_KEY))
			return -1;
		if (o.getKey().equals(ATTR_DEFAULT_KEY))
			return 1;
		return getKey().compareTo(o.getKey());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Property other = (Property) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
}

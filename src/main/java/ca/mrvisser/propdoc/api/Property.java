/**
 * 
 */
package ca.mrvisser.propdoc.api;

import java.util.Map;

/**
 * @author Branden
 *
 */
public class Property implements Comparable<Property> {

	private String key;
	private String value;
	private Map<String, String> metadata;
	
	public Property(String key, String value, Map<String, String> metadata) {
		this.key = key;
		this.value = value;
		this.metadata = metadata;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public int compareTo(Property o) {
		if (o == null)
			return 1;
		if (o.getKey() == null && getKey() == null)
			return 0;
		if (o.getKey() == null && getKey() != null)
			return 1;
		if (o.getKey() != null && getKey() == null)
			return -1;
		return getKey().compareTo(o.getKey());
	}
	
}

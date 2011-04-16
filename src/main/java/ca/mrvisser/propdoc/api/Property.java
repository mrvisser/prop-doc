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

	public static String
			//the meta key for the attribute that specifies the property
			ATTR_PROPERTY_KEY = "property",
			
			//the meta key for the "default" attribute. to be used for comment content that is not associated to a property
			ATTR_DEFAULT_KEY = "description";
	
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

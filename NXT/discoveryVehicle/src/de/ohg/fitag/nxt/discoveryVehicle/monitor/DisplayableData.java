package de.ohg.fitag.nxt.discoveryVehicle.monitor;

/**
 * Represents a displayable data-set, which can hold a specific variable to display (for example on logs or screens),
 * which must/should be updated by value changes to work like expected.
 * @author Calvin
 */
public class DisplayableData implements Data{

	private String key;
	private String displayValue;
	private Object value;
	
	public DisplayableData(String key, String displayValue, Object value){
		this.key = key;
		this.displayValue = displayValue;
		this.value = value;
	}
	
	public DisplayableData(String key, Object value){
		this(key, value.toString(),value);
	} 
	
	@Override
	public String getKey() {
		return key;
	}
	
	@Override
	public Object getValue() {
		return value;
	}
	
	/**
	 * Gets the formatted value
	 * @return formatted value
	 */
	public String getFormattedDisplayValue() {
		return displayValue;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Sets the formatted value, must be updated separately from the value (important to work like expected!)
	 * @param formatted value
	 */
	public void setFormattedDisplayValue(String formatted) {
		this.displayValue = formatted;
	}	
	
}
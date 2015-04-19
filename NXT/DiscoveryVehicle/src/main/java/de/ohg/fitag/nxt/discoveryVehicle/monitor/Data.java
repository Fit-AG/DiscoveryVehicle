package de.ohg.fitag.nxt.discoveryVehicle.monitor;

public interface Data {
	
	public String getKey();
	
	public Object getValue();
	
	public void setValue(Object value);
	
	public void setKey(String key);

}

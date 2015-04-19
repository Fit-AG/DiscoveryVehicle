package de.ohg.fitag.nxt.discoveryVehicle.sensor;

import lejos.nxt.ADSensorPort;
import lejos.nxt.SensorConstants;
import lejos.robotics.RegulatedMotor;

/**
 * Our HydrogenDetectionSensor to detect water. It has a motor to checks mechanically depth to find water.
 * @author Calvin
 *
 */
public class HydrogenDetectionSensor implements SensorConstants{
	
	private RegulatedMotor motor;
	private ADSensorPort sensor;
	
	public HydrogenDetectionSensor(RegulatedMotor motor, ADSensorPort sensor){
		this.sensor = sensor;
	    sensor.setTypeAndMode(TYPE_SWITCH, MODE_BOOLEAN);
	}
	
	/**
	 * Checks if the sensor detected water
	 * @return boolean water detected
	 */
	public boolean isPowered(){
		return (sensor.readRawValue() < 600);  
	}
}

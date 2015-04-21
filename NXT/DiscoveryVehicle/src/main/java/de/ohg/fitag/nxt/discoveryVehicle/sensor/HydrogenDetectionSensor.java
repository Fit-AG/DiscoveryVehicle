package de.ohg.fitag.nxt.discoveryVehicle.sensor;

import de.ohg.fitag.nxt.discoveryVehicle.Configuration;
import lejos.nxt.ADSensorPort;
import lejos.nxt.Button;
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
		this.motor = motor;
		this.sensor = sensor;
	    sensor.setTypeAndMode(TYPE_SWITCH, MODE_BOOLEAN);
	}
	
	/**
	 * Checks if the sensor detected water
	 * @return boolean water detected
	 */
	public boolean isPowered(){
		return Button.ENTER.isDown();
//		return (sensor.readRawValue() < 600);  
	}
	
	/**
	 * Scan underground for water by moving sensor down
	 * @return float found water in depth relative to {@link Configuration#MAX_DEPTH_MEASURE}, -1 if no water was found, or -2 if sensor in usage
	 */
	public synchronized float scan(){
		if(motor.isMoving())
			return -2f;
		int depth = 1;
		while(!isPowered()){
			depth+=10;
			if(depth >= Configuration.HYDROGEN_MAX_DEPTH_ROTATION){
				reset();
				return -1f;
			}	
			motor.rotateTo(depth);
		}
		reset();
		return Configuration.HYDROGEN_MAX_DEPTH_MEASURE / (Configuration.HYDROGEN_MAX_DEPTH_ROTATION / depth);
	}
	
	/**
	 * Brings the sensor back to top if it is not used
	 */
	public synchronized void reset(){
		if(motor.isMoving())
			return;
		motor.rotateTo(0);
	}
}

package de.ohg.fitag.nxt.discoveryVehicle;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

public class Configuration {

	/**
	 * vehicle settings
	 */
	public static final float WHEEL_DIAMETER = 6f;
	public static final float TRACK_WIDTH = 13f;
	
	public static final NXTRegulatedMotor MOTOR_LEFT = Motor.C;
	public static final NXTRegulatedMotor MOTOR_RIGHT = Motor.B;
	
	public static final SensorPort SENSOR_OBJECT_DETECTION = SensorPort.S1;
	
	/**
	 * software settings
	 */
	public static final int SCREEN_UPDATE_DELAY = 1000; //1 second
	public static final int OBJECT_DETECTION_DISTANCE = 30;
	public static final int OBJECT_DETECTED_ROTATION = -90;
	
	public static final int COMPASS_ADJUSTMENT_TOLERANCE = 5;
	
	public static final int VEHICLE_TRAVEL_SPEED = 35;
	public static final int VEHICLE_ROTATE_SPEED = 45;
}

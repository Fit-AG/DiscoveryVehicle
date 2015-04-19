package de.ohg.fitag.nxt.discoveryVehicle.behaviors;

import de.ohg.fitag.nxt.discoveryVehicle.Configuration;
import de.ohg.fitag.nxt.discoveryVehicle.DiscoveryVehicle;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class ObjectDetection implements Behavior{

	private UltrasonicSensor us;
	
	public ObjectDetection(UltrasonicSensor us){
		this.us = us;
	}
	
	@Override
	public boolean takeControl() {
		return us.getDistance() < Configuration.OBJECT_DETECTION_DISTANCE;
	}

	@Override
	public void action() {
		DiscoveryVehicle.getMonitor().log("Object "+us.getDistance());
		DiscoveryVehicle.getPilot().setRotateSpeed(Configuration.VEHICLE_ROTATE_SPEED);
		DiscoveryVehicle.getPilot().rotate(Configuration.OBJECT_DETECTED_ROTATION);
	}

	@Override
	public void suppress() {
	}
	

}

package de.ohg.fitag.nxt.discoveryVehicle.behaviors;

import de.ohg.fitag.nxt.discoveryVehicle.Configuration;
import de.ohg.fitag.nxt.discoveryVehicle.DiscoveryVehicle;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class ObjectDetection implements Behavior{

	private UltrasonicSensor us;
	private boolean nextDirection;
	
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
		double angle = nextDirection ? Configuration.OBJECT_DETECTED_ROTATION * (-1) : Configuration.OBJECT_DETECTED_ROTATION;
		DiscoveryVehicle.getPilot().setRotateSpeed(Configuration.VEHICLE_ROTATE_SPEED);
		DiscoveryVehicle.getPilot().setTravelSpeed(Configuration.VEHICLE_TRAVEL_SPEED);
		DiscoveryVehicle.getPilot().rotate(angle);
		if(us.getDistance() > Configuration.OBJECT_DETECTION_DISTANCE)
			DiscoveryVehicle.getPilot().travel(Configuration.NAVIGATION_TRACK_SPACING);
		else
			angle = Configuration.NAVIGATION_OFFSET_ROTATION;
		DiscoveryVehicle.getPilot().rotate(angle);
		nextDirection ^= true;
	}

	@Override
	public void suppress() {
	}
	

}

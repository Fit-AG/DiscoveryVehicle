package de.ohg.fitag.nxt.discoveryVehicle.behaviors;

import de.ohg.fitag.nxt.discoveryVehicle.Configuration;
import de.ohg.fitag.nxt.discoveryVehicle.DiscoveryVehicle;
import lejos.robotics.subsumption.Behavior;

public class DriveForward implements Behavior{
	
	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		DiscoveryVehicle.getPilot().setTravelSpeed(Configuration.VEHICLE_TRAVEL_SPEED);
		DiscoveryVehicle.getPilot().forward();
	}

	@Override
	public void suppress() {
		DiscoveryVehicle.getPilot().stop();
	}

}

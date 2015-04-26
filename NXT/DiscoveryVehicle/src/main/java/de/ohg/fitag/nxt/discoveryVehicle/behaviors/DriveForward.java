package de.ohg.fitag.nxt.discoveryVehicle.behaviors;

import de.ohg.fitag.common.communication.DataMessage;
import de.ohg.fitag.nxt.discoveryVehicle.Configuration;
import de.ohg.fitag.nxt.discoveryVehicle.DiscoveryVehicle;
import de.ohg.fitag.nxt.discoveryVehicle.sensor.HydrogenDetectionSensor;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;

public class DriveForward implements Behavior{
	
	private HydrogenDetectionSensor hs;
	
	public DriveForward(HydrogenDetectionSensor hs){
		this.hs = hs;
	}
	
	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		DiscoveryVehicle.getPilot().setTravelSpeed(Configuration.VEHICLE_TRAVEL_SPEED);
		DiscoveryVehicle.getPilot().travel(Configuration.TRAVEL_DISTANCE_UNIT);
		float depth = hs.scan(true);
		if(depth != -1)
			Sound.beep();
		DiscoveryVehicle.getMonitor().log("Water detected: "+depth);
		DiscoveryVehicle.getCommunicationManager().sendMessage( DataMessage.build().append("water", depth) );
		//sensor should be reset with suppress UNTESTED
	}

	@Override
	public void suppress() {
		DiscoveryVehicle.getPilot().stop();
		hs.reset();
	}

}

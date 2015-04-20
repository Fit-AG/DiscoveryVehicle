package de.ohg.fitag.nxt.discoveryVehicle.navigation;

import de.ohg.fitag.common.communication.DataMessage;
import de.ohg.fitag.common.communication.Message;
import de.ohg.fitag.common.communication.MessageObserver;
import de.ohg.fitag.nxt.discoveryVehicle.Configuration;
import de.ohg.fitag.nxt.discoveryVehicle.DiscoveryVehicle;
import de.ohg.fitag.nxt.discoveryVehicle.monitor.DisplayableData;
import de.ohg.fitag.nxt.discoveryVehicle.monitor.ScreenMonitor;
import de.ohg.fitag.nxt.discoveryVehicle.utils.MathUtils;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * A CompassPilot built on top of our {@link de.ohg.landau.lejos.communication communication-api}. 
 * @author Calvin
 *
 */
public class CompassPilot extends DifferentialPilot {

	private Regulator regulator;
	private int direction = 0;
	
	public CompassPilot(double wheelDiameter, double trackWidth, RegulatedMotor leftMotor, RegulatedMotor rightMotor) {
		super(wheelDiameter, trackWidth, leftMotor, rightMotor);
		regulator = new Regulator(this);
		regulator.start();
		DiscoveryVehicle.getCommunicationManager().registerObserver(regulator);
	}

	public Regulator getRegulator() {
		return regulator;
	}
	
	@Override
	public void forward(){
		 regulator.requestAdjustment();
		 this.direction = 1;
		 super.forward();
	}
	
	@Override
	public void backward(){
		 regulator.requestAdjustment();
		 this.direction = -1;
		 super.forward();
	}
	
	@Override
	public void stop(){
		regulator.rejectAdjustment();
		this.direction = 0;
		super.stop();
	}
	
	@Override
	public void rotate(double angle){
		 this.direction = 0;
	     regulator.requestAdjustment((int) angle, true);
	}
	
	int getDirection() {
		return direction;
	}

	public class Regulator extends Thread implements MessageObserver {
		
		private int requested_rotation = -1;
		
		public int getRequested_rotation() {
			return requested_rotation;
		}

		private int rotation;
		private int previous_rotation;
		private boolean onPlace;
		private CompassPilot compassPilot;
		
		public Regulator(CompassPilot compassPilot){
			this.compassPilot = compassPilot;
		}
		
		@Override
		public void run(){
			while(true){
				int difference = getDifference();
				if(requested_rotation < 0)
					Thread.yield();
				
				RegulatedMotor balancer;
				RegulatedMotor imbalancer;
				if(difference < 0){
					balancer = compassPilot._left;
					imbalancer = compassPilot._right;
				}else{
					balancer = compassPilot._right;
					imbalancer = compassPilot._left;
				}
				if(Math.abs(difference) < Configuration.COMPASS_ADJUSTMENT_TOLERANCE){
					switch(compassPilot.getDirection()){
					case 1:
						balancer.forward();
						if(onPlace)
							imbalancer.forward();
						break;
					case 2:
						balancer.backward();
						if(onPlace)
							imbalancer.backward();
						break;
					default:
						balancer.stop();
						if(onPlace)
							imbalancer.stop();
					}
				}else{
					balancer.backward();
					if(onPlace)
						imbalancer.forward();
				}
			}
		}

		/**
		 * Get difference to requested adjustment
		 * @return difference (-180 up to 180) in degress
		 */
		public int getDifference(){
			return Math.round(MathUtils.calculateRotationChange(rotation, requested_rotation));
		}
		
		/**
		 * Requests an adjustment to given value
		 * @param requested_rotation Requested rotation in degrees
		 * @param onPlace boolean whether adjust on place
		 */
		public void requestAdjustment(int requested_rotation, boolean onPlace){
			DiscoveryVehicle.getMonitor().log("adjustment "+requested_rotation);
			this.requested_rotation = requested_rotation;
			this.onPlace = onPlace;
		}
		
		/**
		 * Requests an adjustment to current rotation (not on place)
		 */
		public void requestAdjustment(){
			requestAdjustment(rotation, false);
		}
		
		/**
		 * Rejects an adjustment by setting requested_rotation to -1
		 */
		public void rejectAdjustment(){
			this.requested_rotation = -1;
			this.onPlace = false;
		}
		
		/**
		 * Return rotation
		 * @return rotation
		 */
		public int getRotation(){
			return rotation;
		}
		
		/**
		 * Return previous rotation
		 * @return previous_rotation
		 */
		public int getLastRotation(){
			return previous_rotation;
		}
		
		@Override
		public void onReceive(Message message) {
			rotation = ((DataMessage)message).getInt("degree", 0);
			previous_rotation = rotation;
			
			DiscoveryVehicle.getMonitor().store(new DisplayableData("rotation", rotation));
			((ScreenMonitor)DiscoveryVehicle.getMonitor()).update();
		}
		
	}
}

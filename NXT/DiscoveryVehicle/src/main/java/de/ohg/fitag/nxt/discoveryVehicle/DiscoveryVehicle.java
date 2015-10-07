package de.ohg.fitag.nxt.discoveryVehicle;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import de.ohg.fitag.nxt.discoveryVehicle.behaviors.DriveForward;
import de.ohg.fitag.nxt.discoveryVehicle.behaviors.ObjectDetection;
import de.ohg.fitag.nxt.discoveryVehicle.monitor.Monitor;
import de.ohg.fitag.nxt.discoveryVehicle.monitor.ScreenMonitor;
import de.ohg.fitag.nxt.discoveryVehicle.navigation.CompassPilot;
import de.ohg.fitag.nxt.discoveryVehicle.sensor.HydrogenDetectionSensor;
import de.ohg.fitag.nxt.discoveryVehicle.sensor.LightSequenceDetector;
import de.ohg.fitag.common.communication.BluetoothCommunicationManager;
import de.ohg.fitag.common.communication.CommunicationManager;
import de.ohg.fitag.common.communication.DataMessage;
import de.ohg.fitag.common.communication.ErrorMessage;
import de.ohg.fitag.common.communication.Message;
import de.ohg.fitag.common.communication.MessageObserver;

/**
 * Created by Calvin on 12.03.2015.
 */
public class DiscoveryVehicle{

	private static Monitor monitor;
	private static CommunicationManager communicationManager;
	private static Arbitrator arbitrator;
	private static NXTConnection connection;
	private static DifferentialPilot pilot;

    public static void main(String[] args){
		monitor = new ScreenMonitor();
    	EmergencyShutdown emergency = new EmergencyShutdown();
    	emergency.start();
//    	monitor.log("Waiting for connection");
    	
    	
    	LightSensor sensor = new LightSensor(SensorPort.S1);
    	sensor.setFloodlight(false);
    	Button.ENTER.waitForPress();
    	sensor.calibrateHigh();
    	monitor.log("Low calibrated: " + sensor.getLow());
    	Button.ENTER.waitForPress();
    	sensor.calibrateHigh();
    	monitor.log("Hight calibrated: " + sensor.getHigh());
    	
    	LightSequenceDetector lightSequencedDetector = new LightSequenceDetector(sensor);
    	while(true){
    		boolean[] sequence= lightSequencedDetector.getSequence();
        	monitor.log("S:");
        	for(int i = 0; i < sequence.length; i++){
        		monitor.log("" + sequence[i]);
        	}
    	}
    	
//    	connection = Bluetooth.waitForConnection();
//    	monitor.log("Connected!");
//    	monitor.log("Waiting 5 seconds ...");
//    	communicationManager = new BluetoothCommunicationManager(Configuration.BLUETOOTH_COMMUNICATION_MESSAGE_SIZE, 
//    			Configuration.BLUETOOTH_COMMUNICATION_MESSAGE_CACHE, 
//    			connection.openInputStream(),connection.openOutputStream());
//    	communicationManager.registerObserver(emergency);
//    	
//    	try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//    	HydrogenDetectionSensor s = new HydrogenDetectionSensor(Configuration.HYDROGEN_SENSOR_MOTOR, Configuration.HYDROGEN_SENSOR_PORT);
//		while(true){
//			float depth = s.scan();
//			monitor.log("detected: "+depth);
//			communicationManager.sendMessage( DataMessage.build().append("water", depth));
//		}	
//    	    	pilot = new DifferentialPilot(Configuration.WHEEL_DIAMETER, Configuration.TRACK_WIDTH, Configuration.MOTOR_LEFT, Configuration.MOTOR_RIGHT);
//    	
//    	arbitrator = new Arbitrator(new Behavior[]{
//    			new DriveForward(new HydrogenDetectionSensor(Configuration.HYDROGEN_SENSOR_MOTOR, Configuration.HYDROGEN_SENSOR_PORT)),
//    			new ObjectDetection(new UltrasonicSensor(Configuration.SENSOR_OBJECT_DETECTION))
//    	});
//    	arbitrator.start();
    }
    
    public static Monitor getMonitor(){
    	return monitor;
    }
    
    public static CommunicationManager getCommunicationManager(){
    	return communicationManager;
    }

    public static DifferentialPilot getPilot(){
    	return pilot;
    }
    
    public static void shutdown(String message){
		DiscoveryVehicle.getMonitor().log(message);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
    }
    
	public static void initiateConnectionClosed() {
		if(connection != null && communicationManager != null)
			communicationManager.sendMessage(ErrorMessage.build(ErrorMessage.CONNECTION_CLOSED));
	}
}
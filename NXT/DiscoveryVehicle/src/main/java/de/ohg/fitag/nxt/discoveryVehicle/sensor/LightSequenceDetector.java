package de.ohg.fitag.nxt.discoveryVehicle.sensor;

import de.ohg.fitag.nxt.discoveryVehicle.DiscoveryVehicle;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;

public class LightSequenceDetector{

	private LightSensor lightSensor;
	
	//private boolean[] sequence;
	
	
	public LightSequenceDetector(LightSensor lightSensor){
		this.lightSensor = lightSensor;
		//this.sequence = new boolean[5];
		//start();
	}
	
	public boolean[] getSequence(int length){
		boolean[] sequence = new boolean[length];
		for(int i=0; i < length; i++){
			
			int value = this.lightSensor.getLightValue();
			DiscoveryVehicle.getMonitor().log("MEASURE" + value);
			sequence[i] = value > 40 /*? true : false*/;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return sequence;
	}
	
	//TODO add signal length and uncoracity
	public boolean checkSequence(/*boolean[] sequenceToCheck, */int timeToCheck, int pauseLength){
		boolean[] sequence = this.getSequence(timeToCheck);
			for(int i = 0; i < sequence.length; i++){
				if(sequence[i]){
					for(int j = 0; j<pauseLength; j++){
						i++;
						if(i > sequence.length) { return false; }
						if(!sequence[i]){ return false; }
					}
				if(sequence[i]){
					return true;
				}
				}
			}
		return false;
	}
//    public void run(){
//        while(true){
//        	
//        	try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//        }
//    }
    
    
	
	
	//TODO Not Working
	public int countLightPulses(int checkedLightLevel){
		
		DiscoveryVehicle.getMonitor().log("Counting:");
		
		// c is the value to log
		int c = 0;
		boolean lastRes = false;
		//test if light is already on.
		if(lightSensor.getLightValue() > checkedLightLevel){
			c++;
			lastRes = true;
		}
		DiscoveryVehicle.getMonitor().log("C: " + c);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		DiscoveryVehicle.getMonitor().log("woke up1");
		while(!Button.ENTER.isDown()){ //continue until button is pressed

			//DiscoveryVehicle.getMonitor().log("WhileLoop:");
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				DiscoveryVehicle.getMonitor().log("InterruptedException");
				e.printStackTrace();
			}
			//DiscoveryVehicle.getMonitor().log("woke up");
			boolean newRes = lightSensor.getLightValue() > checkedLightLevel;
			//DiscoveryVehicle.getMonitor().log("Result: " + newRes);
			if(newRes == !lastRes){
				//DiscoveryVehicle.getMonitor().log("New Result");
				if(lastRes == false) {
					c++;
					DiscoveryVehicle.getMonitor().log("C: " + c);
				}
				lastRes = newRes;
			}
			
			
		}
		return c;
		
	}
	
	
}

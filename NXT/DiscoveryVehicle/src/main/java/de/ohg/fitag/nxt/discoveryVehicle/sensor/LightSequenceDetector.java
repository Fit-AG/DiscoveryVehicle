package de.ohg.fitag.nxt.discoveryVehicle.sensor;

import de.ohg.fitag.nxt.discoveryVehicle.DiscoveryVehicle;
import lejos.nxt.LightSensor;

public class LightSequenceDetector{

	private LightSensor lightSensor;
	
	//private boolean[] sequence;
	
	
	public LightSequenceDetector(LightSensor lightSensor){
		this.lightSensor = lightSensor;
		//this.sequence = new boolean[5];
		//start();
	}
	
	public boolean[] getSequence(){
		boolean[] sequence = new boolean[10];
		for(int i=0; i < 4; i++){
			
			int value = this.lightSensor.getLightValue();
			DiscoveryVehicle.getMonitor().log("MEASURE" + value);
			sequence[i] = value > 40 ? true : false;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return sequence;
	}
	
	//TODO may cause crash/not all func.
	public boolean checkSequence(boolean[] sequenceToCheck, int pauseLength){
		boolean[] sequence = this.getSequence();
			for(int i = 0; i < sequence.length; i++){
				if(sequence[i]){
					for(int j = 0; j<pauseLength; j++){
						i++;
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
    
    
}

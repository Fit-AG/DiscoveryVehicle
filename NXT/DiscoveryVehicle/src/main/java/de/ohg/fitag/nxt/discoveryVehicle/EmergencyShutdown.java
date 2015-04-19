package de.ohg.fitag.nxt.discoveryVehicle;

import de.ohg.fitag.common.communication.DataMessage;
import de.ohg.fitag.common.communication.Message;
import de.ohg.fitag.common.communication.MessageObserver;
import lejos.nxt.Button;
import lejos.nxt.Sound;

public class EmergencyShutdown extends Thread implements MessageObserver{
	
	@Override
	public void run(){
		while(true){
        Button.ESCAPE.waitForPress();
        Sound.beepSequence();
        DiscoveryVehicle.initiateConnectionClosed();
        DiscoveryVehicle.shutdown("exit");
		}
	}

	@Override
	public void onReceive(Message message) {
		String error = ((DataMessage) message).getString("error", "placeholder");
		if(!error.equals("placeholder")){
	        Sound.beepSequence();
			DiscoveryVehicle.shutdown("Connection error");
		}	
	}

}

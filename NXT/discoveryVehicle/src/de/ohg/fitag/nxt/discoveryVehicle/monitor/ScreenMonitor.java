package de.ohg.fitag.nxt.discoveryVehicle.monitor;

import lejos.nxt.Battery;
import lejos.nxt.LCD;
import java.util.*;

import de.ohg.fitag.nxt.discoveryVehicle.utils.Utils;

enum ScreenLocation{
	CENTER,
	LEFT,
	RIGHT;
	
	/**
	 * Return a ScreenLocation by values from 1 to 3
	 * @param i <3
	 * @return ScreenLocation
	 */
	public static ScreenLocation get(int i){
		switch(i){
			case 3:
				return RIGHT;
			case 2:
				return CENTER;
			case 1:	
			default:
				return LEFT;
		}
	}
}

/**
 * Created by Calvin on 15.03.2015.
 *
 * A monitor that prints logs and data formatted to the screen.
 * @author Calvin
 */
public class ScreenMonitor extends Thread implements Monitor{

    private boolean battery;
    private int cache_ttl;
    private List<DisplayableData> data;

    private final String LINE_SEPERATOR = "----------------";
    private List<String> logs;

    private boolean memory;
    
    private final int TEXT_HEIGHT = 8;
    private final int TEXT_WIDTH = 16;

    /**
     * @param cache_ttl Time to life in milliseconds without automatical update,
     * does not influent by manual updates
     * @param memory true if memory should be displayed automatically
     * @param battery true if battery should be displayed automatically
     */
    public ScreenMonitor(int cache_ttl, boolean memory, boolean battery){
        this.data = new ArrayList<DisplayableData>();
        this.logs = new ArrayList<String>();
        this.cache_ttl = cache_ttl;
        this.memory = memory;
        this.battery = battery;
        start();
    }

    @Override
    public synchronized DisplayableData get(String key) {
        for(DisplayableData displayableData: data){
        	if(displayableData.getKey().equals(key))
        		return displayableData;
        }
        return null;
    }

    @Override
    public synchronized void log(String message) {
    	while(message.length() > 16){
    		logs.add(message.substring(0, 16));
    		message = message.substring(16);
    	}
        logs.add(message);
        update();
    }
    
    /**
     * Prints text on a line on a specific {@link ScreenMonitor.ScreenLocation}
     * @param text (Pay attention for the max. length of 16 chars) 
     * @param line
     * @param ScreenLocation
     */
    private void printAt(String text, int line, ScreenLocation screenLocation){
    	int start;
    	switch(screenLocation){
    		case CENTER:
    			start = (TEXT_WIDTH-text.length())/2+1;
    			break;
    		case RIGHT:
    			start = TEXT_WIDTH-text.length();
    			break;
    		case LEFT:
    		default:
    			start = 0;
    	}
    	LCD.drawString(text, start, line);
    }
    
    private synchronized boolean remove(String key){
    	for(DisplayableData dataElement : data){
    		if(key.equals(dataElement.getKey())){
    				data.remove(dataElement);
    				return true;
    		}		
    	}
		return false;
    	
    }
    
    public void run(){
        while(true){
        	if(battery)
        		updateBattery();
        	if(memory)
        		updateMemory();
        	update();
        	try {
				Thread.sleep(cache_ttl);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }

    @Override
    public void store(Data data){
    	store(new DisplayableData(data.getKey(), data.getValue()));
    }

    public synchronized void store(DisplayableData displayableData){
    	remove(displayableData.getKey());
    	
    	data.add(displayableData);
    }

    /**
     * Store data at specific index without screen update
     * @param displayableData
     * @param index index at which the specified element is to be inserted
     */
    public synchronized void store(DisplayableData displayableData, int index){
    	remove(displayableData.getKey());
    	
        data.add(index, displayableData);
    }
    
    /**
     *  Updates the display. Automatically called by using a {@link Monitor} method or scheduled regularly.
     *  LCD:
     *  16 characters width
     *   8 characters deep
     */
    public synchronized void update(){
        LCD.clear();
        int line = 0;
        int location = 0; //start by zero, becauso increased each iteration
        for(DisplayableData displayableData : data){
        		if(location == 3){
					line++;
					location = 1;
				}else
        		location++;
				printAt(displayableData.getFormattedDisplayValue(), line, ScreenLocation.get(location));		
    	}
        line++;
        LCD.drawString(LINE_SEPERATOR, 0, line);
        line++;
        int logIndex;
        int free = TEXT_HEIGHT - line;
        if(logs.size() < free)
        	logIndex = 0;
        else
        	logIndex = logs.size()-free;
        while(logIndex < logs.size() && line < TEXT_HEIGHT){
        	LCD.drawString(logs.get(logIndex), 0, line);
        	logIndex++;
        	line++;
        }
    }
    
    /**
     * Clears log cache
     */
    public synchronized void clear(){
    	logs.clear();
    }
    	
    /**
     *  Updates the battery without display. Stores battery voltage. 
     */
    private synchronized void updateBattery(){
    	double battery = Utils.round(Battery.getVoltage(),1);
    	store(new DisplayableData("battery", battery+"V", battery));
    }
    
    /**
     *  Updates the memory without display. Stores free memory in percent.
     */
    public synchronized void updateMemory(){
    	int memory = Math.round(System.getRuntime().totalMemory()/System.getRuntime().freeMemory());
    	store(new DisplayableData("memory", "M"+memory+"%", memory));
    }

}

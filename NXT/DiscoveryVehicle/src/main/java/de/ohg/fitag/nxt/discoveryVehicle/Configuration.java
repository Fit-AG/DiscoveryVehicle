package de.ohg.fitag.nxt.discoveryVehicle;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

public class Configuration {

	/**
	 * 
	 * Raumes effizient nutzt. Eine Beschreibung dieser Fahrstrategie kann hier eingesehen werden:
	 * http://saugrobot.de/saugroboter-maeander.php
	 */
	
	//-------------------------------------------------------------------------------------------
	
	/**
	 * vehicle settings
	 * Fahrzeug-Einstellungen, die der Konfiguration von Konstruktionsparametern und Ports dienen
	 */
	public static final float WHEEL_DIAMETER = 5.5f;
	public static final float TRACK_WIDTH = 11.5f;
	
	//Motoren links und rechts zur Bewegung des ganzen Roboters
	public static final NXTRegulatedMotor MOTOR_LEFT = Motor.C;
	public static final NXTRegulatedMotor MOTOR_RIGHT = Motor.B;
	
	//Port des Ultraschallsensor zur Objekterkennung
	public static final SensorPort SENSOR_OBJECT_DETECTION = SensorPort.S1;
	
	//Motor zur mechanischen Absenkung des Feuchtigkeitssensors
	public static final NXTRegulatedMotor HYDROGEN_SENSOR_MOTOR = Motor.A;
	//Port des Feuchtigkeitssensors
	public static final SensorPort HYDROGEN_SENSOR_PORT = SensorPort.S2;
	
	/**
	 * hydrogen sensor settings
	 * Einstellungen zum Feuchtigkeitssensor
	 */
	public static final float HYDROGEN_MAX_DEPTH_ROTATION = 540f;
	public static final float HYDROGEN_MAX_DEPTH_MEASURE = 4f;
	
	/**
	 * software settings
	 * Einstellungen zu technischen Hintergr�nden und Eigenschaften der Software
	 */
	//Aktualisierungsinterval des Monitors in Millisekunden (1 Sekunde = 1000 Millisekunden),
	//bei Log-Nachrichten wird Monitor unabh�ngig von diesem Wert aktualisiert
	public static final int SCREEN_MONITOR_UPDATE_DELAY = 1000;
	
	//Maximale Anzahl an Log-Nachrichten f�r den Monitor
	//geringe Zahl = geringer Speicherverbrauch
    //Beispielsweise werden mit dem Wert 25 Log-Nachrichten gesammelt und beim Erreichen der 25 werden alle Nachrichten entfernt,
    public static final int SCREEN_MONITOR_LOGS_SIZE = 25;
	//geringe Zahl = geringer Speicherverbrauch
    public static final int BLUETOOTH_COMMUNICATION_MESSAGE_SIZE = 25;
    //Minimale Anzahlan eingangenen Nachrichten f�r die Communication Schnittstelle
    public static final int BLUETOOTH_COMMUNICATION_MESSAGE_CACHE = 5;
    
    //Trennzeile des Monitors, die zwischen den Variablen und Log-Nachrichten angezeigt wird
    public static final String SCREEN_MONITOR_LINE_SEPERATOR = "------Logs------";
	
    //Legt fest, ob auf dem Monitor standardm��ig die Ladestandsanzeige angezeigt werden soll
    public static final boolean SCREEN_MONITOR_DATA_POWER = true;
    //Legt fest, ob auf dem Monitor standardm��ig die Speicherauslastung angezeigt werden soll
    public static final boolean SCREEN_MONITOR_DATA_MEMORY = true;
    
	/**
	 * navigation settings
	 * Steuerungseinstellungen f�r die Navigation und Fahrstrategie des Roboters
	 */
    //Distanz ab der ein Objekt erkannt wird
	public static final int OBJECT_DETECTION_DISTANCE = 30;
    //Drehung in Grad, wenn ein Objekt erkannt wird
	public static final int OBJECT_DETECTED_ROTATION = -90;
	
	//Noch nicht implemtiert
	public static final int COMPASS_ADJUSTMENT_TOLERANCE = 5;
	
	public static final int VEHICLE_TRAVEL_SPEED = 35;
	public static final int VEHICLE_ROTATE_SPEED = 35;
	
	//Abstand zwischen Messungen mit dem Feuchtigkeitssensor
	public static final float TRAVEL_DISTANCE_UNIT = 30f;
	
	public static final float NAVIGATION_TRACK_SPACING = TRACK_WIDTH * 1.5f;
	
	public static final float NAVIGATION_OFFSET_ROTATION = 180;
}

package de.ohg.fitag.nxt.discoveryVehicle;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

public class Configuration {

	/**
	 * Alle Messangaben müssen in derselben Einheit angegeben werden (Beispielsweise Zentimeter)
	 * 
	 * Der Roboter arbeitet mit einer Mäander-Fahrstrategie, welche unter optimalen Bedingungen etwa 95% der Fläche des
	 * Raumes effizient nutzt. Eine Beschreibung dieser Fahrstrategie kann hier eingesehen werden:
	 * http://saugrobot.de/saugroboter-maeander.php
	 */
	
	//-------------------------------------------------------------------------------------------
	
	/**
	 * vehicle settings
	 * Fahrzeug-Einstellungen, die der Konfiguration von Konstruktionsparametern und Ports dienen
	 */
	//Raddurchmesser, dient der Berechnung der Streckenlängen und Rotationen
	public static final float WHEEL_DIAMETER = 5.5f;
	//Abstand zwischen den beiden Fahrachsen (Gesamtbreite), dient der Berechnung der Streckenlängen und Rotationen
	public static final float TRACK_WIDTH = 29f;
	
	//Motoren links und rechts zur Bewegung des ganzen Roboters
	public static final NXTRegulatedMotor MOTOR_LEFT = Motor.B;
	public static final NXTRegulatedMotor MOTOR_RIGHT = Motor.C;
	
	//Port des Ultraschallsensor zur Objekterkennung
	public static final SensorPort SENSOR_OBJECT_DETECTION = SensorPort.S2;
	
	//Motor zur mechanischen Absenkung des Feuchtigkeitssensors
	public static final NXTRegulatedMotor HYDROGEN_SENSOR_MOTOR = Motor.B;
	//Port des Feuchtigkeitssensors
	public static final SensorPort HYDROGEN_SENSOR_PORT = SensorPort.S4;
	
	/**
	 * hydrogen sensor settings
	 * Einstellungen zum Feuchtigkeitssensor
	 */
	//Motorumdrehungen in Grad zur vollständigen Absenkung des Feuchtigkeitssensors (idealerweise 540°)
	public static final float HYDROGEN_MAX_DEPTH_ROTATION = 540f;
	//Maximale Eindringtiefe des Feuchtigkeitssensors in den Untergrund, dient der Berechnung der tatsächlichen Tiefe
	public static final float HYDROGEN_MAX_DEPTH_MEASURE = 20f;
	//Rotationsschrittweite (standardmäßig -10)
	public static final float HYDROGEN_MEASURE_STEP = 10;
	//Richtung (-1 für rückwärts und 1 für vorwärts)
	public static final int HYDROGEN_MOTOR_INVERT = -1;
	//Auslösewert für den Feuchtigkeitssensor
	public static final int HYDROGEN_MEASURE_TRIGGER_PRECISION = 900;
	
	/**
	 * software settings
	 * Einstellungen zu technischen Hintergründen und Eigenschaften der Software
	 */
	//Aktualisierungsinterval des Monitors in Millisekunden (1 Sekunde = 1000 Millisekunden),
	//bei Log-Nachrichten wird Monitor unabh�ngig von diesem Wert aktualisiert
	public static final int SCREEN_MONITOR_UPDATE_DELAY = 1000;
	
	//Maximale Anzahl an Log-Nachrichten für den Monitor
	//geringe Zahl = geringer Speicherverbrauch
    //Beispielsweise werden mit dem Wert 25 Log-Nachrichten gesammelt und beim Erreichen der 25 werden alle Nachrichten entfernt,
	//die nicht mehr auf dem Monitor dargestellt werden können (Der NXT bietet ein nur achtzeiliges Display)
    public static final int SCREEN_MONITOR_LOGS_SIZE = 25;
	//Maximale Anzahl an eingangenen Nachrichten für die Communication Schnittstelle
	//geringe Zahl = geringer Speicherverbrauch
    //Ermöglicht den Zugriff auf ältere Nachrichten
    //Beispielsweise werden mit dem Wert 25 Nachrichten gesammelt und beim Erreichen der 25 die ältesten Nachrichten (s.u.) entfernt. 
    public static final int BLUETOOTH_COMMUNICATION_MESSAGE_SIZE = 25;
    //Minimale Anzahlan eingangenen Nachrichten f�r die Communication Schnittstelle
    //Beispielsweise werden mit dem Wert 5 immer die letzten fünf Nachrichten zwischengespeichert
    public static final int BLUETOOTH_COMMUNICATION_MESSAGE_CACHE = 5;
    
    //Trennzeile des Monitors, die zwischen den Variablen und Log-Nachrichten angezeigt wird
    public static final String SCREEN_MONITOR_LINE_SEPERATOR = "------Logs------";
	
    //Legt fest, ob auf dem Monitor standardmäßig die Ladestandsanzeige angezeigt werden soll
    public static final boolean SCREEN_MONITOR_DATA_POWER = true;
    //Legt fest, ob auf dem Monitor standardmäßig die Speicherauslastung angezeigt werden soll
    public static final boolean SCREEN_MONITOR_DATA_MEMORY = true;
    
	/**
	 * navigation settings
	 * Steuerungseinstellungen f�r die Navigation und Fahrstrategie des Roboters
	 */
    //Distanz ab der ein Objekt erkannt wird
	public static final int OBJECT_DETECTION_DISTANCE = 30;
    //Drehung in Grad, wenn ein Objekt erkannt wird
	//Aufgrund der Mäander-Fahrstrategie wechselt dieser Wert in jeder Fahrspur automatisch von positiv auf negativ und umgekehrt
	///(Sollte aufgrund Mäander-Fahrstrategie 90° betragen)
	public static final int OBJECT_DETECTED_ROTATION = -90;
	
	//Noch nicht implemtiert
	//Legt die Toleranzgrenze für Abweichungen vom digitalen Kompass fest
	public static final int COMPASS_ADJUSTMENT_TOLERANCE = 5;
	
	//Geschwindigkeit für Fahrstrecken (zwischen 0 und 100)
	public static final int VEHICLE_TRAVEL_SPEED = 15;
	//Geschwindigkeit für Rotationen des Roboters (zwischen 0 und 100)
	public static final int VEHICLE_ROTATE_SPEED = 15;
	
	//Abstand zwischen Messungen mit dem Feuchtigkeitssensor
	public static final float TRAVEL_DISTANCE_UNIT = 30f;
	
	//Abstand zwischen einzelnen Fahrspuren (entspricht standardmäßig 1,5 der Breite des Fahrzeugs)
	public static final float NAVIGATION_TRACK_SPACING = TRACK_WIDTH * 1.25f;
	
	//Rotation bei einer Offset-Bewegung der Fahrstragie (Änderungen der Fahrspurrichtung; sollte 180° betragen)
	public static final float NAVIGATION_OFFSET_ROTATION = 180;
}

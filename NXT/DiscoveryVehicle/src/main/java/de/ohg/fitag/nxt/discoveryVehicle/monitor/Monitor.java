package de.ohg.fitag.nxt.discoveryVehicle.monitor;

/**
 * Created by Calvin on 15.03.2015.
 *
 * A basic interface for designing of monitors. A monitor can hold data and enables basic logging functionality.
 * @author Calvin
 */
public interface Monitor {

    /**
     * Log a message. Can take multiple messages with multiple arguments.
     * @param message
     */
    public void log(String message);

    /**
     * Sets or override any value, holded as {@link Data}
     * @param data
     */
    public void store(Data data);

    /**
     * Gets saved data
     * @param key Identifying string representing the data
     * @return Data found or null
     */
    public Data get(String key);

}

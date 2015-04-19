package de.ohg.fitag.common.communication;

/**
 * Created by Calvin on 04.04.2015.
 * A simple observer that will be notified when a message arrives. Can be implemented and then registered in {@link CommunicationManager}
 */
public interface MessageObserver {

    /**
     * Called from CommunicationManager when message arrives
     * @param message The message
     */
    public void onReceive(Message message);
}

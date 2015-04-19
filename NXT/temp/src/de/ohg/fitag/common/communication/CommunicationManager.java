package de.ohg.fitag.common.communication;

import java.util.List;

/**
 * Created by Calvin on 04.04.2015.
 *
 * Describes a CommunicationManager that handles I/O at same time without instant blocking.
 */
public interface CommunicationManager {

    /**
     * Get a copy of all messages received
     * @return messages
     */
    public Message[] getMessages();

    /**
     * Get last message received
     * @return last message
     */
    public Message getLastMessage();

    /**
     * Remove all received messages from cache
     */
    public void clearCache();

    /**
     * Send a message
     * @param message
     */
    public void sendMessage(Message message);

    /**
     * Register a {@link MessageObserver} that will be notified when a message arrives.
     * @param observer Observer that implements the {@link MessageObserver} interface
     */
    public void registerObserver(MessageObserver observer);

    /**
     * Return all active observers.
     * @return List<Observer>
     */
    public List<MessageObserver> getObservers();

    /**
     * Notify all observers.
     * @param message The message to notify
     */
    void notifyObservers(Message message);

}

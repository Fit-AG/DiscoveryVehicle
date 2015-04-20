package de.ohg.fitag.common.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calvin on 04.04.2015.
 */
public class BluetoothCommunicationManager implements CommunicationManager {

    private List<Message> messages;
	private int messageSize;
	private int messageCache;
    private List<MessageObserver> observers;
    private InputStream inputStream;
    private OutputStream outputStream;

    /**
     * Creates a BluetoothCommunicationManager with given Streams
     * @param inputStream
     * @param outputStream
     */
    public BluetoothCommunicationManager(InputStream inputStream, OutputStream outputStream){
        this(300, 25, inputStream, outputStream);
    }

    /**
     * Creates a BluetoothCommunicationManager
     * @param messageSize max size of messages
     * @param messageCache min size of messages to cache
     * @param inputStream
     * @param outputStream
     */
    public BluetoothCommunicationManager(int messageSize, int messageCache, InputStream inputStream, OutputStream outputStream){
        this.messageSize = messageSize;
        this.messageCache = messageCache;

        this.inputStream = inputStream;
        this.outputStream = outputStream;

        this.messages = new VisibleArrayList<Message>();
        this.observers = new ArrayList<MessageObserver>();

        InputThread inputThread = new InputThread();
        inputThread.start();
    }
	
    /**
     * Insert a message in cache (should be called from inputstream thread)
     * @param message
     */
    public void insertMessage(Message message){
		cleanUpMessageCache(1);
        messages.add(message);
        notifyObservers(message);
    }

    /**
     * Get InputStream
     * @return inputStream
     */
    public InputStream getInputStream(){
        return inputStream;
    }

    /**
     * Get OutputStream
     * @return inputStream
     */
    public OutputStream getOutputStream(){
        return outputStream;
    }

    @Override
    public Message[] getMessages() {
        return (Message[]) messages.toArray();
    }

    @Override
    public Message getLastMessage() {
        return messages.get(getMessageCount());
    }

    /**
     * Count received messages
     * @return message count
     */
    public int getMessageCount(){
        return messages.size();
    }

	/**
     * Cleanup message cache and prevent upscaling the list (possibly causes memory overflows on systems with less memory)
     * 
     * @param clearance Needed clearance
     */
    public synchronized void cleanUpMessageCache(int clearance){
    	if(messages.size() + clearance >= messageSize && messages.size() >= messageCache)
    		((VisibleArrayList<?>)messages).removeRange(1, messages.size() - (messages.size()-messageCache));
    }
	
	/**
	 * Remove all messages, can cause problems. Instead you can use {@link BluetoothCommunicationManager#cleanUpMessageCache}}
	 */
    @Override
    public void clearCache() {
        messages.clear();
    }

    @Override
    public void sendMessage(Message message) {
        byte[] buffer = message.pack();
        try {
        outputStream.write(buffer.length);
        outputStream.write(buffer);
        outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerObserver(MessageObserver observer) {
        observers.add(observer);
    }

    @Override
    public List<MessageObserver> getObservers() {
        return observers;
    }

    @Override
    public void notifyObservers(Message message) {
        for(MessageObserver observer : observers)
            observer.onReceive(message);
    }

    /**
     * Handles permanently incoming messages and inserts them to the message-cache
     */
    private class InputThread extends Thread{

        @Override
        public void run(){
            while(true){
                try {
                    int length = inputStream.read();
                    byte[] buffer = new byte[length];
                    inputStream.read(buffer);
                    Message message = DataMessage.build(new String(buffer));
                    insertMessage(message);
                } catch (Exception e) {
                    notifyObservers(ErrorMessage.build(ErrorMessage.CONNECTION_CLOSED));
                    e.printStackTrace();
                }
            }
        }

    }
}
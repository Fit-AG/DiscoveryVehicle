package de.ohg.fitag.common.communication;

import com.eclipsesource.json.JsonObject;

import java.util.List;

/**
 * Created by Calvin on 05.04.2015.
 */
public class DataMessage extends JsonObject implements Message{

    public DataMessage(){
        super();
    }

    private DataMessage(JsonObject object){
        super(object);
    }

    /**
     * Append a value with key to the message
     * @param key
     * @param value
     * @return message
     */
    public DataMessage append(String key, String value){
        add(key, value);
        return this;
    }

    /**
     * Append a value with key to the message
     * @param key
     * @param value
     * @return message
     */
    public DataMessage append(String key, int value){
        add(key, value);
        return this;
    }

    /**
     * Append a value with key to the message
     * @param key
     * @param value
     * @return message
     */
    public DataMessage append(String key, long value){
        add(key, value);
        return this;
    }

    /**
     * Append a value with key to the message
     * @param key
     * @param value
     * @return message
     */
    public DataMessage append(String key, double value){
        add(key, value);
        return this;
    }

    /**
     * Append a value with key to the message
     * @param key
     * @param value
     * @return message
     */
    public DataMessage append(String key, float value){
        add(key, value);
        return this;
    }

    /**
     * Append a value with key to the message
     * @param key
     * @param value
     * @return message
     */
    public DataMessage append(String key, boolean value){
        add(key, value);
        return this;
    }

    @Override
    public Object getContent(String key){
        return get(key);
    }

    @Override
    public List<String> getTiles(){
        return names();
    }

    @Override
    public String parse() {
        return toString();
    }

    @Override
    public byte[] pack() {
        return parse().getBytes();
    }

    /**
     * Create a new message (calls only constructor)
     * @return message
     */
    public static DataMessage build(){
        return new DataMessage();
    }

    /**
     * Create a new message (calls only constructor)
     * @return message
     */
    public static DataMessage build(String message){
        return new DataMessage(readFrom(message));
    }
}


package de.ohg.fitag.common.communication;

import java.util.List;

/**
 * Created by Calvin on 04.04.2015.
 *
 * A Message is a universal transfer object for transmitting informations and commands.
 */
public interface Message {

    /**
     * Get a content of message with specified key
     * @param key Key of content
     * @return holding content with key or null
     */
    public Object getContent(String key);

    /**
     * Get keys as list of message
     * @return tiles Tiles as ArrayList
     */
    public List<String> getTiles();

    /**
     * Parses the message to the JSON-format (or a equivalent transfer format).
     * @return The message as parsed JSON-String
     */
    public String parse();

    /**
     * Parses the message and convert it to a byte-array, so it is ready to send
     */
    public byte[] pack();
}

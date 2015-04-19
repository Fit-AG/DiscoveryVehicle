package de.ohg.fitag.common.communication;

/**
 * Created by Calvin on 06.04.2015.
 */
public class ErrorMessage extends DataMessage {

    public static final String CONNECTION_CLOSED = "connection_closed";

    /**
     * Build simple error message with reason
     * @param reason
     * @return DataMessage with predefined key "error" and reason
     */
    public static DataMessage build(String reason){
        return build().append("error",reason);
    }
}

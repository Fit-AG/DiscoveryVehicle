package de.ohg.fitag.android.discoveryVehicleRemote;

import android.graphics.Color;

/**
 * Created by Calvin on 06.04.2015.
 */
public enum ConnectionState{
    DISCONNECTED(R.string.conn_disconnected, Color.BLACK),
    PENDING(R.string.conn_pending, Color.BLACK),
    CONNECTED(R.string.conn_connected, Color.GREEN),
    FAILED(R.string.conn_failed, Color.RED),
    ABORTED(R.string.conn_aborted, Color.RED);

    private int  value;
    private int color;

    ConnectionState(int value, int color){
        this.value = value;
        this.color = color;
    }

    public int color(){
        return color;
    }

    public int value(){
        return value;
    }

}
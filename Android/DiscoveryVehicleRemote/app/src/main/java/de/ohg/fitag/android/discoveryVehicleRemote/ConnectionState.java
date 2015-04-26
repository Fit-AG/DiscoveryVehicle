package de.ohg.fitag.android.discoveryVehicleRemote;

import android.graphics.Color;

/**
 * Created by Calvin on 06.04.2015.
 */
public enum ConnectionState{
    DISCONNECTED(R.string.connDisconnected, R.color.primaryText),
    PENDING(R.string.connPending, R.color.primaryText),
    CONNECTED(R.string.connConnected, R.color.friendly),
    FAILED(R.string.connFailed, R.color.danger),
    ABORTED(R.string.connAborted, R.color.danger);

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
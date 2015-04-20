package de.ohg.fitag.nxt.discoveryVehicle.utils;

public class MathUtils {

	public static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
	
    public static float calculateRotationChange(float old_rotation, float new_rotation){
        float difference = new_rotation - old_rotation;
        if(difference > 180)
            difference =  -(360-difference);
        else if(difference < -180)
            difference = 360+difference;
        return difference;
    }
}

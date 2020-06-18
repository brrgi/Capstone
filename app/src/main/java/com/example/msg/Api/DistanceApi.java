package com.example.msg.Api;

import android.location.Location;

public class DistanceApi {
    public static int latitudeToMeter(double latitude1, double latitude2) {
        double differ = Math.abs(latitude1 - latitude2);
        int meter = (int)(differ/0.0000089525);
        return meter;
    }

    public static int longitudeToMeter(double longitude1, double longitude2) {
        double differ = Math.abs(longitude1 - longitude2);
        int meter = (int)(differ/0.000110658);
        return meter;
    }

    public static double meterToLatitude(int meter) {
        double latitude = (double)meter*0.0000089525;
        return latitude;
    }

    public static double meterToLongitude(int meter) {
        double longitude = (double)meter*0.000110658;
        return longitude;
    }

    public static double getDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        float[] result = new float[10];
        Location.distanceBetween(startLatitude, startLongitude, endLatitude,endLongitude, result);
        return result[0];
    }
    //Location.distanceBetween(37.239624, 126.965566, 37.242331,126.966220, result); //305미터/
}

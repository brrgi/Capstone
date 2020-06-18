package com.example.msg.Api;

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
}

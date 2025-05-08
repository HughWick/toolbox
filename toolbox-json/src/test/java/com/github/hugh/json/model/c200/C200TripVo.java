package com.github.hugh.json.model.c200;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class C200TripVo {

    @SerializedName("TripId")
    private String tripId;
    @SerializedName("StartTimestamp")
    private Integer startTimestamp;
    @SerializedName("StartLatitude")
    private Integer startLatitude;
    @SerializedName("StartLongitude")
    private Integer startLongitude;
    @SerializedName("FleetId")
    private String fleetId;
    @SerializedName("VehicleId")
    private String vehicleId;
    @SerializedName("DriverId")
    private String driverId;
    @SerializedName("Id")
    private String id;
    @SerializedName("MaxSpeed")
    private Integer maxSpeed;
    @SerializedName("Distance")
    private Integer distance;
    @SerializedName("Duration")
    private Integer duration;
    @SerializedName("Latitude")
    private Integer latitude;
    @SerializedName("Longitude")
    private Integer longitude;
    @SerializedName("AverageSpeed")
    private Integer averageSpeed;
    @SerializedName("Timestamp")
    private Integer timestamp;
    @SerializedName("Speed")
    private Integer speed;
    @SerializedName("RunningSeconds")
    private Integer runningSeconds;
    @SerializedName("UnauthorizedDriver")
    private Integer unauthorizedDriver;
    @SerializedName("DriverPicturePath")
    private String driverPicturePath;
    @SerializedName("ACC")
    private Integer acc;
    @SerializedName("Model")
    private String model;
    @SerializedName("Events")
    private List<EventsDTO> events;

    @NoArgsConstructor
    @Data
    public static class EventsDTO {
        @SerializedName("EventName")
        private String eventName;
        @SerializedName("Count")
        private Integer count;
    }
}
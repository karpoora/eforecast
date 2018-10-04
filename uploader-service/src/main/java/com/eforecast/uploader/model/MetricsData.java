package com.eforecast.uploader.model;

import lombok.Data;

@Data
public class MetricsData {
    private Double lowest;
    private Double highest;
    private Double lowAverage;
    private Double highAverage;
    private String lastTradePrice;
    private String lowestDate;
    private String highestDate;
    private String message;
}

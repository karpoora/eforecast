package com.eforecast.uploader.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Metrics {
    private String symbol;
    private List<HashMap<String, MetricsData>> metricsMapList;
}

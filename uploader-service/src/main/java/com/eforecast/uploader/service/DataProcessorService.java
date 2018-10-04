package com.eforecast.uploader.service;

import com.eforecast.uploader.model.IndexData;
import com.eforecast.uploader.model.MarketData;
import com.eforecast.uploader.model.Metrics;
import com.eforecast.uploader.model.MetricsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DataProcessorService {

    @Autowired
    MongoTemplate mongoTemplate;

    DateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
    List<String> daysList = Arrays.asList("7", "15", "30", "90", "180", "240");

    public List<Metrics> getMetricsData(List<String> symbols) {
        List<IndexData> indexDataList = getIndexData(symbols);
        List<Metrics> metricsList = new ArrayList<>();
        for (IndexData indexData : indexDataList) {
            List<HashMap<String, MetricsData>> metricsMapList = new ArrayList<>();
            Metrics metrics = new Metrics();
            for (String day : daysList) {
                metricsMapList.add(getMetricsList(day, indexData, metricsMapList));
            }
            metrics.setSymbol(indexData.getSymbol());
            metrics.setMetricsMapList(metricsMapList);
            metricsList.add(metrics);
        }
        return metricsList;
    }

    private List<IndexData> getIndexData(List<String> symbols) {
        List<IndexData> indexDataList = new ArrayList<>();
        for (String symbol : symbols) {
            IndexData indexData = mongoTemplate.findById(symbol, IndexData.class);
            sortMarketData(indexData.getMarketDataList());
            indexDataList.add(indexData);
        }
        return indexDataList;
    }

    private void sortMarketData(List<MarketData> marketDataList) {
        marketDataList.sort(new Comparator<MarketData>() {
            @Override
            public int compare(MarketData o1, MarketData o2) {
                return o2.getOnDate().compareTo(o1.getOnDate());
            }
        });
    }

    private HashMap<String, MetricsData> getMetricsList(String days, IndexData indexData, List<HashMap<String, MetricsData>> metricsMapList) {
        Double highAverage = 0.0;
        Double lowAverage = 0.0;
        Double highest = 0.0;
        Double lowest = -1.0;
        HashMap<String, MetricsData> metricsMap = new HashMap<>();
        MetricsData metricsData = new MetricsData();
        List<MarketData> expectedMarketDataList = new ArrayList<>();
        try {
            expectedMarketDataList = indexData.getMarketDataList().subList(0, new Integer(days));
        } catch (Exception e) {
            metricsData.setMessage("No Data in Selected Range of Days");
            metricsMap.put(days, metricsData);
                /*metrics.setSymbol(indexData.getSymbol());
                metrics.setMetricsMapList(metricsMapList);
                metricsList.add(metrics);*/
            return metricsMap;
        }

        for (MarketData marketData : expectedMarketDataList) {
            highAverage = highAverage + Double.parseDouble(marketData.getHighPrice());
            lowAverage = lowAverage + Double.parseDouble(marketData.getLowPrice());
            if (highest < Double.parseDouble(marketData.getHighPrice())) {
                highest = Double.parseDouble(marketData.getHighPrice());
                metricsData.setHighestDate(format.format(marketData.getOnDate()));
            }
            if (lowest == -1 || lowest > Double.parseDouble(marketData.getHighPrice())) {
                lowest = Double.parseDouble(marketData.getLowPrice());
                metricsData.setLowestDate(format.format(marketData.getOnDate()));
            }
        }
        metricsData.setLastTradePrice(expectedMarketDataList.get(0).getClosePrice());
        metricsData.setHighAverage(highAverage / Double.parseDouble(days));
        metricsData.setLowAverage(lowAverage / Double.parseDouble(days));
        metricsData.setLowest(lowest);
        metricsData.setHighest(highest);
        metricsData.setMessage("Data Metrics Success");
        metricsMap.put(days, metricsData);
        return metricsMap;
    }
}

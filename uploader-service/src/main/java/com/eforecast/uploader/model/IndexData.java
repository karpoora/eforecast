package com.eforecast.uploader.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.List;

@Document(collection = "index_data")
@Data
public class IndexData{

    @Id
    private String symbol;
    private List<MarketData> marketDataList;

}

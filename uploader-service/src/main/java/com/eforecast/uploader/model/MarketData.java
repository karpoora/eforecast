package com.eforecast.uploader.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.Date;


@Data
public class MarketData{
    private String symbol;
    private String series;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String closePrice;
    private String lastPrice;
    private String prevClose;
    private String totalTrdQty;
    private String totalTrdValue;
    private Date onDate;
    private String totalTrades;
    private String isin;

}

package com.eforecast.uploader.util;

import com.eforecast.uploader.model.IndexData;
import com.eforecast.uploader.model.MarketData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexDataMapperUtil {

    DateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
    public List<IndexData> getIndexDataFromFile(File file) throws IOException, ParseException {
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        CSVParser parser = new CSVParser(new FileReader(file), format);
        List<IndexData> indexDataList = new ArrayList<>();
        for (CSVRecord record : parser) {
            IndexData indexData =
                    marketDataMapper(record);
            indexDataList.add(indexData);
        }
        parser.close();
        return indexDataList;
    }

    public IndexData marketDataMapper(CSVRecord record) throws ParseException {

        MarketData marketData = new MarketData();
        marketData.setSymbol(record.get("SYMBOL"));
        marketData.setSeries(record.get("SERIES"));
        marketData.setOpenPrice(record.get("OPEN"));
        marketData.setHighPrice(record.get("HIGH"));
        marketData.setLowPrice(record.get("LOW"));
        marketData.setClosePrice(record.get("CLOSE"));
        marketData.setLastPrice(record.get("LAST"));
        marketData.setPrevClose(record.get("PREVCLOSE"));
        marketData.setTotalTrdQty(record.get("TOTTRDQTY"));
        marketData.setTotalTrdValue(record.get("TOTTRDVAL"));
        marketData.setOnDate(format.parse(record.get("TIMESTAMP")));
        marketData.setTotalTrades(record.get("TOTALTRADES"));
        marketData.setIsin(record.get("ISIN"));
        IndexData indexData = new IndexData();

        List<MarketData> marketDataList = new ArrayList<>();
        marketDataList.add(marketData);

        indexData.setMarketDataList(marketDataList);
        indexData.setSymbol(record.get("SYMBOL"));
        return indexData;
    }
}
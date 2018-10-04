package com.eforecast.uploader.controller;

import com.eforecast.uploader.model.IndexData;
import com.eforecast.uploader.model.MarketData;
import com.eforecast.uploader.model.Metrics;
import com.eforecast.uploader.service.DataProcessorService;
import com.eforecast.uploader.service.UploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UploadController {
    @Autowired
    UploaderService uploaderService;

    @Autowired
    DataProcessorService dataProcessorService;

    @GetMapping("/upload")
    public String fileUpload() throws IOException {
        uploaderService.uploadFiles();
        return "Success";
    }

    @PostMapping("/market-metrics")
    public List<Metrics> getMetricsData(@RequestBody List<String> symbols){
        return dataProcessorService.getMetricsData(symbols);
    }
}

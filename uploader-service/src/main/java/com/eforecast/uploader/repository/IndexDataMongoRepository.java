package com.eforecast.uploader.repository;

import com.eforecast.uploader.model.IndexData;
import com.eforecast.uploader.model.MarketData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface IndexDataMongoRepository extends MongoRepository<IndexData, String> {
}

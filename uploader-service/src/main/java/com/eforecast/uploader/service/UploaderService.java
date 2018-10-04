package com.eforecast.uploader.service;

import com.eforecast.uploader.model.IndexData;
import com.eforecast.uploader.repository.IndexDataMongoRepository;
import com.eforecast.uploader.util.IndexDataMapperUtil;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

@Service
public class UploaderService {

    @Value("${market.raw-data}")
    Resource rawData;

    @Value("${market.processed-data}")
    Resource processedData;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    IndexDataMongoRepository indexDataMongoRepository;


    public String uploadFiles() throws IOException {
        Files.walk(Paths.get(rawData.getURI()))
                .parallel()
                .filter(path -> path.getFileName().toString().endsWith(".csv"))
                .forEach(path -> {
                    try {
                        mongoDbProcessFile(path.toFile());
                        storeFilesToBackup(path.toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });
        return "success";
    }

    private void storeFilesToBackup(File file) throws IOException {
        file.renameTo(new File(processedData.getURI().getPath()+"/"+file.getName()));
        file.delete();
    }

    private void mongoDbProcessFile(File file) throws IOException, ParseException {
        IndexDataMapperUtil indexDataMapperUtil = new IndexDataMapperUtil();
        List<IndexData> indexDataList = indexDataMapperUtil.getIndexDataFromFile(file);
        indexDataList.parallelStream().forEach(indexData -> {
            Query query = new Query();
            Update update = new Update();
            query.addCriteria(Criteria.where("symbol").is(indexData.getSymbol()));
            update.addToSet("marketDataList", indexData.getMarketDataList().get(0));
            mongoTemplate.upsert(query, update, IndexData.class);
        });
    }
}

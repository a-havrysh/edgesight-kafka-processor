package com.havrysh.edgesight.entity;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

@Data
@Document(indexName = "detections")
public class Detection {

    @Field(type = Keyword)
    private String uniqueObjectId;

    @Field(type = Keyword)
    private String objectType;

    @Field(type = FieldType.Double)
    private Double confidence;

    @Field(type = FieldType.Date)
    private LocalDateTime timestamp;

    @Field(type = Keyword)
    private String source;

    @GeoPointField
    private String location;
}
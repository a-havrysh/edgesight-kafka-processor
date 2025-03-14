package com.havrysh.edgesight.repository.elasticsearch;

import com.havrysh.edgesight.entity.Detection;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticsearchDetectionRepository extends ElasticsearchRepository<Detection, Long> {}
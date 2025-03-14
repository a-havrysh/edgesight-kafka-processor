package com.havrysh.edgesight.service;

import com.havrysh.edgesight.dto.DetectionDto;
import com.havrysh.edgesight.exception.ElasticsearchSyncException;
import com.havrysh.edgesight.mapper.DetectionMapper;
import com.havrysh.edgesight.repository.elasticsearch.ElasticsearchDetectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final DetectionMapper detectionMapper;
    private final ElasticsearchDetectionRepository elasticsearchRepository;

    @Transactional
    public void saveAll(List<DetectionDto> detections) {
        if (detections.isEmpty()) {
            return;
        }

        log.debug("Syncing {} detections to Elasticsearch", detections.size());

        try {
            elasticsearchRepository.saveAll(detectionMapper.toDetectionList(detections));

            log.debug("Successfully synced {} detections to Elasticsearch", detections.size());
        } catch (Exception e) {
            log.error("Failed to sync detections to Elasticsearch: {}", e.getMessage(), e);
            throw new ElasticsearchSyncException("Failed to sync detections to Elasticsearch", e);
        }
    }
}
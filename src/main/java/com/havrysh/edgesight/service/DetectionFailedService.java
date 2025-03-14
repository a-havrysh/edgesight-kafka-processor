package com.havrysh.edgesight.service;

import com.havrysh.edgesight.dto.DetectionDto;
import com.havrysh.edgesight.mapper.DetectionMapper;
import com.havrysh.edgesight.repository.jpa.DetectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetectionFailedService {

    private final DetectionMapper detectionMapper;
    private final DetectionRepository detectionRepository;
    private final ElasticsearchService elasticsearchService;

    @Transactional
    public void syncFailedDetectionsToElasticsearch(int batchSize) {
        var countUnprocessedDetections = detectionRepository.count();
        if (countUnprocessedDetections == 0) {
            return;
        }

        log.info("Found {} failed detections in SQL database", countUnprocessedDetections);

        var unprocessedDetections  = detectionRepository.findUnprocessedDetections(batchSize);
        if (unprocessedDetections.isEmpty()) {
            return;
        }

        log.info("Starting sync of {} unprocessed detections to Elasticsearch", unprocessedDetections.size());

        elasticsearchService.saveAll(detectionMapper.toDetectionDto(unprocessedDetections));
        detectionRepository.deleteAll(unprocessedDetections);

        log.info("Processed {}/{} detections", unprocessedDetections.size(), countUnprocessedDetections);
    }

    @Transactional
    public void saveFailedDetections(List<DetectionDto> detections) {
        if (detections.isEmpty()) {
            return;
        }

        log.debug("Save {} failed detections to SQL database", detections.size());


        try {
            detectionRepository.saveAll(detectionMapper.toDetectionFailed(detections));

            log.debug("Successfully saved {} failed detections to SQL database", detections.size());
        } catch (Exception e) {
            log.error("Failed to save failed detections to SQL database", e);
        }
    }
}

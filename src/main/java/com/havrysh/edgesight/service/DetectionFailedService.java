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
    public void syncFailedDetectionsToElasticsearch() {
        var unprocessedDetectionOpt = detectionRepository.findFirst();
        if (unprocessedDetectionOpt.isEmpty()) {
            return;
        }

        var unprocessedDetection = unprocessedDetectionOpt.get();
        var detections = unprocessedDetection.getDetections();

        log.info("Starting sync of {} unprocessed detections to Elasticsearch", detections.size());

        elasticsearchService.saveAll(detections);
        detectionRepository.delete(unprocessedDetection);

        log.info("Processed {} detections", detections.size());
    }

    @Transactional
    public void saveFailedDetections(List<DetectionDto> detections) {
        if (detections.isEmpty()) {
            return;
        }

        log.debug("Save {} failed detections to SQL database", detections.size());


        try {
            detectionRepository.save(detectionMapper.toDetectionFailed(detections));

            log.debug("Successfully saved {} failed detections to SQL database", detections.size());
        } catch (Exception e) {
            log.error("Failed to save failed detections to SQL database", e);
        }
    }
}

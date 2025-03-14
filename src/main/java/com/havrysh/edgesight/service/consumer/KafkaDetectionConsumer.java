package com.havrysh.edgesight.service.consumer;

import com.havrysh.edgesight.dto.DetectionDto;
import com.havrysh.edgesight.exception.ElasticsearchSyncException;
import com.havrysh.edgesight.service.DetectionFailedService;
import com.havrysh.edgesight.service.ElasticsearchService;
import jakarta.validation.Validator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaDetectionConsumer {

    private final Validator validator;
    private final ElasticsearchService elasticsearchService;
    private final DetectionFailedService detectionFailedService;

    @KafkaListener(topics = "${kafka.topic.detections}", batch = "true")
    public void processDetectionBatch(List<DetectionDto> detections) {
        log.info("Received batch of {} detections from Kafka", detections.size());

        if (detections.size() > 100) {
            log.warn("Batch size exceeds maximum allowed (100). Received: {}", detections.size());
        }

        var detectionsForProcessing = detections.stream()
                .filter(detection -> validator.validate(detection).isEmpty())
                .toList();

        log.info("Detections for processing: {}/{}", detectionsForProcessing.size(), detections.size());

        try {
            elasticsearchService.saveAll(detectionsForProcessing);
        } catch (ElasticsearchSyncException e) {
            detectionFailedService.saveFailedDetections(detectionsForProcessing);
        }
    }
}
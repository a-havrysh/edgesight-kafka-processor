package com.havrysh.edgesight.service.scheduler;

import com.havrysh.edgesight.service.DetectionFailedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.sync.enabled", havingValue = "true")
public class DetectionFailedScheduler {

    private final DetectionFailedService detectionFailedService;

    @Value("${scheduler.sync.batch-size}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${scheduler.sync.interval:60000}")
    public void syncDetectionsToElasticsearchScheduled() {
        log.info("DetectionFailedScheduler started");
        detectionFailedService.syncFailedDetectionsToElasticsearch(batchSize);
        log.info("DetectionFailedScheduler finished");
    }
}
package com.havrysh.edgesight.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

import com.havrysh.edgesight.dto.DetectionDto;
import com.havrysh.edgesight.entity.Detection;
import com.havrysh.edgesight.entity.DetectionFailed;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", nullValueCheckStrategy = ALWAYS)
public interface DetectionMapper {

    @Mapping(target = "id", ignore = true)
    Detection toDetection(DetectionDto detectionDto);

    List<Detection> toDetectionList(Collection<DetectionDto> detectionDtoList);

    List<DetectionFailed> toDetectionFailed(Collection<DetectionDto> detections);

    @Mapping(target = "createdDate", ignore = true)
    DetectionFailed toDetectionFailed(DetectionDto detection);

    DetectionDto toDetectionDto(DetectionFailed detectionFailed);

    List<DetectionDto> toDetectionDto(Collection<DetectionFailed> detectionFailedList);
}
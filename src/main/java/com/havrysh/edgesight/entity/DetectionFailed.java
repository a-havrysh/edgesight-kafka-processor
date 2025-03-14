package com.havrysh.edgesight.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.havrysh.edgesight.config.DetectionDtoConverter;
import com.havrysh.edgesight.dto.DetectionDto;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

@Data
@Entity
public class DetectionFailed {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Convert(converter = DetectionDtoConverter.class)
    private List<DetectionDto> detections;

    @CreatedDate
    private LocalDateTime createdDate;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DetectionFailed that)) {
            return false;
        }

        return Objects.equals(detections, that.detections);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(detections);
    }
}
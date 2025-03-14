package com.havrysh.edgesight.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

@Data
@Entity
public class DetectionFailed {

    @Id
    private String uniqueObjectId;

    @NotBlank
    private String objectType;

    @NotNull
    private Double confidence;

    @NotNull
    private LocalDateTime timestamp;

    @NotBlank
    private String source;

    @NotBlank
    private String location;

    @CreatedDate
    private LocalDateTime createdDate;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DetectionFailed that)) {
            return false;
        }

        return Objects.equals(uniqueObjectId, that.uniqueObjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uniqueObjectId);
    }
}
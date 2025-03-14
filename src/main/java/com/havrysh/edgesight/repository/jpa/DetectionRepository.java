package com.havrysh.edgesight.repository.jpa;

import com.havrysh.edgesight.entity.DetectionFailed;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectionRepository extends JpaRepository<DetectionFailed, Long> {

    Optional<DetectionFailed> findFirst();
}
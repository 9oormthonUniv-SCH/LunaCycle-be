package com.sch._roomthon.Pre_9roomthon.repository;

import com.sch._roomthon.Pre_9roomthon.entity.SleepInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface SleepInfoRepository extends JpaRepository<SleepInfoEntity, Long> {
    Optional<SleepInfoEntity> findByUserIdAndDate(UUID userId, LocalDate date);
    boolean existsByUserIdAndDate(UUID userId, LocalDate date);

}

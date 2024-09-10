package com.romanbai.kafka_mikro_user_service.repository;

import com.romanbai.kafka_mikro_user_service.entity.OutboxEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
  List<OutboxEvent> findByProcessedFalse();
}
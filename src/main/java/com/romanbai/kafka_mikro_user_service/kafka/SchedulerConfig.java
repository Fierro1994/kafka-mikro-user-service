package com.romanbai.kafka_mikro_user_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

  @Autowired
  private OutboxEventProcessor outboxEventProcessor;

  @Scheduled(fixedRate = 5000)
  public void scheduleOutboxProcessing() {
    outboxEventProcessor.processOutboxEvents();
  }
}
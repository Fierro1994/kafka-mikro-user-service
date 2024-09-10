package com.romanbai.kafka_mikro_user_service.kafka;

import com.romanbai.kafka_mikro_user_service.entity.OutboxEvent;
import com.romanbai.kafka_mikro_user_service.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class OutboxEventProcessor {
  private static final String TOPIC = "user-topic";
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private OutboxEventRepository outboxEventRepository;


  public void processOutboxEvents() {
    // Получаем все необработанные события
    List<OutboxEvent> events = outboxEventRepository.findByProcessedFalse();
    if(!events.isEmpty()){
      for (OutboxEvent event : events) {
        try {
          // Отправляем сообщение в Kafka
          CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, event.getEventType(), event.getPayload());

          future.whenComplete((result, error) -> {
            if (error == null) {
              // Устанавливаем флаг processed = true после успешной отправки
              event.setProcessed(true);
              outboxEventRepository.save(event);
              System.out.println("Сообщение отправлено в партицию " + result.getRecordMetadata().partition() + " с офсетом " + result.getRecordMetadata().offset());
            } else {
              System.err.println("Ошибка отправки сообщения: " + error.getMessage());
            }
          });
        } catch (Exception e) {
          System.err.println("Ошибка обработки события: " + e.getMessage());
        }
      }
    }

  }
}
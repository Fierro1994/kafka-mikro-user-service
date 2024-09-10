package com.romanbai.kafka_mikro_user_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanbai.kafka_mikro_user_service.dto.UserEvent;
import com.romanbai.kafka_mikro_user_service.entity.OutboxEvent;
import com.romanbai.kafka_mikro_user_service.entity.User;
import com.romanbai.kafka_mikro_user_service.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserKafkaProducer {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private OutboxEventRepository outboxEventRepository;



  @Transactional
  public void sendUserCreatedEvent(User user) throws JsonProcessingException {
    // Создаем событие UserEvent
    UserEvent userEvent = new UserEvent(user.getId(), user.getName(), user.getEmail());
    String userEventJson = objectMapper.writeValueAsString(userEvent);

    // Сохраняем событие в таблицу Outbox
    OutboxEvent outboxEvent = new OutboxEvent();
    outboxEvent.setEventType("UserCreated");
    outboxEvent.setPayload(userEventJson);
    outboxEvent.setProcessed(false);
    outboxEventRepository.save(outboxEvent);

    // После успешного сохранения транзакция завершится
  }
}
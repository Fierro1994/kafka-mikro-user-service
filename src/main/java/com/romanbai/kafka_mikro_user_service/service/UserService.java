package com.romanbai.kafka_mikro_user_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.romanbai.kafka_mikro_user_service.entity.User;
import com.romanbai.kafka_mikro_user_service.kafka.UserKafkaProducer;
import com.romanbai.kafka_mikro_user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserKafkaProducer userProducer;

  public User createUser(User user) throws JsonProcessingException {

    User savedUser = userRepository.save(user);

    userProducer.sendUserCreatedEvent(savedUser);

    return savedUser;
  }
}
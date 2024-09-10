package com.romanbai.kafka_mikro_user_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.romanbai.kafka_mikro_user_service.entity.User;
import com.romanbai.kafka_mikro_user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  public User createUser(@RequestBody User user) throws JsonProcessingException {
    return userService.createUser(user);
  }
}
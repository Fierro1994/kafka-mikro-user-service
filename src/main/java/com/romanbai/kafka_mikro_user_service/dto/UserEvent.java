package com.romanbai.kafka_mikro_user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEvent {
  private Long id;
  private String name;
  private String email;

  public UserEvent(Long id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

}
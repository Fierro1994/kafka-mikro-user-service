package com.romanbai.kafka_mikro_user_service.repository;

import com.romanbai.kafka_mikro_user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
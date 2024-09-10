package com.romanbai.kafka_mikro_user_service.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaTopicInfoUtil {

  @Autowired
  private KafkaAdmin kafkaAdmin;

  public Map<String, TopicDescription> getTopicDetails(String topicName) {
    AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
    try {
      DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singletonList(topicName));
      return describeTopicsResult.all().get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return Collections.emptyMap();
    } finally {
      adminClient.close();
    }
  }

  public void printTopicDetails(String topicName) {
    Map<String, TopicDescription> topicDetails = getTopicDetails(topicName);
    if (topicDetails.isEmpty()) {
      System.out.println("Не удалось получить информацию о топике: " + topicName);
      return;
    }

    TopicDescription description = topicDetails.get(topicName);
    System.out.println("Информация о топике: " + topicName);
    System.out.println("Количество разделов: " + description.partitions().size());
    System.out.println("Количество реплик: " + description.partitions().get(0).replicas().size());
    System.out.println("Настройки разделов:");
    description.partitions().forEach(partition -> {
      System.out.println("  Раздел " + partition.partition() + ":");
      System.out.println("    Лидер: " + partition.leader());
      System.out.println("    Количество реплик: " + partition.replicas().size());
      System.out.println("    Количество in-sync реплик: " + partition.isr().size());
      System.out.println("    Реплики: " + partition.replicas());
      System.out.println("    In-sync реплики (ISR): " + partition.isr());
    });
    System.out.println("-----------------------------");
  }

  public Set<String> listAllTopics() {
    AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
    try {
      ListTopicsResult listTopicsResult = adminClient.listTopics();
      return listTopicsResult.names().get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return Collections.emptySet();
    } finally {
      adminClient.close();
    }

  }

  @EventListener(ApplicationReadyEvent.class)
  public void printAllTopicsInfoOnStartup() {
    System.out.println("Вывод информации о всех топиках Kafka при запуске приложения:");
    Set<String> allTopics = listAllTopics();
    for (String topicName : allTopics) {
      printTopicDetails(topicName);
    }
  }

  public void deleteTopic(String topicName) {
    AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
    try {
      DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singletonList(topicName));
      deleteTopicsResult.all().get();
      System.out.println("Топик '" + topicName + "' успешно удален.");
    } catch (InterruptedException | ExecutionException e) {
      System.err.println("Ошибка при удалении топика '" + topicName + "': " + e.getMessage());
      e.printStackTrace();
    } finally {
      adminClient.close();
    }
  }
}

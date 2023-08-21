package ru.skillbox.socialnetwork.notifications.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.skillbox.socialnetwork.notifications.entities.Notification;
import ru.skillbox.socialnetwork.notifications.repositories.NotificationsRepository;

import javax.transaction.Transactional;

@Slf4j
@Component
public class KafkaConsumer {

	private final NotificationsRepository notificationsRepository;

	@Value(value = "${spring.application.name}")
	private String groupId;

	private final String KEYWORD_FOR_CONSUMER = "WORLD";

	public KafkaConsumer(NotificationsRepository notificationsRepository) {
		this.notificationsRepository = notificationsRepository;
	}

	@Transactional
	@KafkaListener(topics = "notify-topic", groupId = "notify-group", containerFactory = "kafkaListenerContainerFactory")
	public void listenGroupAuth(String message) {
		log.info("<-- message '{}' received from group '{}'", message, groupId);
		try {
			notificationsRepository.save(new Notification(123, message));
			log.info(" > Notification saved into DB");
		}
		catch (Exception e){
			log.error(e.getMessage());
		}
	}
//
//	@KafkaListener(topics = "${kafka.topics.auth}", groupId = "${spring.application.name}", containerFactory = "filteredKafkaListenerContainerFactory")
//	public void listenFilteredMessage(String message) {
//		log.info("◀ message '{}' received from group '{}' filtered by KEYWORD '{}'", message, groupId, KEYWORD_FOR_CONSUMER);
//	}
//
//	@KafkaListener(topics = "${kafka.topics.auth}", groupId = "${spring.application.name}", containerFactory = "jsonKafkaListenerContainerFactory")
//	public void jsonListener(@NotNull JsonMessage jsonMessage) {
//		log.info("◀ JSON message '{}' received from group '{}'", jsonMessage.toString(), groupId);
//	}

//	@KafkaListener(topics = "${kafka.topics.auth}", groupId = "${spring.application.name}", containerFactory = "kafkaListenerContainerFactory")
//	public void listenGroupAuth(String message) {
//		log.info("◀ message '{}' received from group '{}'", message, groupId);
//	}
}

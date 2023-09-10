package ru.skillbox.socialnetwork.notifications.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.skillbox.socialnetwork.notifications.dto.kafka.CommonNotifyDto;
import ru.skillbox.socialnetwork.notifications.model.CommonNotifyModel;
import ru.skillbox.socialnetwork.notifications.repositories.NotificationsRepository;
import ru.skillbox.socialnetwork.notifications.service.NotificationService;

import javax.transaction.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

	private final NotificationsRepository notificationsRepository;
	private final ObjectMapper objectMapper;
	private final ModelMapper modelMapper;
	private final NotificationService notificationService;

	@Value(value = "${spring.application.name}")
	private String groupId;

	@Value(value = "${kafka.topics.notify}")
	private String notifyTopic;
	@Value(value = "${kafka.topics.notify-common}")
	private String notifyCommonTopic;

	private final String KEYWORD_FOR_CONSUMER = "WORLD";

	@Transactional
	@KafkaListener(topics = "notify-topic", groupId = "${spring.application.name}", containerFactory = "kafkaListenerContainerFactory")
	public void listenGroupAuth(@NotNull String message) {
		log.info("--|< message '{}' received from group '{}' from topic '{}'", message, groupId, notifyTopic);
//		NotificationModel notificationModel;
//
//		try {
//			notificationsRepository.save(new NotificationModel(123, message));
//			log.info(" * NotificationModel read but not saved into DB");
//		}
//		catch (Exception e){
//			log.error(e.getMessage());
//		}
	}

	@KafkaListener(topics = "${kafka.topics.notify-common}", groupId = "${spring.application.name}", containerFactory = "kafkaListenerContainerFactory")
	public void listenGroupNotify(String message) {
		CommonNotifyDto commonNotifyDto;
		try {
			commonNotifyDto = objectMapper.readValue(message, CommonNotifyDto.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		log.info("--|< Message data received from group '{}' from topic '{}'", groupId, notifyCommonTopic);

		CommonNotifyModel commonNotifyModel = modelMapper.map(commonNotifyDto, CommonNotifyModel.class);
		log.info("--|< Common notify data received and map to CommonNotifyModel entity. {}", commonNotifyModel);

		notificationService.saveCommonNotify(commonNotifyModel);

	}
}

package ru.skillbox.socialnetwork.notifications.kafka.consumer;

import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value(value = "${kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value(value = "${spring.application.name")
	private String groupId;

	@Value(value = "${kafka.backoff.interval}")
	private Long interval;

	@Value(value = "${kafka.backoff.max_failure}")
	private Long maxAttempts;

	private final String KEYWORD_FOR_CONSUMER = "WORLD";

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> filteredKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setRecordFilterStrategy(record -> record.value().contains(KEYWORD_FOR_CONSUMER));
		return factory;
	}

	@Bean
	public ConsumerFactory<String, Object> objectConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object>	objectKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(objectConsumerFactory());
		factory.setCommonErrorHandler(errorHandler());
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
		factory.afterPropertiesSet();
		return factory;
	}

	@Bean
	public DefaultErrorHandler errorHandler() {
		BackOff fixedBackOff = new FixedBackOff(interval, maxAttempts);
		DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, e) -> {}, fixedBackOff);
		errorHandler.addRetryableExceptions(SocketTimeoutException.class);
		errorHandler.addNotRetryableExceptions(NullPointerException.class);
		return errorHandler;
	}

}

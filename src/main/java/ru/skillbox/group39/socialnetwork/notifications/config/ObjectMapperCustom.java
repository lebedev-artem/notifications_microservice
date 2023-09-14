package ru.skillbox.group39.socialnetwork.notifications.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Artem Lebedev | 14/09/2023 - 21:54
 */

@Slf4j
@Component
public class ObjectMapperCustom {

	public ObjectMapper objectMapper() {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		objectMapper.addHandler(new DeserializationProblemHandler() {

			@Override
			public boolean handleUnknownProperty(DeserializationContext ctxt,
			                                     JsonParser jp, JsonDeserializer<?> deserializer,
			                                     Object beanOrClass, String propertyName)
					throws IOException, JsonProcessingException {

				String unknownField = String.format(" ! Ignoring unknown property %s while deserializing %s", propertyName, beanOrClass);
				log.error(getClass().getSimpleName(), unknownField);
				return true;
			}
		});

		return objectMapper;
	}
}

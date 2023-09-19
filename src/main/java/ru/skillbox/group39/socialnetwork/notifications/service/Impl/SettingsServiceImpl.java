package ru.skillbox.group39.socialnetwork.notifications.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.group39.socialnetwork.notifications.client.UsersClient;
import ru.skillbox.group39.socialnetwork.notifications.client.dto.AccountDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.setting.SettingChangeDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.setting.SettingsDto;
import ru.skillbox.group39.socialnetwork.notifications.exception.ErrorResponse;
import ru.skillbox.group39.socialnetwork.notifications.exception.exceptions.SettingsNotFoundException;
import ru.skillbox.group39.socialnetwork.notifications.model.settings.SettingsModel;
import ru.skillbox.group39.socialnetwork.notifications.repositories.SettingsRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.SettingsRepositoryImpl;
import ru.skillbox.group39.socialnetwork.notifications.service.SettingsService;

import javax.transaction.Transactional;
import java.util.Optional;

import static ru.skillbox.group39.socialnetwork.notifications.security.service.UserDetailsServiceImpl.getPrincipalId;

/**
 * @author Artem Lebedev | 16/09/2023 - 22:29
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

	private final UsersClient usersClient;
	private final SettingsRepository settingsRepository;
	private final SettingsRepositoryImpl settingsRepositoryImpl;
	private final ModelMapper modelMapper;

	@Override
	public Object getSettings() {
		Optional<SettingsModel> sm =
				Optional.of(settingsRepository
						.findByUserId(getPrincipalId())
						.orElseThrow(() -> new SettingsNotFoundException("User / settings not found")));

		return new ResponseEntity<>(modelMapper.map(sm, SettingsDto.class), HttpStatus.OK);
	}

	@Override
	@Transactional
	public Object createSettings(Long userId) {
		log.info(" * service/NotificationsSettings/createSettings");
		AccountDto user;
		try {
			user = usersClient.getUserDetailsById(userId);
		} catch (RuntimeException e) {
			log.error(" ! User with id '{}' not found", userId);
			return new ResponseEntity<>(
					new ErrorResponse(
							"Error while creating a setting for user id " + userId.toString(),
							HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
		Optional<SettingsModel> sm = Optional.of(settingsRepository.findByUserId(userId).orElse(new SettingsModel(user.getId())));
		settingsRepository.save(sm.get());

		return new ResponseEntity<>(modelMapper.map(sm, SettingsDto.class), HttpStatus.OK);
	}

	@Override
	public Object changeSetting(@NotNull SettingChangeDto scd) {
		Optional<SettingsModel> sm = Optional.of(settingsRepository
				.findByUserId(getPrincipalId())
				.orElseThrow(() -> new SettingsNotFoundException("User / settings not found")));

		Long id = sm.get().getId();

		try {
			String key = scd.getNotificationType().getType();
			Boolean value = scd.getEnable();
			settingsRepositoryImpl.updateSetting(id, key, value);
			log.info(" * Setting '{}' set '{}'", key, value);

		} catch (RuntimeException e) {
			return new ResponseEntity<>(new ErrorResponse("Error while updating a setting. " + e.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}
}

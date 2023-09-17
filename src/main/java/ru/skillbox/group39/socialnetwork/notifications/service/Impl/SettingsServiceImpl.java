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

	@Override
	public Object getSettings() {
		Optional<SettingsModel> sm =
				Optional.of(settingsRepository
						.findByUserId(getPrincipalId())
						.orElseThrow(() -> new SettingsNotFoundException("User / settings not found")));

		return new ResponseEntity<>(sm, HttpStatus.OK);
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
			return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, "User not found"), HttpStatus.BAD_REQUEST);
		}

		SettingsModel nsm = new SettingsModel(user.getId());
		settingsRepository.save(nsm);

		return new ResponseEntity<>(nsm, HttpStatus.OK);
	}

	@Override
	public Object changeSetting(@NotNull SettingChangeDto scd) {
		Optional<SettingsModel> sm = Optional.of(settingsRepository
				.findByUserId(getPrincipalId())
				.orElseThrow(() -> new SettingsNotFoundException("User / settings not found")));

		Long id = sm.get().getId();

		try {
			String key = scd.getType().getType();
			Boolean value = scd.getEnable();
			settingsRepositoryImpl.updateSetting(id, key, value);
			log.info(" * Setting '{}' set '{}'", key, value);

		} catch (RuntimeException e) {
			return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, "Error while updating a setting"), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}
}

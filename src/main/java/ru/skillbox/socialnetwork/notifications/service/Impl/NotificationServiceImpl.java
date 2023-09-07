package ru.skillbox.socialnetwork.notifications.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.notifications.model.CommonNotifyModel;
import ru.skillbox.socialnetwork.notifications.repositories.CommonNotifyRepository;
import ru.skillbox.socialnetwork.notifications.service.NotificationService;

import javax.transaction.Transactional;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:26
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final CommonNotifyRepository commonNotifyRepository;

	@Override
	@Transactional
	public void saveCommonNotify(CommonNotifyModel model) {
		commonNotifyRepository.save(model);
		log.info(" * CommonNotifyModel saved to DB/notifications/commonnotifications");
	}
}

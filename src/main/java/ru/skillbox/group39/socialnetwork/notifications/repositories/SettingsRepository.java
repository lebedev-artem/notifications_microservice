package ru.skillbox.group39.socialnetwork.notifications.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.group39.socialnetwork.notifications.model.settings.SettingsModel;
import java.util.Optional;

/**
 * @author Artem Lebedev | 16/09/2023 - 22:39
 */

public interface SettingsRepository extends JpaRepository<SettingsModel, Long> {
	Optional<SettingsModel> findByUserId(Long userId);
}

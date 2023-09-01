package ru.skillbox.socialnetwork.notifications.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Artem Lebedev | 24/08/2023 - 17:46 <p>
 * description - dto для установки настроек оповещений <p>
 * enable - разрешить / запретить оповещения для данного типа событий
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationUpdateDto {

    private boolean enable;
    private NotificationType notificationType;
}

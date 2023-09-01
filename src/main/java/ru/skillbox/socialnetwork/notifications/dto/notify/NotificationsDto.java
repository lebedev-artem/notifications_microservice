package ru.skillbox.socialnetwork.notifications.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Artem Lebedev | 24/08/2023 - 17:46 <p>
 * description - dto события <p>
 * timeStamp - время отправки события <p>
 * data - dto события доп. <p>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsDto {

    private LocalDateTime timeStamp;
    private NotificationDto data;
}

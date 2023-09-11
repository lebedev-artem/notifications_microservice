package ru.skillbox.socialnetwork.notifications.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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

    private Timestamp timeStamp;
    private List<NotificationDto> data;
}

package ru.skillbox.group39.socialnetwork.notifications.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Artem Lebedev | 24/08/2023 - 17:46 <p>
 * description - dto события <p>
 * timeStamp - время отправки события <p>
 * data - dto события доп. <p>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationStampedDto {

    private Timestamp timeStamp;
    private NotificationDto data;
}

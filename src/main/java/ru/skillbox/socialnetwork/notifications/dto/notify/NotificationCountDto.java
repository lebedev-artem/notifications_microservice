package ru.skillbox.socialnetwork.notifications.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.socialnetwork.notifications.dto.Count;

import java.time.LocalDateTime;

/**
 * @author Artem Lebedev | 24/08/2023 - 19:23 <p>
 * description - dto получения счетчика событий <p>
 * timeStamp - время показа счетчика событий <p>
 * count - значение счетчика событий <p>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCountDto {

    private LocalDateTime timeStamp;
    private Count count;
}

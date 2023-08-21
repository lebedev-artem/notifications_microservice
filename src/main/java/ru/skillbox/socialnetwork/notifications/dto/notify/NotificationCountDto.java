package ru.skillbox.socialnetwork.notifications.dto.notify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.socialnetwork.notifications.dto.Count;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto получения счетчика событий")
public class NotificationCountDto {

    //Время выдачи показаний счетчика
    private LocalDateTime timeStamp;

    private Count data;
}

package ru.skillbox.socialnetwork.notifications.dto.notify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto события")
public class NotificationsDto {

    //Врямя отправки события
    LocalDateTime timeStamp;

    //Dto события доп
    NotificationDto data;
}

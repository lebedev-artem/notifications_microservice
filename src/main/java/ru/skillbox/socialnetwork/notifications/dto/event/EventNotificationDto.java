package ru.skillbox.socialnetwork.notifications.dto.event;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto получения уведомлений о событии")
public class EventNotificationDto {

    private Integer authorId;

    private String receiverId;

    private String notificationType;

    private String content;
}

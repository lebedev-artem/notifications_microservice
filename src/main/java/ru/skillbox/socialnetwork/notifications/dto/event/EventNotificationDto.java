package ru.skillbox.socialnetwork.notifications.dto.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.socialnetwork.notifications.dto.notify.NotificationType;

/**
 * @author Artem Lebedev | 24/08/2023 - 17:46 <p>
 * description - Dto получения уведомлений о событии <p>
 * authorId - автор события <p>
 * receiverId - получатель события <p>
 * notificationType - тип уведомления <p>
 * content - описание события <p>
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventNotificationDto {

    private Integer authorId;
    private String receiverId;
    private NotificationType notificationType;
    private String content;
}

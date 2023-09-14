package ru.skillbox.group39.socialnetwork.notifications.dto.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.group39.socialnetwork.notifications.dto.common.CommonNotifyTypeEnum;

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

    private Long authorId;
    private String receiverId;
    private CommonNotifyTypeEnum notificationType;
    private String content;
}

package dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Dto получения уведомлений о событии")
public class EventNotificationDto {
    public UUID authorId;
    public UUID receiverId;
    public String notificationType;
    public String content;
}
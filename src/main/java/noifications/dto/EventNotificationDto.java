package dto;
import lombok.Data;

import java.util.UUID;

@Data
public class EventNotificationDto {
    public UUID authorId;
    public UUID receiverId;
    public String notificationType;
    public String content;
}
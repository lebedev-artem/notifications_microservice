package dto;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Dto события доп.")
public class NotificationDto {
    public UUID id;

    //Id пользователя, создавшего событие
    public UUID authorId;

    //Описание события
    public String content;

    //Тип события
    public String notificationType;

    //Время события
    LocalDateTime sentTime;
}

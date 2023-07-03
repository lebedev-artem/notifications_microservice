package dto;

import java.util.UUID;
import lombok.Data;
import java.time.LocalDateTime;

//Dto события доп
@Data
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

package dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Dto события")
public class NotificationsDto {

    //Врямя отправки события
    LocalDateTime timeStamp;

    //Dto события доп
    NotificationDto data;
}

package dto;
import lombok.Data;
import java.time.LocalDateTime;

//Dto события
@Data
public class NotificationsDto {

    //Врямя отправки события
    LocalDateTime timeStamp;

    //Dto события доп
    NotificationDto data;
}

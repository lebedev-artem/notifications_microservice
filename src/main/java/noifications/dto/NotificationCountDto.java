package dto;
import lombok.Data;
import java.time.LocalDateTime;

//Dto получения счетчика событий
@Data
public class NotificationCountDto {
    //Время выдачи показаний счетчика
    LocalDateTime timeStamp;
    Count data;
}
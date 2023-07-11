package dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Dto получения счетчика событий")
public class NotificationCountDto {
    //Время выдачи показаний счетчика
    LocalDateTime timeStamp;
    Count data;
}
package dto;
import lombok.Data;

//Dto для установки настроек оповещений
@Data
public class NotificationUpdateDto {

    //Разрешить оповещение для данного типа событий
    public boolean enable;
    public String notificationType;
}

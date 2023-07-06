package dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dto для установки настроек оповещений")
public class NotificationUpdateDto {

    //Разрешить оповещение для данного типа событий
    public boolean enable;
    public String notificationType;
}

package ru.skillbox.socialnetwork.notifications.dto.notify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto для установки настроек оповещений")
public class NotificationUpdateDto {

    //Разрешить оповещение для данного типа событий
    private boolean enable;

    private String notificationType;
}

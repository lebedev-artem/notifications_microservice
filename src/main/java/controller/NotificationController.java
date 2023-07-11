package controller;
import io.swagger.v3.oas.annotations.Operation;
import dto.EventNotificationDto;
import dto.NotificationSettingsDto;
import dto.NotificationUpdateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Notification service", description = "Сервис сообщений")

public class NotificationController implements controller.NotificationControllerApi {

    @Override
    @Operation(summary = "Получение настроек оповещений")
    public ResponseEntity<NotificationSettingsDto> getSetting(String id) {
        return ResponseEntity.ok(new NotificationSettingsDto());
    }

    @Override
    @Operation(summary = "Коррекция настроек оповещений")
    public ResponseEntity<NotificationSettingsDto> updateSetting(String id) {
        return ResponseEntity.ok(new NotificationSettingsDto());
    }

    @Override
    @Operation(summary = "Отметить все события, как прочитанные")
    public ResponseEntity<NotificationUpdateDto> setIsRead(String id) {
        return ResponseEntity.ok(new NotificationUpdateDto());
    }

    @Override
    @Operation(summary = "Создание настроек оповещений")
    public ResponseEntity<NotificationUpdateDto> createSetting(String id) {
        return ResponseEntity.ok(new NotificationUpdateDto());
    }

    @Override
    @Operation(summary = "Создание события")
    public ResponseEntity<EventNotificationDto> addEvent(String id) {
        return ResponseEntity.ok(new EventNotificationDto());
    }

    @Override
    @Operation(summary = "Получить счетчик количества событий")
    public ResponseEntity<NotificationSettingsDto> getCount(String id) {
        return ResponseEntity.ok(new NotificationSettingsDto());
    }
}


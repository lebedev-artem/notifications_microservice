package controller;
import io.swagger.v3.oas.annotations.Operation;
import dto.EventNotificationDto;
import dto.NotificationSettingsDto;
import dto.NotificationUpdateDto;
import dto.PageNotificationsDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Notification service", description = "Сервис сообщений")

public class NotificationController implements NotificationControllerApi{

    @Override
    @Operation(summary = "Получение настроек оповещений")
    public ResponseEntity<NotificationSettingsDto> getSetting(String id) {
        return ResponseEntity.ok(new NotificationSettingsDto());
    }

    @Override
    public ResponseEntity<NotificationUpdateDto> updateSetting(String id) {
        return null;
    }

    @Override
    public ResponseEntity<NotificationUpdateDto> setIsRead(String id) {
        return null;
    }

    @Override
    public ResponseEntity<NotificationUpdateDto> createSetting(String id) {
        return null;
    }

    @Override
    public ResponseEntity<EventNotificationDto> addEvent(String id) {
        return null;
    }

    @Override
    public ResponseEntity<PageNotificationsDto> getNotifications(String id) {
        return null;
    }

    @Override
    public ResponseEntity<NotificationSettingsDto> getCount(String id) {
        return null;
    }

}









//import dto.NotificationSettingsDto;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
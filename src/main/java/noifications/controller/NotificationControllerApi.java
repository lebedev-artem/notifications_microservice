package controller;
import dto.NotificationSettingsDto;
import dto.NotificationUpdateDto;
import dto.EventNotificationDto;
import dto.PageNotificationsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/notifications")
public interface NotificationControllerApi {
    @GetMapping(value = "/settings")
    ResponseEntity<NotificationSettingsDto> getSetting(@PathVariable String id);

    @PutMapping(value = "/settings")
    ResponseEntity<NotificationUpdateDto> updateSetting(@PathVariable String id);

    @PutMapping(value = "/readed")
    ResponseEntity<NotificationUpdateDto> setIsRead(@PathVariable String id);

    @PostMapping(value = "/settings{id}")
    ResponseEntity<NotificationUpdateDto> createSetting(@PathVariable String id);

    @PostMapping(value = "/add")
    ResponseEntity<EventNotificationDto> addEvent(@PathVariable String id);

    @GetMapping
    ResponseEntity<PageNotificationsDto> getNotifications(@PathVariable String id);

    @GetMapping(value = "/count")
    ResponseEntity<NotificationSettingsDto> getCount(@PathVariable String id);

}

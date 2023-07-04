package controller;
import dto.NotificationSettingsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class NotificationController {
    @Operation(summary = "get NotificationSetting", description = "Получение настроек оповещений", tags = {"Notification service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account",
            method = RequestMethod.GET)
    ResponseEntity<NotificationSettingsDto> notification() {
        return new ResponseEntity<dto.NotificationSettingsDto>(HttpStatus.OK);
    }

}

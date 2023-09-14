package ru.skillbox.group39.socialnetwork.notifications.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.group39.socialnetwork.notifications.dto.event.EventNotificationDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationCountDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationSettingDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationUpdateDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.PageNotificationsDto;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/api/v1/notifications")
public interface NotificationController {

    @Operation(
            description = "Получить счетчик количества событий",
            security = {@SecurityRequirement(name = "JWT")}, tags = {"NotificationSimpleModel service"})
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = NotificationCountDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(
            value = "/count",
            method = RequestMethod.GET)
    Object getCount();

// ---------------------------------------------------------------------------------------------------------------------

    @Operation(
            description = "Получение страницы событий",
            security = {@SecurityRequirement(name = "JWT")}, tags = {"NotificationSimpleModel service"})
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "*/*", schema = @Schema(implementation = PageNotificationsDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/",
            produces = APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    Object getPageNotifications(
            @RequestParam (required = false, defaultValue = "0") Integer page,
            @RequestParam (required = false, defaultValue = "5") Integer size,
            @RequestParam (required = false, defaultValue = "timestamp") @Nullable String sort);

// ---------------------------------------------------------------------------------------------------------------------

    @Operation(
            description = "Создание события",
            security = {@SecurityRequirement(name = "JWT")},
            tags = {"NotificationSimpleModel service"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(
            value = "/add",
            consumes = APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    Object addEvent(@Parameter(in = ParameterIn.DEFAULT, required = true)
                                  @Valid @RequestBody EventNotificationDto body);

    // ---------------------------------------------------------------------------------------------------------------------

    @Operation(
            description = "Создание настроек оповещений",
            security = {@SecurityRequirement(name = "JWT")},
            tags = {"NotificationSimpleModel service"})
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(mediaType = "*/*", schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/settings{id}",
            produces = APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    ResponseEntity<Boolean> createSetting(@Parameter(in = ParameterIn.PATH, required = true)
                                          @PathVariable("id") String id);

// ---------------------------------------------------------------------------------------------------------------------

    @Operation(
            description = "Получение настроек оповещений",
            security = {@SecurityRequirement(name = "JWT")},
            tags = {"NotificationSimpleModel service"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(mediaType = "*/*", schema = @Schema(implementation = NotificationSettingDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(
            value = "/settings",
            produces = APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    ResponseEntity<NotificationSettingDto> getSetting();

// ---------------------------------------------------------------------------------------------------------------------

    @Operation(
            description = "Отметить все события, как прочитанные",
            security = {@SecurityRequirement(name = "JWT")},
            tags = {"NotificationSimpleModel service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/readed",
            method = RequestMethod.PUT)
    ResponseEntity<Void> setIsRead();

// ---------------------------------------------------------------------------------------------------------------------

    @Operation(
            description = "Коррекция настроек оповещений",
            security = {@SecurityRequirement(name = "JWT")},
            tags = {"NotificationSimpleModel service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(
            value = "/settings",
            consumes = APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT)
    ResponseEntity<Void> updateSetting(
            @Parameter(in = ParameterIn.DEFAULT, required = true)
            @Valid @RequestBody NotificationUpdateDto body);
}

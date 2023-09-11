package ru.skillbox.group39.socialnetwork.notifications.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto получения счетчика событий доп.")
public class Count {

    //Счетчик событий доп.
    private Long count;
}

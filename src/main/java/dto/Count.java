package dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dto получения счетчика событий доп.")
public class Count {
    //Счетчик событий доп.
    public Long count;
}
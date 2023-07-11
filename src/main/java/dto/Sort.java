package dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dto сортировки")
public class Sort {
    public boolean empty;
    public boolean unsorted;
    public boolean sorted;
}

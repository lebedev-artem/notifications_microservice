package dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dto страниц")
public class Pageable {

    //minimum: 0
    int page = 0;

    //minimum: 1
    int size = 1;
    dto.PageableSort sort;
}

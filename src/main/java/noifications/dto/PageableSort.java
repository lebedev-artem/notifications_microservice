package dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.ArrayList;

@Data
@Schema(description = "Dto постраничной сортировки")
public class PageableSort {
    ArrayList pageableSort = new ArrayList();
}

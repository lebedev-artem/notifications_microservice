package dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dto постраничных объектов")
public class PageableObject {
    public Long offset;
    Sort sort;
    public boolean paged;
    public Integer pageSize;
    public boolean unpaged;
    public Integer pageNumber;
}

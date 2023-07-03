package dto;
import lombok.Data;

@Data
public class PageableObject {
    public Long offset;
    Sort sort;
    public boolean paged;
    public Integer pageSize;
    public boolean unpaged;
    public Integer pageNumber;
}

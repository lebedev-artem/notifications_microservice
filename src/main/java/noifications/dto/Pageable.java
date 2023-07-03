package dto;
import lombok.Data;

@Data
public class Pageable {

    //minimum: 0
    int page = 0;

    //minimum: 1
    int size = 1;
    PageableSort sort;
}

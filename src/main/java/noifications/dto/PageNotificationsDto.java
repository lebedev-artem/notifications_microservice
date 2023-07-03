package dto;
import lombok.Data;

@Data
public class PageNotificationsDto {
    public Integer totalPages;
    public Long totalElements;
    public Integer number;
    public Integer size;

    //Dto события
    NotificationsDto content;
    Sort sort;
    public boolean first;
    public boolean last;
    public Integer numberOfElements;
    PageableObject pageable;
    public boolean empty;
}

package dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dto уведомлений страниц")
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

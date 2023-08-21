package ru.skillbox.socialnetwork.notifications.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageNotificationsDto {

	private  Integer totalPages;

	private  Integer totalElements;

	private Integer number;

	private Integer size;

	private Sort sort;

	private Boolean first;

	private Boolean last;

	private Integer numberOfElements;

	private Pageable pageable;

	private Boolean empty;
}

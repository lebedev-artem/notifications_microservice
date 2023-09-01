package ru.skillbox.socialnetwork.notifications.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Artem Lebedev | 24/08/2023 - 20:32 <p>
 * description - Think for yourself.
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageNotificationsDto {

	private Boolean empty;
	private Integer number;
	private Integer size;
	private Integer numberOfElements;
	private Integer totalPages;
	private Integer totalElements;
	private Boolean first;
	private Boolean last;
	private Sort sort;
	private Pageable pageable;

}

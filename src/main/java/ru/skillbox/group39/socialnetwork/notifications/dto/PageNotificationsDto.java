package ru.skillbox.group39.socialnetwork.notifications.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationStampedDto;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Artem Lebedev | 24/08/2023 - 20:32 <p>
 * description - Think for yourself.
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageNotificationsDto {

	@JsonProperty("totalPages")
	private Integer totalPages;

	@JsonProperty("totalElements")
	private Integer totalElements;

	@JsonProperty("number")
	private Integer number;

	@JsonProperty("size")
	private Integer size;

	@JsonProperty("content")
	@Valid
	private List<NotificationStampedDto> content;

	@JsonProperty("sort")
	private Sort sort;

	@JsonProperty("first")
	private Boolean first;

	@JsonProperty("last")
	private Boolean last;

	@JsonProperty("numberOfElements")
	private Integer numberOfElements;

	@JsonProperty("pageable")
	private Pageable pageable;

	@JsonProperty("empty")
	private Boolean empty;







}

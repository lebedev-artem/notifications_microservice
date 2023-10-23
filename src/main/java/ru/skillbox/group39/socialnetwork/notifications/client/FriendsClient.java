package ru.skillbox.group39.socialnetwork.notifications.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.skillbox.group39.socialnetwork.notifications.config.FeignSupportConfig;

import java.util.List;

@FeignClient(name = "friends-service", url = "${gtw.friends-service}", configuration = FeignSupportConfig.class)
public interface FriendsClient {
	String authHeader = "Authorization";
	@GetMapping(
			value = "/friendId/{userId}",
			produces = {"application/json"})
	List<Long> getFriendsListById(@RequestHeader(authHeader) String bearerToken, @PathVariable Long userId);
}

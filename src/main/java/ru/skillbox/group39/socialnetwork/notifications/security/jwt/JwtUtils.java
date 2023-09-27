package ru.skillbox.group39.socialnetwork.notifications.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.skillbox.group39.socialnetwork.notifications.client.UsersClient;
import ru.skillbox.group39.socialnetwork.notifications.client.dto.AccountDto;
import ru.skillbox.group39.socialnetwork.notifications.security.model.Person;
import ru.skillbox.group39.socialnetwork.notifications.security.service.UserDetailsServiceImpl;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {
	@Value("${jwt.secret}")
	private String SECRET;

	@Value("${jwt.access-token-duration}")
	private Duration ACCESS_TOKEN_DURATION;
	private final SignatureAlgorithm TOKEN_ALG = SignatureAlgorithm.HS256;
    private final UsersClient usersClient;
    private final UserDetailsServiceImpl userDetailsService;

	public String generateToken(@NonNull final Person person,
	                            @NotNull final Duration duration) {
		final Map<String, Object> claims = collectClaims(person);
		final Date issuedDate = new Date();
		final Date expiredDate = new Date(issuedDate.getTime() + duration.toMillis());
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(person.getUsername())
				.setIssuedAt(issuedDate)
				.setExpiration(expiredDate)
				.signWith(TOKEN_ALG, SECRET)
				.compact();
	}

	@NonNull
	private static Map<String, Object> collectClaims(@NonNull final Person person) {
		final Map<String, Object> claims = new HashMap<>();
		final List<String> rolesList = person.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		claims.put("roles", rolesList);
		claims.put("userId", person.getId());
		claims.put("firstName", person.getFirstName());
		claims.put("lastName", person.getLastName());
		return claims;
	}

	private @NotNull Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key()).build()
				.parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}

	@NotNull
	public String getBearerToken(@NotNull AccountDto accountDto) throws UserPrincipalNotFoundException {
		log.info(" * Generating bearer token for {}", accountDto.getEmail());
		Person person;
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			person = userDetailsService.loadUserByUsername(accountDto.getEmail());
		} else {
			person = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		if (person == null) {
			throw new UserPrincipalNotFoundException(" ! Unable to retrieve user's principals");
		}

		return "Bearer ".concat(
				generateToken(person, Duration.ofMillis(10 * 1000)));
	}
}

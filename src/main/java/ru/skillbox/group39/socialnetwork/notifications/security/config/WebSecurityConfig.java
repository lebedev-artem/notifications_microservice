package ru.skillbox.group39.socialnetwork.notifications.security.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.skillbox.group39.socialnetwork.notifications.security.jwt.AuthEntryPointJwt;
import ru.skillbox.group39.socialnetwork.notifications.security.jwt.AuthTokenFilter;
import ru.skillbox.group39.socialnetwork.notifications.security.jwt.JwtUtils;
import ru.skillbox.group39.socialnetwork.notifications.security.service.UserDetailsServiceImpl;

/**
 * @author Artem Lebedev | 15/09/2023 - 20:49 <p>
 * Person person = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();<p>
 * person.getUsername()<p>
 * persoon.getPassword()<p>
 * person.getAuthorities()<p>
 */

@PropertySource("secrets.properties")
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class WebSecurityConfig {

	private final UserDetailsServiceImpl userDetailsService;

	private final JwtUtils jwtUtils;

	private final AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter(jwtUtils, userDetailsService);
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(@NotNull AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests()
				.antMatchers("/api/v1/notifications/swagger-ui/**",
						"/api/v1/notifications/h2-console/**",
						"/api/v1/notifications/actuator/**",
						"/swagger-ui/**",
						"/api/v1/swagger-ui/**",
						"/v3/api-docs/**").permitAll()
				.anyRequest().authenticated();
		http.authenticationProvider(authenticationProvider());
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}

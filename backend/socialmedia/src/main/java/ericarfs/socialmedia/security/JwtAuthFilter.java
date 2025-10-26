package ericarfs.socialmedia.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	private final UserDetailsService userDetailsService;

	public JwtAuthFilter(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	private static final List<String> EXCLUDED_PATHS = List.of(
			"/auth/token",
			"/auth/register",
			"/auth/token/refresh");

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();

		return EXCLUDED_PATHS.stream().anyMatch(path::contains);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);

		try {
			Claims claims = JwtUtil.extractClaims(token);
			String username = claims.getSubject();
			String role = claims.get("role", String.class);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userdetails = userDetailsService.loadUserByUsername(username);

				if (JwtUtil.validateToken(token)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userdetails,
							null,
							Collections.singletonList(new SimpleGrantedAuthority(role)));

					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}

			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException ex) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Access token has expired.");
			response.getWriter().flush();

		} catch (JwtException ex) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid or malformed JWT.");
			response.getWriter().flush();
		}
	}
}
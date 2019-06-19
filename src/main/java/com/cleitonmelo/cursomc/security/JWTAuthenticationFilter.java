package com.cleitonmelo.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cleitonmelo.cursomc.dto.CredenciaisDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtil jwtUtil;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			CredenciaisDTO credenciais = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					credenciais.getEmail(), credenciais.getSenha(), new ArrayList<>());

			Authentication auth = authenticationManager.authenticate(authenticationToken);

			return auth;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain, Authentication auth) throws IOException, ServletException {

		String username = ((UserSS) auth.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(username);
		response.addHeader("Authorization", "Bearer " + token);

	}

	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException exception) throws IOException, ServletException {

			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().append(json());

		}

		private String json() {
			long date = new Date().getTime();
			return "{\"timestamp\": " + date + ", "
					+ "\"status\": 401, "
					+ "\"error\": \"Não autorizado\", "
					+ "\"message\": \"Email ou senha inválidos\", "
					+ "\"path\": \"/login\"}";
		}

	}

}
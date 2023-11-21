package com.webdevelopment.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.webdevelopment.data.vo.v1.security.AccountCredentialsVO;
import com.webdevelopment.data.vo.v1.security.TokenVO;
import com.webdevelopment.repositories.UserRepository;
import com.webdevelopment.security.jwt.JwtTokenProvider;

@Service
public class AuthServices {

	private JwtTokenProvider tokenProvider;
	private AuthenticationManager authenticationManager;
	private UserRepository repository;

	public AuthServices(JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager,
			UserRepository repository) {
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
		this.repository = repository;
	}

	public ResponseEntity<TokenVO> signin(AccountCredentialsVO data) {

		try {
			var username = data.getUsername();
			var password = data.getPassword();

			UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(username,
					password);

			authenticationManager.authenticate(usernamePassword);

			var user = repository.findByUsername(username);
			var tokenResponse = new TokenVO();

			if (user != null) {
				tokenResponse = this.tokenProvider.createAccessToken(username, user.getRoles());

			} else {
				throw new UsernameNotFoundException("Username " + username + " not found!");
			}

			return ResponseEntity.ok(tokenResponse);

		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username/password supplied!");
		}
	}

	public ResponseEntity<TokenVO> refreshToken(String username, String refreshToken) {
		var user = repository.findByUsername(username);
		var tokenResponse = new TokenVO();

		if (user != null) {
			tokenResponse = this.tokenProvider.refreshToken(refreshToken);

		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
		return ResponseEntity.ok(tokenResponse);
	}
}

package com.webdevelopment.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.webdevelopment.data.vo.v1.security.TokenVO;
import com.webdevelopment.exceptions.InvalidJwtAuthenticationException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1h

	private UserDetailsService userDetailsService;

	private Algorithm algorithm = null;

	@Autowired
	public JwtTokenProvider(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@PostConstruct
	protected void init() {
//		Pega o que foi definido na secret e encripta, depois define novamente na variável.
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}

	public TokenVO createAccessToken(String username, List<String> roles) {

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);
		var accessToken = getAccessToken(username, roles, now, validity);
		var refreshToken = getRefreshToken(username, roles, now);

		return new TokenVO(username, true, now, validity, accessToken, refreshToken);
	}

	private String getAccessToken(String username, List<String> roles, Date now, Date validity) {

		String issuerURL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

		return JWT.create().withClaim("roles", roles).withIssuedAt(now).withIssuer(issuerURL).withSubject(username)
				.withExpiresAt(validity).sign(algorithm).strip();

//		return JWT.create().withIssuer("Agrix").withSubject(person.getUsername())
//		.withExpiresAt(generateExpirationDate()).sign(algorithm);
	}

	private String getRefreshToken(String username, List<String> roles, Date now) {

		Date validityRefreshToken = new Date(now.getTime() + (validityInMilliseconds * 3));

		return JWT.create().withClaim("roles", roles).withIssuedAt(now).withSubject(username)
				.withExpiresAt(validityRefreshToken).sign(algorithm).strip();

//		return JWT.create().withIssuer("Agrix").withSubject(person.getUsername())
//		.withExpiresAt(generateExpirationDate()).sign(algorithm);
	}

	public Authentication getAuthentication(String token) {

		DecodedJWT decodedJWT = DecodedToken(token);

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
		
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private DecodedJWT DecodedToken(String token) {

//		JWT.require(this.algorithm)
//        .withIssuer("Agrix")
//        .build()
//        .verify(token)
//        .getSubject();

		Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
		JWTVerifier verifier = JWT.require(alg).build();
		DecodedJWT decodedJWT = verifier.verify(token);
		return decodedJWT;
	}

	public String resolveToken(HttpServletRequest request) {

		String bearerToken = request.getHeader("Authorization");

		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.replace("Bearer ", "");
		}
		return null;
	}

	public Boolean validateToken(String token) {

		DecodedJWT decodedJWT = DecodedToken(token);

		try {

			if (decodedJWT.getExpiresAt().before(new Date())) {
				return false;
			}

			return true;

		} catch (Exception e) {
			throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
		}

	}

}

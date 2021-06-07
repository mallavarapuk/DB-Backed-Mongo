package com.db.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	MongoOperations mongoOperations;

	@Autowired
	HttpServletRequest requestObject;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;
		String insertQuery = "";
		String api = "";
		String loginId = "";
		String tokenIpAdress = "";
		String apiIpAdress = "";

		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the
		// Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				Claims claims = Jwts.parser().setSigningKey(username).parseClaimsJws(jwtToken).getBody();
				loginId = (String) claims.get("loginId");
				tokenIpAdress = (String) claims.get("ipAddress");
				// System.out.println("tokenIpAdress: " + claims.get("ipAddress"));
				// api = request.getRequestURL().toString();
				api = request.getMethod().toString() + " " + request.getRequestURL().toString();

				apiIpAdress = request.getHeader("X-FORWARDED-FOR");
				if (apiIpAdress == null || "".equals(apiIpAdress)) {
					apiIpAdress = request.getRemoteAddr();
				}

			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				Map userLogObject = new HashMap();
				userLogObject.put("UserName", loginId);
				userLogObject.put("TokenIpAddress", tokenIpAdress);
				userLogObject.put("ApiIpAddress", apiIpAdress);
				userLogObject.put("Api", api);

				mongoOperations.save(userLogObject, "tblUserLogV2");

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the Spring Security
				// Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

	public Map checkToken() {

		Map mainMap = new HashMap();
		mainMap.put("success", false);
		mainMap.put("message", "Token is invalid!");
		String requestTokenHeader = "";
		String username = null;

		String api = "";
		String loginId = "";

		String tokenIpAdress = "";

		String requestToken = requestObject.getHeader("Authorization");

		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the
		// Token
		if (requestToken != null && requestToken.startsWith("Bearer ")) {

			requestToken = requestToken.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(requestToken);
				Claims claims = Jwts.parser().setSigningKey(username).parseClaimsJws(requestToken).getBody();
				loginId = (String) claims.get("loginId");
				tokenIpAdress = (String) claims.get("ipAddress");

				mainMap.put("success", true);
				mainMap.put("message", "Token is valid");

			}

			catch (MalformedJwtException e) {
				System.out.println("JWT Token not valid");
			} catch (PrematureJwtException e) {
				System.out.println("JWT Token was pre matured");
			} catch (SignatureException e) {
				System.out.println("JWT Token verification is failed");
			} catch (UnsupportedJwtException e) {
				System.out.println("JWT Token not supported for this application");
			}

			catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		return mainMap;

	}
}

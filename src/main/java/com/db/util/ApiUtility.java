package com.db.util;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiUtility {

	@Value("${jwt.secret}")
	private String username;

	@Value("${local_url}")
	private String local_url;

	public String generateToken(String uname, String ipAddress) {

		// String uri = "http://localhost:8080/authenticate";
		String token = "";
		String uri = local_url + "/authenticate";
		RestTemplate restTemplate = new RestTemplate();
		String user = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		try {
			JSONObject jsonCredentials = new JSONObject();
			// jsonCredentials.put("password", "password");
			jsonCredentials.put("username", username);
			jsonCredentials.put("loginId", uname);
			jsonCredentials.put("ipAddress", ipAddress);
			HttpEntity<String> entityCredentials = new HttpEntity<String>(jsonCredentials.toString(), httpHeaders);
			ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entityCredentials,
					String.class);
			if (responseEntity != null) {
				user = responseEntity.getBody();
			}

			JSONObject json = new JSONObject(user);
			if (json.has("token")) {
				token = (String) json.get("token");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return token;
	}

}

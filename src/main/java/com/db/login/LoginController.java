package com.db.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LoginController {

	@Autowired
	private LoginService loginService;

	@PostMapping("/login")
	public Map userLogin(@RequestBody Login login) {
		Map response = new HashMap();
		response = loginService.userLogin(login);
		return response;

	}

	

}

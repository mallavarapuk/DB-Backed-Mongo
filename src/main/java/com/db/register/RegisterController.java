package com.db.register;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class RegisterController {

	@Autowired
	private RegisterService registerService;

	@PostMapping("/register")
	public Map createUser(@RequestBody Register register) {
		Map response = registerService.createUser(register);
		return response;

	}

	@PutMapping("/register")
	public Map updateUser(@RequestBody Register register) {
		Map response = registerService.updateUser(register);
		return response;

	}

	@GetMapping("/districts/{stateName}")
	public Map getDistricts(@PathVariable(value = "stateName") String stateName) {
		Map response = registerService.getDistricts(stateName);
		return response;

	}

	@GetMapping("/sub-districts/{districtCode}")
	public Map getSubDistricts(@PathVariable(value = "districtCode") String districtCode) {
		Map response = registerService.getSubDistricts(districtCode);
		return response;

	}

	@GetMapping("/states")
	public Map getStates() {
		Map response = registerService.getStates();
		return response;

	}

	@GetMapping("/insert")
	public Map insertStates() {
		Map response = registerService.getDistrictData();
		return response;

	}

	@GetMapping("/user-exist/{username}")
	@CrossOrigin
	public boolean isUserNameExists(@PathVariable("username") String username) {
		System.out.println("Hello");
		boolean result = registerService.isUserNameExists(username);
		return result;
	}

	@GetMapping("/email-exist/{email}")
	@CrossOrigin
	public boolean isEmailExists(@PathVariable("email") String email) {
		boolean result = registerService.isEmailExists(email);
		return result;
	}

	@PostMapping("/search-donors")
	public Map searchDonors(@RequestBody Register register) {
		Map response = registerService.searchDonors(register);
		return response;

	}

}

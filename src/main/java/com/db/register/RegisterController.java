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
	@CrossOrigin
	public Map createUser(@RequestBody Register register) {
		Map response = registerService.createUser(register);
		return response;

	}

	@PutMapping("/register")
	@CrossOrigin
	public Map updateUser(@RequestBody Register register) {
		Map response = registerService.updateUser(register);
		return response;

	}

	@GetMapping("/districts/{stateName}")
	@CrossOrigin
	public Map getDistricts(@PathVariable(value = "stateName") String stateName) {
		Map response = registerService.getDistricts(stateName);
		return response;

	}

	@GetMapping("/sub-districts/{districtCode}")
	@CrossOrigin
	public Map getSubDistricts(@PathVariable(value = "districtCode") String districtCode) {
		Map response = registerService.getSubDistricts(districtCode);
		return response;

	}

	@GetMapping("/states")
	@CrossOrigin
	public Map getStates() {
		Map response = registerService.getStates();
		return response;

	}

	@GetMapping("/blood-group")
	@CrossOrigin
	public Map getBloodGroups() {
		Map response = registerService.getBloodGroups();
		return response;

	}

	@GetMapping("/insert")
	@CrossOrigin
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
	@CrossOrigin
	public Map searchDonors(@RequestBody Register register) {
		Map response = registerService.searchDonors(register);
		return response;

	}

}

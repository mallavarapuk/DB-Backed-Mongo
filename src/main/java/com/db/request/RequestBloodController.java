
package com.db.request;

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
public class RequestBloodController {

	@Autowired
	private RequestBloodService requestBloodService;

	@PostMapping("/request-blood")
	public Map createRequestForBlood(@RequestBody RequestBlood requestBlood) {
		Map response = requestBloodService.createRequestForBlood(requestBlood);
		return response;
	}

	@GetMapping("/request-blood/{id}")
	public Map getRequestedDetails(@PathVariable("id") String uniqueCode) {
		Map response = requestBloodService.getRequestedDetails(uniqueCode);
		return response;
	}

	@PutMapping("/request-blood")
	public Map updatedRequestedDetails(@RequestBody RequestBlood requestBlood) {
		Map response = requestBloodService.updatedRequestedDetails(requestBlood);
		return response;
	}

}

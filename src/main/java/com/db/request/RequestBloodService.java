package com.db.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.db.register.RegisterService;
import com.db.util.DataSourceDataProvider;
import com.mongodb.client.result.UpdateResult;

@Service
@SuppressWarnings({ "rawtypes", "unchecked", "unused", "static-access" })
public class RequestBloodService {

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private DataSourceDataProvider dataSourceDataProvider;

	@Autowired
	private RegisterService registerService;

	public Map createRequestForBlood(RequestBlood requestBlood) {
		Map response = new HashMap();
		List<Map> results = new ArrayList<Map>();
		response.put("success", false);
		response.put("data", results);
		response.put("message", "Unable to connect database");

		String bloodGroup = requestBlood.getBloodGroup();
		String stateName = requestBlood.getStateName();
		String districtName = requestBlood.getDistrictName();
		String cityName = requestBlood.getCityName();
		String phoneNumber = requestBlood.getPhoneNumber();
		String emailId = requestBlood.getEmailId();
		String hospitalName = requestBlood.getHospitalName();
		String unitsOfBlood = requestBlood.getUnitsOfBlood();
		String description = requestBlood.getDescription();
		String lastDate = requestBlood.getLastDate();
		String fullName = requestBlood.getFullName();
		String gender = requestBlood.getGender();

		try {
			String uniqueCode = dataSourceDataProvider.getAlphaNumericRandomKey(16);

			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("uniqueCode").is(uniqueCode)));

			int randomCount = 1;
			while (randomCount > 0) {
				randomCount = mongoOperations.find(query, Map.class, "tblRequests").size();
				if (randomCount == 0) {
					break;
				} else {
					uniqueCode = dataSourceDataProvider.getAlphaNumericRandomKey(16);
				}
			}
			String disName = registerService.getDistrictName(districtName);
			requestBlood.setDistrictName(disName);
			requestBlood.setUniqueCode(uniqueCode);
			requestBlood.setStatus("Active");
			RequestBlood result = mongoOperations.save(requestBlood, "tblRequests");
			response.put("success", true);
			response.put("data", result);
			response.put("message", "Your request has been successfully sent.");

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Unable to process this request right now!");
		}

		return response;
	}

	public Map getRequestedDetails(String uniqueCode) {

		Map response = new HashMap();
		Map result = new HashMap();

		response.put("success", false);
		response.put("data", result);
		response.put("message", "Unable to connect database");

		try {

			if (uniqueCode == null || uniqueCode.length() != 16) {
				response.put("message", "Please give valid unique code");
				return response;
			}

			Query query = new Query();
			query.addCriteria(Criteria.where("uniqueCode").is(uniqueCode));
			result = mongoOperations.findOne(query, Map.class, "tblRequests");
			String disCode = registerService.getDistrictCode(result.get("districtName").toString());
			result.put("districtName", disCode);
			if (result != null && result.size() > 0) {
				response.put("success", true);
				response.put("data", result);
				response.put("message", "Successfully retrived requested details");

			} else {
				response.put("message", "Sorry! No requests for your given Unique Code");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Unable to process this request right now!");
		}

		return response;
	}

	public Map updatedRequestedDetails(RequestBlood requestBlood) {
		Map response = new HashMap();
		List<Map> results = new ArrayList<Map>();
		response.put("success", false);
		response.put("message", "Unable to connect database");

		String bloodGroup = requestBlood.getBloodGroup();
		String stateName = requestBlood.getStateName();
		String districtName = requestBlood.getDistrictName();
		String cityName = requestBlood.getCityName();
		String phoneNumber = requestBlood.getPhoneNumber();
		String emailId = requestBlood.getEmailId();
		String hospitalName = requestBlood.getHospitalName();
		String unitsOfBlood = requestBlood.getUnitsOfBlood();
		String description = requestBlood.getDescription();
		String lastDate = requestBlood.getLastDate();
		String fullName = requestBlood.getFullName();
		String gender = requestBlood.getGender();
		String uniqueCode = requestBlood.getUniqueCode();

		try {

			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("uniqueCode").is(uniqueCode)));
			Map requesteDetails = mongoOperations.findOne(query, Map.class, "tblRequests");

			if (requesteDetails == null || requesteDetails.size() <= 0) {
				response.put("message", "Sorry! Details are not updated");
				return response;
			}

			String disName = registerService.getDistrictName(districtName);
			requestBlood.setDistrictName(disName);

			Update update = new Update();
			update.set("bloodGroup", bloodGroup);
			update.set("stateName", stateName);
			update.set("districtName", disName);
			update.set("cityName", cityName);
			update.set("phoneNumber", phoneNumber);
			update.set("emailId", emailId);
			update.set("hospitalName", hospitalName);
			update.set("unitsOfBlood", unitsOfBlood);
			update.set("description", description);
			update.set("lastDate", lastDate);
			update.set("gender", gender);
			update.set("fullName", fullName);

			UpdateResult result = mongoOperations.updateMulti(query, update, "tblRequests");
			response.put("data", result);
			response.put("success", true);
			response.put("message", "Your details updated successfully");

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Unable to process this request right now!");
		}

		return response;
	}

}

package com.db.register;

import static com.mongodb.client.model.Filters.eq;

import java.util.List;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@Service
@SuppressWarnings({ "unchecked", "rawtypes", "unused", "static-access" })
public class RegisterService {

	@Autowired
	private RegisterRepository registerRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MongoOperations mongoOperations;

	public Map createUser(Register register) {
		List finalList = new ArrayList();
		Map response = new HashMap();
		response.put("data", finalList);
		response.put("success", false);
		response.put("message", "Unable to connect database");

		String fullName = register.getFullName();
		String userName = register.getUserName();
		String emailId = register.getEmailId();
		String bloodGroup = register.getBloodGroup();
		String stateName = register.getState();
		String districtName = register.getDistrict();
		String cityName = register.getCity();
		String availabilty = register.getAvailability();
		String password = register.getPassword();
		String confirmPassword = register.getConfirmPassword();

		try {

			if (fullName == null || fullName.trim().equalsIgnoreCase("")) {
				response.put("message", "Please give your Fullname");
				return response;
			}

			if (userName == null || userName.trim().equalsIgnoreCase("")) {
				response.put("message", "Please give your Username");
				return response;
			}

			if (emailId == null || emailId.trim().equalsIgnoreCase("")) {
				response.put("message", "Please give your Email Id");
				return response;
			}

			if (bloodGroup == null || bloodGroup.trim().equalsIgnoreCase("")) {
				response.put("message", "Please give your Blood Group");
				return response;
			}

			if (stateName == null || stateName.trim().equalsIgnoreCase("")) {
				response.put("message", "Please give your State Name");
				return response;
			}

			if (districtName == null || districtName.trim().equalsIgnoreCase("")) {
				response.put("message", "Please give your District Name");
				return response;
			}

			if (cityName == null || cityName.trim().equalsIgnoreCase("")) {
				response.put("message", "Please give your City Name");
				return response;
			}

			if (availabilty == null || availabilty.trim().equalsIgnoreCase("")) {
				response.put("message", "Please give your Availabilty");
				return response;
			}

			if (password == null || password.trim().equalsIgnoreCase("")) {
				response.put("message", "Please give your Password");
				return response;
			}

			if (confirmPassword == null || confirmPassword.trim().equalsIgnoreCase("")) {
				response.put("message", "Please give your Confirm Password");
				return response;
			}

			if (!password.equalsIgnoreCase(confirmPassword)) {
				response.put("message", "Password sholud be match!");
				return response;
			}

			register.setConfirmPassword(null);
			Register responses = registerRepository.save(register);
			response.put("data", responses);
			response.put("success", true);
			response.put("message", "Successfully registered!");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Unable to process this request right now!");
		}
		return response;
	}

	public Map updateUser(Register register) {
		Map res = new HashMap();
		register.setDistrict("Srikakulam");
		Register response = registerRepository.save(register);
		res.put("data", response);
		return res;
	}

	public Map getDistricts(String stateName) {
		List finalList = new ArrayList();
		Map response = new HashMap();
		response.put("data", finalList);
		response.put("success", false);
		response.put("message", "Unable to connect database");
		Map districtsMap = new HashMap();

		try {
			Document query = new Document("StateName", stateName);
			List<Map<String, Object>> results = new ArrayList();
			mongoOperations.getCollection("tblLKStatesAndDistricts").find(query).into(results);
			for (Map<String, Object> row : results) {
				districtsMap = new HashMap();
				districtsMap.put("DistrictName", row.get("DistrictName"));
				districtsMap.put("DistrictCode", row.get("DistrictCode"));
				finalList.add(districtsMap);

			}

			if (finalList.size() > 0) {
				response.put("data", finalList);
				response.put("success", true);
				response.put("message", "successfully retrived the districts list");
			} else {
				response.put("message", "Sorry! No districts found.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Unable to process this request right now!");
		}

		return response;
	}

	public Map getSubDistricts(String districtCode) {
		List finalList = new ArrayList();
		Map response = new HashMap();
		response.put("data", finalList);
		response.put("success", false);
		response.put("message", "Unable to connect database");
		Map districtsMap = new HashMap();

		try {
			Document query = new Document("DistrictCode", districtCode);
			List<Map<String, Object>> results = new ArrayList();
			mongoOperations.getCollection("tblLKDistrictsAndSubDistricts").find(query).into(results);
			for (Map<String, Object> row : results) {
				districtsMap = new HashMap();
				districtsMap.put("SubDistrictName", row.get("SubDistrictName"));
				finalList.add(districtsMap);

			}

			if (finalList.size() > 0) {
				response.put("data", finalList);
				response.put("success", true);
				response.put("message", "successfully retrived the sub districts list");
			} else {
				response.put("message", "Sorry! No records found.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Unable to process this request right now!");
		}

		return response;
	}

	public Map getStates() {
		List finalList = new ArrayList();
		Map response = new HashMap();
		response.put("data", finalList);
		response.put("success", false);
		response.put("message", "Unable to connect database");
		Map statesMap = new HashMap();

		try {
			Document query = new Document();
			List<String> results = new ArrayList();
			mongoOperations.getCollection("tblLKStatesAndDistricts").distinct("StateName", query, String.class)
					.into(results);
			for (String stateName : results) {
				statesMap = new HashMap();
				if (stateName != null && !stateName.equalsIgnoreCase("")) {
					statesMap.put("stateName", stateName);
					finalList.add(statesMap);
				}
			}

			if (finalList.size() > 0) {
				response.put("data", finalList);
				response.put("success", true);
				response.put("message", "successfully retrived the states list");

			} else {
				response.put("message", "Sorry! No states found.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Unable to process this request right now!");
		}

		return response;
	}

	public String getDistrictName(String districtCode) {
		String finalList = "";
		try {
			MongoCollection<Document> collection = mongoOperations.getCollection("tblLKStatesAndDistricts");
			MongoCursor<Document> myDoc = collection.find(eq("DistrictCode", districtCode)).iterator();
			while (myDoc.hasNext()) {
				finalList = (String) myDoc.next().get("DistrictName");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return finalList;
	}

	public String getDistrictCode(String districtName) {
		String finalList = "";
		System.out.println(districtName);
		try {
			MongoCollection<Document> collection = mongoOperations.getCollection("tblLKStatesAndDistricts");
			MongoCursor<Document> myDoc = collection.find(eq("DistrictName", districtName)).iterator();
			while (myDoc.hasNext()) {
				finalList = (String) myDoc.next().get("DistrictCode");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return finalList;
	}

	public Map getBloodGroups() {
		List finalList = new ArrayList();
		Map response = new HashMap();
		response.put("data", finalList);
		response.put("success", false);
		response.put("message", "Unable to connect database");
		Map statesMap = new HashMap();

		try {
			Document query = new Document();
			List<String> results = new ArrayList();
			mongoOperations.getCollection("tblLKBloodGroup").distinct("bloodGroup", query, String.class).into(results);
			for (String bloodGroup : results) {
				statesMap = new HashMap();
				if (bloodGroup != null && !bloodGroup.equalsIgnoreCase("")) {
					statesMap.put("bloodGroup", bloodGroup);
					finalList.add(statesMap);
				}
			}

			if (finalList.size() > 0) {
				response.put("data", finalList);
				response.put("success", true);
				response.put("message", "successfully retrived the list of Blood Groups");

			} else {
				response.put("message", "Sorry! No states found.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Unable to process this request right now!");
		}

		return response;
	}

	public Map getDistrictData() {
		Map res = new HashMap();
		ArrayList data = new ArrayList();
		MongoCollection<Document> collection = mongoOperations.getCollection("tblLKStatesAndDistricts");
		MongoCursor<Document> myDoc = collection.find(eq("StateName", "ANDHRA PRADESH")).iterator();
//		System.out.println(myDoc.toJson());
		while (myDoc.hasNext()) {
			data.add(myDoc.next().get("DistrictName"));
		}

		res.put("data", data);
		return res;
	}

	public String addStateAndDistrictData() {

		return "success";
	}

	public Map insertStates() {
		Map res = new HashMap();

		JSONParser jsonParser = new JSONParser();

		try {
			// Read JSON file
			Object obj = new JSONParser().parse(new FileReader("D://in.json"));

			JSONObject employeeList = (JSONObject) obj;
			JSONArray employeeObject = (JSONArray) employeeList.get("states");
			for (int i = 0; i < employeeObject.size(); i++) {
				res = new HashMap();
				JSONObject jSONObject = (JSONObject) employeeObject.get(i);
				String DistrictCode = (String) jSONObject.get("DistrictCode");
				String DistrictName = (String) jSONObject.get("DistrictName");
				String StateCode = (String) jSONObject.get("StateCode");
				String StateName = (String) jSONObject.get("StateName");
				res.put("DistrictCode", DistrictCode);
				res.put("DistrictName", DistrictName);
				res.put("StateCode", StateCode);
				res.put("StateName", StateName);
				mongoOperations.save(res, "tblLKStatesAndDistricts");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public Map insertDistricts() {
		Map res = new HashMap();

		JSONParser jsonParser = new JSONParser();

		try {
			// Read JSON file
			Object obj = new JSONParser().parse(new FileReader("D://allSubDistrictofIndia.json"));

			JSONObject employeeList = (JSONObject) obj;
			JSONArray employeeObject = (JSONArray) employeeList.get("districts");
			for (int i = 0; i < employeeObject.size(); i++) {
				res = new HashMap();
				JSONObject jSONObject = (JSONObject) employeeObject.get(i);
				String SubDistrictCode = (String) jSONObject.get("SubDistrictCode");
				String SubDistrictName = (String) jSONObject.get("SubDistrictName");
				String DistrictCode = (String) jSONObject.get("DistrictCode");
				res.put("SubDistrictCode", SubDistrictCode);
				res.put("SubDistrictName", SubDistrictName);
				res.put("DistrictCode", DistrictCode);
				mongoOperations.save(res, "tblLKDistrictsAndSubDistricts");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public boolean isUserNameExists(String username) {
		boolean result = false;
		try {
			int size = 0;
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("userName").regex("^" + username + "$", "i")));
			size = mongoOperations.find(query, Map.class, "tblUsers").size();
			System.out.println(size);
			if (size == 1) {
				result = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in isUserNameExists method");
		}

		return result;
	}

	public boolean isEmailExists(String emailId) {
		boolean result = false;
		try {
			int size = 0;
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("emailId").regex("^" + emailId + "$", "i")));
			size = mongoOperations.find(query, Map.class, "tblUsers").size();
			if (size == 1) {
				result = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in isEmailExists method");
		}

		return result;
	}

	public Map searchDonors(Register register) {
		List finalList = new ArrayList();
		Map response = new HashMap();
		response.put("data", finalList);
		response.put("success", false);
		response.put("message", "Unable to connect database");
		try {

			String bloodGroup = register.getBloodGroup();
			String stateName = register.getState();
			String districtName = register.getDistrict();
			String cityName = register.getCity();
			
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("bloodGroup").is(bloodGroup),
					Criteria.where("state").is(stateName), Criteria.where("district").is(districtName)));

			if (cityName != null && cityName.length() > 0 && !cityName.equalsIgnoreCase("All")) {
				query.addCriteria(Criteria.where("city").is(cityName));
			}

			List<Map> donerDetails = mongoOperations.find(query, Map.class, "tblUsers");
			if (donerDetails != null && donerDetails.size() > 0) {
				response.put("data", donerDetails);
				response.put("success", true);
				response.put("message", "Successfully retrived the donors details");
			} else {
				response.put("message", "Sorry! No donors found!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in searchDonors method");
		}

		return response;
	}

}

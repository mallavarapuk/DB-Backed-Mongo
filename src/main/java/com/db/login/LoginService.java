package com.db.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.db.util.ApiUtility;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LoginService {

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ApiUtility apiUtility;

	public Map userLogin(Login login) {
		System.out.println("Methos Started");
		Map response = new HashMap();
		List<Map> results = new ArrayList<Map>();
		response.put("success", false);
		response.put("data", results);
		response.put("message", "Unable to connect database");
		String loginId = login.getLoginId();
		String password = login.getPassword();

		try {

			if (loginId == null || loginId.length() == 0) {
				response.put("message", "Please give username (or) email (or) mobile no");
				return response;
			}

			if (password == null || password.length() == 0) {
				response.put("message", "Please give password!");
				return response;
			}

			Criteria criteria = new Criteria();
			criteria.orOperator(Criteria.where("userName").is(loginId), Criteria.where("emailId").is(loginId),
					Criteria.where("mobileNo").is(loginId));
			Query querys = new Query(criteria);
			results = mongoTemplate.find(querys, Map.class, "tblUsers");

			Criteria passwordCriteria = new Criteria();
			passwordCriteria.orOperator(Criteria.where("userName").is(loginId), Criteria.where("emailId").is(loginId),
					Criteria.where("mobileNo").is(loginId)).and("password").is(password);
			Query passwordQuery = new Query(passwordCriteria);

			Map passwordResults = mongoTemplate.findOne(passwordQuery, Map.class, "tblUsers");

			if (results == null || results.size() == 0) {
				response.put("message", "Please give valid username (or) email (or) mobile no");
				return response;
			}

			if (passwordResults == null || passwordResults.size() == 0) {
				response.put("message", "Password is incorrect!");
				return response;
			}

			if (results != null && passwordResults != null) {

				String ip = request.getHeader("X-FORWARDED-FOR");
				if (ip == null || "".equals(ip)) {
					ip = request.getRemoteAddr();
				}
				String uname = "";
				if (results.get(0).containsKey("userName"))
					uname = (String) results.get(0).get("loginId");

				String token = apiUtility.generateToken(uname, ip);

				if (token != null && !"".equals(token)) {
					response.put("token", token);
					response.put("success", true);
					response.put("data", results);
					response.put("message", "successfully login");
				} else {
					response.put("message", "Sorry! Something went wrong. Please try again later");
				}

			} else {
				response.put("message", "Login failed!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Unable to process this request right now!");
		}

		return response;

	}

}

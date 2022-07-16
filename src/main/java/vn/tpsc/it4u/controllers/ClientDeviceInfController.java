package vn.tpsc.it4u.controllers;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import vn.tpsc.it4u.models.SystemConfig;
import vn.tpsc.it4u.services.ClientDeviceInfService;
import vn.tpsc.it4u.services.SitesNameService;
import vn.tpsc.it4u.services.SystemConfigService;
import vn.tpsc.it4u.utils.ApiRequest;
import vn.tpsc.it4u.utils.ApiResponseUtils;

@RestController
@RequestMapping("${app.api.version}")
public class ClientDeviceInfController {

	@Value("${app.ucrm.url}")
	public String urlUCRM;

	@Value("${app.ucrm.x_auth_app_Key}")
	public String authAppKey;

	@Autowired
	ClientDeviceInfService clientDeviceInfService;

	@Autowired
	SystemConfigService systemConfigService;

	@Autowired
	SitesNameService sitesNameservice;

	@Autowired
	ApiResponseUtils apiResponse;

	@Operation(description = "Create client device information")
	@PostMapping("/it4u/clientDeviceInf")
	public ResponseEntity<?> createClientDeviceInf(@RequestBody String data, Locale locale) {
		JSONObject convertDataToJson = new JSONObject(data);
		clientDeviceInfService.createClientDeviceInf(convertDataToJson);
		return ResponseEntity.ok(apiResponse.success(200, locale));
	}

	@Operation(description = "Get contract ucrm by sitename")
	@PostMapping("/it4u/getContractBySitename")
	public String getContractBySitename(@RequestBody String data) {
		JSONObject convertDataToJson = new JSONObject(data);
		JSONObject infoContract = new JSONObject();
		ApiRequest apiRequest = new ApiRequest();
		String getSiteName = convertDataToJson.getString("sitename");
		String getInfoClient = apiRequest.getRequestUCRM(
				urlUCRM + "/clients?customAttributeKey=sitename&customAttributeValue=" + getSiteName, authAppKey);
		JSONArray convertInfoClient = new JSONArray(getInfoClient);
		if (convertInfoClient.length() > 0) {
			JSONObject infoClient = (JSONObject) convertInfoClient.get(0);
			JSONArray getCustomAttributes = infoClient.getJSONArray("attributes");
			for (int j = 0; j < getCustomAttributes.length(); j++) {
				JSONObject getItemAtt = (JSONObject) getCustomAttributes.get(j);
				if (getItemAtt.getString("key").equals("sHPNg")) {
					infoContract.put("contracts", getItemAtt.getString("value"));
				}
				if (getItemAtt.getString("key").equals("iNThoINgILiNHLPT")) {
					infoContract.put("phone", getItemAtt.getString("value"));
				}
				if (getItemAtt.getString("key").equals("sitename")) {
					String getServicePlan = apiRequest
							.getRequestUCRM(urlUCRM + "/clients/" + infoClient.getInt("id") + "/services", authAppKey);
					JSONArray convertServicePlan = new JSONArray(getServicePlan);
					if (convertServicePlan.length() > 0) {
						JSONObject servicePlan = (JSONObject) convertServicePlan.get(0);
						infoContract.put("servicePlan", servicePlan.getString("name"));
					} else {
						infoContract.put("servicePlan", "");
					}

					try {
						infoContract.put("companyName", infoClient.getString("companyName"));
					} catch (Exception e) {
						infoContract.put("companyName",
								infoClient.getString("firstName") + " " + infoClient.getString("lastName"));
					}
					infoContract.put("street", infoClient.getString("street1"));
					infoContract.put("customId", infoClient.getString("userIdent"));
				}
			}
		}
		return infoContract.toString();
	}

	@Operation(description = "Upload client device infomation")
	@PostMapping("/it4u/upload/ClientDeviceInf")
	public ResponseEntity<?> uploadClientDeviceInf(@RequestBody String data, Locale locale) {
		JSONArray convertDataToJson = new JSONArray(data);
		ApiRequest apiRequest = new ApiRequest();
		SystemConfig systemConfig = systemConfigService.findSystemConfig();
		authAppKey = systemConfig.getTokenUcrm();
		for (int i = 0; i < convertDataToJson.length(); i++) {
			JSONObject getData = (JSONObject) convertDataToJson.get(i);
			JSONObject infoContract = new JSONObject();
			String getSiteName = getData.getString("siteName");
			String getInfoClient = apiRequest.getRequestUCRM(
					urlUCRM + "/clients?customAttributeKey=sitename&customAttributeValue=" + getSiteName, authAppKey);
			JSONArray convertInfoClient = new JSONArray(getInfoClient);
			if (convertInfoClient.length() > 0) {
				JSONObject infoClient = (JSONObject) convertInfoClient.get(0);
				JSONArray getCustomAttributes = infoClient.getJSONArray("attributes");
				for (int j = 0; j < getCustomAttributes.length(); j++) {
					JSONObject getItemAtt = (JSONObject) getCustomAttributes.get(j);
					if (getItemAtt.getString("key").equals("sHPNg")) {
						infoContract.put("contracts", getItemAtt.getString("value"));
					}
					if (getItemAtt.getString("key").equals("iNThoINgILiNHLPT")) {
						infoContract.put("phone", getItemAtt.getString("value"));
					}
					if (getItemAtt.getString("key").equals("sitename")) {
						String getServicePlan = apiRequest
								.getRequestUCRM(urlUCRM + "/clients/" + infoClient.getInt("id") + "/services",
										authAppKey);
						JSONArray convertServicePlan = new JSONArray(getServicePlan);
						if (convertServicePlan.length() > 0) {
							JSONObject servicePlan = (JSONObject) convertServicePlan.get(0);
							infoContract.put("servicePlan", servicePlan.getString("name"));
						} else {
							infoContract.put("servicePlan", "");
						}

						try {
							infoContract.put("companyName", infoClient.getString("companyName"));
						} catch (Exception e) {
							infoContract.put("companyName",
									infoClient.getString("firstName") + " " + infoClient.getString("lastName"));
						}
						infoContract.put("street", infoClient.getString("street1"));
						infoContract.put("customId", infoClient.getString("userIdent"));
						clientDeviceInfService.uploadClientDeviceInf(getData, infoContract);
					}
				}
			}
		}
		return ResponseEntity.ok(apiResponse.success(200, locale));
	}

	@Operation(description = "Update client device information by id")
	@PutMapping("/it4u/clientDeviceInf/{id}")
	public ResponseEntity<?> updateClientDeviceInfById(@PathVariable(value = "id") long id,
			@RequestBody String data, Locale locale) {
		JSONObject convertDataToJson = new JSONObject(data);
		clientDeviceInfService.updateClientDeviceInf(id, convertDataToJson);
		return ResponseEntity.ok(apiResponse.success(200, locale));
	}

	@Operation(description = "Get all client device information")
	@GetMapping("/it4u/clientDeviceInf")
	public String getClientDeviceInf() {
		JSONArray getData = new JSONArray(clientDeviceInfService.findAllClientDeviceInf());
		return getData.toString();
	}

	@Operation(description = "Get limit client device information")
	@GetMapping("/it4u/clientDeviceInfLimit/{limit}")
	public String getLimitClientDeviceInf(@PathVariable(value = "limit") String limit) {
		if (limit.equals("all")) {
			JSONArray getData = new JSONArray(clientDeviceInfService.findAllClientDeviceInf());
			return getData.toString();
		} else {
			long num = Long.parseLong(limit);
			JSONArray getData = new JSONArray(clientDeviceInfService.findLimitClientDeviceInf(num));
			return getData.toString();
		}
	}

	@Operation(description = "Search client device info by param")
	@PostMapping("/it4u/searchClientDeviceInf")
	public String searchClientDeviceInf(@RequestBody String data) {
		JSONObject convertDataToJson = new JSONObject(data);
		String content = convertDataToJson.getString("content");
		JSONArray getData = new JSONArray(clientDeviceInfService.findAllByParam(content));
		return getData.toString();
	}

	@Operation(description = "Delete client device information by id")
	@DeleteMapping("/it4u/clientDeviceInf/{id}")
	public ResponseEntity<?> deleteClientDeviceInf(@PathVariable(value = "id") Long id, Locale locale) {
		clientDeviceInfService.deleteClientDeviceInf(id);
		return ResponseEntity.ok(apiResponse.success(200, locale));
	}

	@Operation(description = "Get site name by id")
	@GetMapping("/it4u/sitenames")
	public String getSitenames() {
		JSONArray getSitename = new JSONArray(sitesNameservice.findAll());
		return getSitename.toString();
	}

	@Operation(description = "Get site name by id")
	@GetMapping("/it4u/sitename/{id}")
	public String getSitenameById(@PathVariable(value = "id") Long id) {
		JSONObject getSitename = new JSONObject(sitesNameservice.findById(id));
		return getSitename.toString();
	}

	@Operation(description = "Create site name")
	@PostMapping("/it4u/sitename")
	public ResponseEntity<?> postSitename(@RequestBody String data, Locale locale) {
		JSONObject convertDataToJson = new JSONObject(data);
		sitesNameservice.createSitename(convertDataToJson);
		return ResponseEntity.ok(apiResponse.success(200, locale));
	}

	@Operation(description = "Update site name by id")
	@PutMapping("/it4u/sitename/{id}")
	public ResponseEntity<?> putSitenameById(@RequestBody String data, @PathVariable(value = "id") Long id,
			Locale locale) {
		JSONObject convertDataToJson = new JSONObject(data);
		sitesNameservice.updateSitename(id, convertDataToJson);
		return ResponseEntity.ok(apiResponse.success(1001, locale));
	}

	@Operation(description = "Delete site name by id")
	@DeleteMapping("/it4u/sitename/{id}")
	public ResponseEntity<?> deleteSitenameById(@PathVariable(value = "id") Long id, Locale locale) {
		sitesNameservice.deleteById(id);
		return ResponseEntity.ok(apiResponse.success(200, locale));
	}
}

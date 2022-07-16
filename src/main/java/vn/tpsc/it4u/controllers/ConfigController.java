package vn.tpsc.it4u.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.tpsc.it4u.models.SitesName;
import vn.tpsc.it4u.models.SystemConfig;
import vn.tpsc.it4u.payloads.SystemConfigSummary;
import vn.tpsc.it4u.repository.SitesNameRepository;
import vn.tpsc.it4u.security.CurrentUser;
import vn.tpsc.it4u.security.CustomUserDetails;
import vn.tpsc.it4u.services.ConfigTokenService;
import vn.tpsc.it4u.services.SystemConfigService;
import vn.tpsc.it4u.utils.ApiRequest;
import vn.tpsc.it4u.utils.ApiResponseUtils;

@RestController
@Slf4j
@RequestMapping("${app.api.version}")
public class ConfigController {
	@Value("${app.ubnt.url}")
	private String urlIt4u;

	@Value("${app.ubnt.hotspot_template}")
	private String hotspotTemplate;

	@Value("${app.ubnt.ssid_template}")
	private String ssidTemplate;

	@Value("${app.ubnt.csrf_token}")
	private String csrfToken;

	@Value("${app.ubnt.unifises}")
	private String unifises;

	@Value("${app.ubnt.username}")
	private String username;

	@Value("${app.ubnt.password}")
	private String password;

	@Autowired
	private SystemConfigService systemConfigService;

	@Autowired
	private ConfigTokenService configTokenService;

	@Autowired
	private SitesNameRepository sitesNameRepository;

	@Autowired
	ApiResponseUtils apiResponse;

	@Operation(description = "Create vlan group")
	@PostMapping("it4u/{id}/wlangroup")
	public String createVLGroup(@PathVariable(value = "id") String id, @CurrentUser CustomUserDetails currentUser,
			@RequestBody String postData) {
		JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
		JSONArray getBody = getCookies.getJSONArray("body");
		JSONObject body = (JSONObject) getBody.get(0);
		csrfToken = body.getString("csrfToken");
		unifises = body.getString("unifises");
		ApiRequest apiRequest = new ApiRequest();
		try {
			String createVlanGroup = apiRequest.postRequestApi(urlIt4u, "/s/" + id + "/rest/wlangroup", csrfToken,
					unifises, postData);
			JSONObject getData = new JSONObject(createVlanGroup);
			JSONArray convertDataToJson = getData.getJSONArray("data");
			JSONArray result = new JSONArray();
			for (int i = 0; i < convertDataToJson.length(); i++) {
				JSONObject getItemData = (JSONObject) convertDataToJson.get(i);
				String wlanGroupName = getItemData.getString("name");
				if (!wlanGroupName.toLowerCase().equals("off")) {
					result.put(getItemData);
				}
			}
			log.info(currentUser.getUsername() + " - Post: it4u/" + id + "/wlangroup");
			return result.toString();
		} catch (Exception e) {
			getCookies();
			String createVlanGroup = apiRequest.postRequestApi(urlIt4u, "/s/" + id + "/rest/wlangroup", csrfToken,
					unifises, postData);
			JSONObject getData = new JSONObject(createVlanGroup);
			JSONArray convertDataToJson = getData.getJSONArray("data");
			JSONArray result = new JSONArray();
			for (int i = 0; i < convertDataToJson.length(); i++) {
				JSONObject getItemData = (JSONObject) convertDataToJson.get(i);
				String wlanGroupName = getItemData.getString("name");
				if (!wlanGroupName.toLowerCase().equals("off")) {
					result.put(getItemData);
				}
			}
			log.info(currentUser.getUsername() + " - Post: it4u/" + id + "/wlangroup");
			return result.toString();
		}
	}

	@Operation(description = "Get vlan group")
	@GetMapping("it4u/{id}/wlangroup")
	public String getVLGroup(@PathVariable(value = "id") String id, @CurrentUser CustomUserDetails currentUser) {
		JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
		JSONArray getBody = getCookies.getJSONArray("body");
		JSONObject body = (JSONObject) getBody.get(0);
		csrfToken = body.getString("csrfToken");
		unifises = body.getString("unifises");
		ApiRequest apiRequest = new ApiRequest();
		try {
			String getData = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlangroup", csrfToken, unifises);
			JSONObject convertData = new JSONObject(getData);
			JSONArray convertDataToJson = convertData.getJSONArray("data");
			JSONArray result = new JSONArray();
			for (int i = 0; i < convertDataToJson.length(); i++) {
				JSONObject getItemData = (JSONObject) convertDataToJson.get(i);
				String wlanGroupName = getItemData.getString("name");
				if (!wlanGroupName.toLowerCase().equals("off")) {
					result.put(getItemData);
				}
			}
			log.info(currentUser.getUsername() + " - Get: it4u/" + id + "/wlangroup");
			return result.toString();
		} catch (Exception e) {
			getCookies();
			String getData = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlangroup", csrfToken, unifises);
			JSONObject convertData = new JSONObject(getData);
			JSONArray convertDataToJson = convertData.getJSONArray("data");
			JSONArray result = new JSONArray();
			for (int i = 0; i < convertDataToJson.length(); i++) {
				JSONObject getItemData = (JSONObject) convertDataToJson.get(i);
				String wlanGroupName = getItemData.getString("name");
				if (!wlanGroupName.toLowerCase().equals("off")) {
					result.put(getItemData);
				}
			}
			log.info(currentUser.getUsername() + " - Get: it4u/" + id + "/wlangroup");
			return result.toString();
		}

	}

	@Operation(description = "Create SSID to vlan group")
	@PostMapping("it4u/{id}/wlanconf")
	public String createSSID(@PathVariable(value = "id") String id,
			@CurrentUser CustomUserDetails currentUser, @RequestBody String postData) {
		ApiRequest apiRequest = new ApiRequest();
		JSONArray dataVlanGr = new JSONArray();
		try {
			String getVlanGr = apiRequest.getRequestApi(urlIt4u, ssidTemplate, csrfToken, unifises);
			JSONObject convertGetDataVlanGr = new JSONObject(getVlanGr);
			dataVlanGr = convertGetDataVlanGr.getJSONArray("data");
		} catch (Exception e) {
			getCookies();
			String getVlanGr = apiRequest.getRequestApi(urlIt4u, ssidTemplate, csrfToken, unifises);
			JSONObject convertGetDataVlanGr = new JSONObject(getVlanGr);
			dataVlanGr = convertGetDataVlanGr.getJSONArray("data");
		}
		JSONObject postDataJson = new JSONObject(postData);
		JSONObject getItemVlanGr = (JSONObject) dataVlanGr.get(0);
		getItemVlanGr.put("enabled", postDataJson.getBoolean("enabled"));
		getItemVlanGr.put("is_guest", postDataJson.getBoolean("is_guest"));
		getItemVlanGr.put("usergroup_id", postDataJson.getString("usergroup_id"));
		getItemVlanGr.put("name", postDataJson.getString("name"));
		getItemVlanGr.put("wlangroup_id", postDataJson.getString("wlangroup_id"));
		getItemVlanGr.put("security", postDataJson.getString("security"));
		if (postDataJson.getString("security").equals("wpapsk")) {
			getItemVlanGr.put("x_passphrase", postDataJson.getString("x_passphrase"));
		}
		String createVlanGroup = apiRequest.postRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken, unifises,
				getItemVlanGr.toString());
		JSONObject convertData = new JSONObject(createVlanGroup);
		JSONArray data = convertData.getJSONArray("data");
		log.info(currentUser.getUsername() + " - Post: it4u/" + id + "/wlanconf");
		return data.toString();
	}

	@Operation(description = "Get wlanconf to vlan group")
	@GetMapping("it4u/{id}/wlanconf/{wlan}")
	public String getWlanconf(@PathVariable(value = "id") String id,
			@CurrentUser CustomUserDetails currentUser, @PathVariable(value = "wlan") String wlan) {
		List<String> result = new ArrayList<>();
		ApiRequest apiRequest = new ApiRequest();
		JSONArray data = new JSONArray();
		try {
			String createVlanGroup = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken,
					unifises);
			JSONObject convertData = new JSONObject(createVlanGroup);
			data = convertData.getJSONArray("data");
		} catch (Exception e) {
			getCookies();
			String createVlanGroup = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken,
					unifises);
			JSONObject convertData = new JSONObject(createVlanGroup);
			data = convertData.getJSONArray("data");
		}
		for (int i = 0; i < data.length(); i++) {
			JSONObject getItem = (JSONObject) data.get(i);
			String ssidName = getItem.getString("name").toLowerCase();
			if (wlan.equals(getItem.getString("wlangroup_id")) && !ssidName.equals("tps tpcoms")) {
				result.add(getItem.toString());
			}
		}
		log.info(currentUser.getUsername() + " - Post: it4u/" + id + "/wlanconf/" + wlan);
		return result.toString();
	}

	@Operation(description = "Get SSID to vlan group")
	@GetMapping("it4u/{id}/essid/{wlan}")
	public String getSSID(@PathVariable(value = "id") String id, @CurrentUser CustomUserDetails currentUser,
			@PathVariable(value = "wlan") String wlan) {
		JSONObject result = new JSONObject();
		ApiRequest apiRequest = new ApiRequest();
		JSONArray data = new JSONArray();
		// List<String> schedule = new ArrayList<>();
		try {
			String createVlanGroup = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken,
					unifises);
			JSONObject convertData = new JSONObject(createVlanGroup);
			data = convertData.getJSONArray("data");
		} catch (Exception e) {
			getCookies();
			String createVlanGroup = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken,
					unifises);
			JSONObject convertData = new JSONObject(createVlanGroup);
			data = convertData.getJSONArray("data");
		}
		for (int i = 0; i < data.length(); i++) {
			JSONObject getItem = (JSONObject) data.get(i);
			if (wlan.equals(getItem.getString("_id"))) {
				result = (JSONObject) data.get(i);
				break;
			}
		}
		result.put("status_monday", false);
		result.put("status_tuesday", false);
		result.put("status_wednessday", false);
		result.put("status_thursday", false);
		result.put("status_friday", false);
		result.put("status_saturday", false);
		result.put("status_sunday", false);
		JSONArray getSchedule = result.getJSONArray("schedule");
		try {
			if (result.getBoolean("schedule_enabled")) {
				List<String> schedule = Arrays.asList(getSchedule.toString());
				// String[] textArray = schedule.get(0);
				for (int i = 0; i < schedule.size(); i++) {
					final String getItem = schedule.get(i);
					final String regex = "\\[(.*)\\]";
					String getDay = "";
					final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
					final Matcher matcher = pattern.matcher(getItem);

					while (matcher.find()) {
						String getSch = matcher.group(1);
						String[] listSchedule = getSch.split(",");
						for (int k = 0; k < listSchedule.length; k++) {
							List<Integer> listTimeDay = new ArrayList<Integer>();
							String getItemSch = listSchedule[k];
							String item = getItemSch.substring(1, 14);
							String[] parts = item.split("\\|");
							String getTimeSch = parts[1];
							getDay = parts[0];
							String[] partTime = getTimeSch.split("-");
							for (int j = 0; j < partTime.length; j++) {
								int index = 0;
								int time = 0;
								String getItemTime = partTime[j];
								while (index < getItemTime.length()) {
									String getTime = getItemTime.substring(index,
											Math.min(index + 2, getItemTime.length()));
									if (index == 0) {
										time = Integer.parseInt(getTime) * 60;
									} else {
										time = time + Integer.parseInt(getTime);
									}
									index = index + 2;
								}
								listTimeDay.add(time / 15);

							}
							switch (getDay) {
								case "mon":
									result.put("status_monday", true);
									result.put("mon_schedule", item);
									result.put("valid_monday", listTimeDay);
									break;
								case "tue":
									result.put("tue_schedule", item);
									result.put("status_tueday", true);
									result.put("valid_tueday", listTimeDay);
									break;
								case "wed":
									result.put("wed_schedule", item);
									result.put("status_wedday", true);
									result.put("valid_wedday", listTimeDay);
									break;
								case "thu":
									result.put("thu_schedule", item);
									result.put("status_thuday", true);
									result.put("valid_thuday", listTimeDay);
									break;
								case "fri":
									result.put("fri_schedule", item);
									result.put("status_friday", true);
									result.put("valid_friday", listTimeDay);
									break;
								case "sat":
									result.put("sat_schedule", item);
									result.put("status_satday", true);
									result.put("valid_satday", listTimeDay);
									break;
								case "sun":
									result.put("sun_schedule", item);
									result.put("status_sunday", true);
									result.put("valid_sunday", listTimeDay);
									break;

								default:
									break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			result.put("schedule_enabled", false);
		}
		log.info(currentUser.getUsername() + " - Post: it4u/" + id + "/essid/" + wlan);
		return result.toString();
	}

	@Operation(description = "Put SSID to vlan group")
	@PutMapping("it4u/{id}/wlanconf/{ssid}")
	public String putSSID(@PathVariable(value = "id") String id,
			@CurrentUser CustomUserDetails currentUser, @PathVariable(value = "ssid") String ssid,
			@RequestBody String postData) {
		String name = "";
		List<String> schedule = new ArrayList<>();
		ApiRequest apiRequest = new ApiRequest();
		JSONArray data = new JSONArray();
		JSONArray getVlanGroupJson = new JSONArray();
		JSONObject convertDataPost = new JSONObject(postData);
		String putData = "";
		try {
			String getVlanGroup = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken,
					unifises);
			JSONObject convertVlanGroup = new JSONObject(getVlanGroup);
			getVlanGroupJson = convertVlanGroup.getJSONArray("data");
		} catch (Exception e) {
			getCookies();
			String getVlanGroup = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken, unifises);
			JSONObject convertVlanGroup = new JSONObject(getVlanGroup);
			getVlanGroupJson = convertVlanGroup.getJSONArray("data");
		}
		if (convertDataPost.getBoolean("schedule_enabled")) {
			if (!convertDataPost.getString("monday").equals("disable")) {
				schedule.add(convertDataPost.getString("monday"));
			}
			if (!convertDataPost.getString("tuesday").equals("disable")) {
				schedule.add(convertDataPost.getString("tuesday"));
			}
			if (!convertDataPost.getString("wednessday").equals("disable")) {
				schedule.add(convertDataPost.getString("wednessday"));
			}
			if (!convertDataPost.getString("thursday").equals("disable")) {
				schedule.add(convertDataPost.getString("thursday"));
			}
			if (!convertDataPost.getString("friday").equals("disable")) {
				schedule.add(convertDataPost.getString("friday"));
			}
			if (!convertDataPost.getString("saturday").equals("disable")) {
				schedule.add(convertDataPost.getString("saturday"));
			}
			if (!convertDataPost.getString("sunday").equals("disable")) {
				schedule.add(convertDataPost.getString("sunday"));
			}
		}
		for (int i = 0; i < getVlanGroupJson.length(); i++) {
			JSONObject getItem = (JSONObject) getVlanGroupJson.get(i);
			if (ssid.equals(getItem.getString("_id"))) {
				name = getItem.getString("name");
				break;
			}
		}
		for (int i = 0; i < getVlanGroupJson.length(); i++) {
			JSONObject getItem = (JSONObject) getVlanGroupJson.get(i);
			if (name.equals(getItem.getString("name"))) {
				String ssidName = getItem.getString("_id");
				getItem.put("enabled", convertDataPost.getBoolean("enabled"));
				getItem.put("name", convertDataPost.getString("name"));
				getItem.put("vlan_enabled", convertDataPost.getBoolean("vlan_enabled"));
				getItem.put("schedule", schedule);
				try {
					getItem.put("vlan", convertDataPost.getString("vlan"));
				} catch (Exception e) {
				}
				try {
					getItem.put("is_guest", convertDataPost.getBoolean("is_guest"));
				} catch (Exception e) {
				}
				if (convertDataPost.getString("security").equals("wpapsk")) {
					getItem.put("security", "wpapsk");
					getItem.put("x_passphrase", convertDataPost.getString("x_passphrase"));
					putData = getItem.toString();
				} else {
					getItem.put("security", "open");
					putData = getItem.toString();
				}
				String editEssid = apiRequest.putRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf/" + ssidName,
						csrfToken, unifises, putData);
				JSONObject convertData = new JSONObject(editEssid);
				data = convertData.getJSONArray("data");
			}
		}
		log.info(currentUser.getUsername() + " - Put: it4u/" + id + "/wlanconf/" + ssid);
		return data.toString();
	}

	@Operation(description = "Assign vlan group to APs")
	@PostMapping("it4u/{id}/group/device")
	public String assignVG(@PathVariable(value = "id") String id, @CurrentUser CustomUserDetails currentUser) {
		ApiRequest apiRequest = new ApiRequest();
		JSONObject createPostData = new JSONObject();
		List<String> itemDevicesArray = new ArrayList<>();
		String getDeviceId = "";
		String highSpeed = "";
		String lowSpeed = "";
		JSONArray getDataDevices = new JSONArray();
		try {
			getDeviceId = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/stat/device", csrfToken, unifises);
			JSONObject convertData = new JSONObject(getDeviceId);
			getDataDevices = convertData.getJSONArray("data");
		} catch (Exception e) {
			getCookies();
			getDeviceId = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/stat/device", csrfToken, unifises);
			JSONObject convertData = new JSONObject(getDeviceId);
			getDataDevices = convertData.getJSONArray("data");
		}
		for (int i = 0; i < getDataDevices.length(); i++) {
			JSONObject itemDevice = (JSONObject) getDataDevices.get(i);
			itemDevicesArray.add(itemDevice.getString("device_id"));
		}
		String getVlanGr = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlangroup", csrfToken, unifises);
		JSONObject convertDataVlanGr = new JSONObject(getVlanGr);
		JSONArray dataVlanGr = convertDataVlanGr.getJSONArray("data");
		for (int i = 0; i < dataVlanGr.length(); i++) {
			JSONObject getItem = (JSONObject) dataVlanGr.get(i);
			if (getItem.getString("name").equals("5G")) {
				highSpeed = getItem.getString("_id");
			}
			if (getItem.getString("name").equals("2G")) {
				lowSpeed = getItem.getString("_id");
			}
		}
		String postData = "{\"radio_table\":[{\"radio\":\"ng\",\"wlangroup_id\":\"" + lowSpeed
				+ "\",\"tx_power_mode\":\"medium\"},{\"radio\":\"na\",\"wlangroup_id\":\"" + highSpeed
				+ "\"}],\"wlan_overrides\":[],\"bandsteering_mode\":\"prefer_5g\"}";
		createPostData.put("id", itemDevicesArray);
		createPostData.put("data", postData);
		String putData = "{\"id\":" + itemDevicesArray + ", \"data\":" + postData + "}";
		JSONObject convertPutData = new JSONObject(putData);
		String assignVG = apiRequest.putRequestApi(urlIt4u, "/s/" + id + "/group/device", csrfToken, unifises,
				convertPutData.toString());
		log.info(currentUser.getUsername() + " - Post: it4u/" + id + "/group/device");
		return assignVG;
	}

	@Operation(description = "Create a sitename")
	@PostMapping("it4u/{id}/sitemgr")
	public String createSitename(@PathVariable(value = "id") String id,
			@CurrentUser CustomUserDetails currentUser, @RequestBody String postData) {
		JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
		JSONArray getBody = getCookies.getJSONArray("body");
		JSONObject body = (JSONObject) getBody.get(0);
		csrfToken = body.getString("csrfToken");
		unifises = body.getString("unifises");
		ApiRequest apiRequest = new ApiRequest();
		JSONArray getData = new JSONArray();
		try {
			String createSitename = apiRequest.postRequestApi(urlIt4u, "/s/bkhsurc7/cmd/sitemgr", csrfToken, unifises,
					postData);
			JSONObject convertData = new JSONObject(createSitename);
			getData = convertData.getJSONArray("data");
		} catch (Exception e) {
			getCookies();
			String createSitename = apiRequest.postRequestApi(urlIt4u, "/s/bkhsurc7/cmd/sitemgr", csrfToken, unifises,
					postData);
			JSONObject convertData = new JSONObject(createSitename);
			getData = convertData.getJSONArray("data");
		}
		JSONObject data = (JSONObject) getData.get(0);
		final SitesName addSitename = new SitesName(data.getString("desc"), data.getString("name"));
		sitesNameRepository.save(addSitename);
		log.info(currentUser.getUsername() + " - Post: it4u/" + id + "/sitemgr");
		return data.toString();
	}

	@Operation(description = "Get a hotspot")
	@GetMapping("it4u/{id}/hotspot")
	public String getHotspot(@PathVariable(value = "id") String id, @CurrentUser CustomUserDetails currentUser) {
		JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
		JSONArray getBody = getCookies.getJSONArray("body");
		JSONObject body = (JSONObject) getBody.get(0);
		csrfToken = body.getString("csrfToken");
		unifises = body.getString("unifises");
		ApiRequest apiRequest = new ApiRequest();
		int positionPortal = 0;
		JSONArray getDataSetting = new JSONArray();
		try {
			String getSetting = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/get/setting", csrfToken, unifises);
			JSONObject convertDataSetting = new JSONObject(getSetting);
			getDataSetting = convertDataSetting.getJSONArray("data");
		} catch (Exception e) {
			getCookies();
			String getSetting = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/get/setting", csrfToken, unifises);
			JSONObject convertDataSetting = new JSONObject(getSetting);
			getDataSetting = convertDataSetting.getJSONArray("data");
		}
		for (int i = 0; i < getDataSetting.length(); i++) {
			JSONObject getItem = (JSONObject) getDataSetting.get(i);
			if (getItem.getString("key").equals("guest_access")) {
				positionPortal = i;
				break;
			}
		}
		JSONObject getData = (JSONObject) getDataSetting.get(positionPortal);
		log.info(currentUser.getUsername() + " - Get: it4u/" + id + "/hotspot");
		return getData.toString();
	}

	@Operation(description = "Create a hotspot")
	@PostMapping("it4u/{id}/hotspot")
	public String createHotspot(@PathVariable(value = "id") String id,
			@CurrentUser CustomUserDetails currentUser, @RequestBody String data) {
		JSONObject postData = new JSONObject(data);
		JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
		JSONArray getBody = getCookies.getJSONArray("body");
		JSONObject body = (JSONObject) getBody.get(0);
		csrfToken = body.getString("csrfToken");
		unifises = body.getString("unifises");
		ApiRequest apiRequest = new ApiRequest();
		JSONArray getDataFromPostData = new JSONArray();
		try {
			String getPostData = apiRequest.getRequestApi(urlIt4u, hotspotTemplate, csrfToken, unifises);
			JSONObject convertPostData = new JSONObject(getPostData);
			getDataFromPostData = convertPostData.getJSONArray("data");
		} catch (Exception e) {
			getCookies();
			String getPostData = apiRequest.getRequestApi(urlIt4u, hotspotTemplate, csrfToken, unifises);
			JSONObject convertPostData = new JSONObject(getPostData);
			getDataFromPostData = convertPostData.getJSONArray("data");
		}

		// Boolean portal = true;
		int positionPortal = 0;
		int positionPortalSetting = 0;
		for (int i = 0; i < getDataFromPostData.length(); i++) {
			// JSONObject getItem = (JSONObject) getDataFromPostData.get(i);
			try {
				// portal = getItem.getBoolean("portal_enabled");
				positionPortalSetting = i;
			} catch (Exception e) {
				throw e;
			}
		}
		String getSetting = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/get/setting", csrfToken, unifises);
		JSONObject convertDataSetting = new JSONObject(getSetting);
		JSONArray getDataSetting = convertDataSetting.getJSONArray("data");
		for (int i = 0; i < getDataSetting.length(); i++) {
			JSONObject getItem = (JSONObject) getDataSetting.get(i);
			if (getItem.getString("key").equals("guest_access")) {
				positionPortal = i;
				break;
			}
		}
		JSONObject getDataId = (JSONObject) getDataSetting.get(positionPortal);
		String idSetting = getDataId.getString("_id");
		String idSite = getDataId.getString("site_id");
		JSONObject getDataHotspot = (JSONObject) getDataFromPostData.get(positionPortalSetting);
		getDataHotspot.put("_id", idSetting);
		getDataHotspot.put("site_id", idSite);
		getDataHotspot.put("redirect_url", postData.getString("redirect_url"));
		getDataHotspot.put("facebook_wifi_gw_name", postData.getString("facebook_wifi_gw_name"));
		switch (postData.getString("auth")) {
			case "none":
				getDataHotspot.put("auth", "none");
				break;
			case "hotspot":
				getDataHotspot.put("auth", "hotspot");
				break;
			case "password":
				getDataHotspot.put("auth", "password");
				getDataHotspot.put("x_password", postData.getString("x_password"));
				break;
			default:
				break;
		}
		String createHotspot = apiRequest.postRequestApi(urlIt4u, "/s/" + id + "/set/setting/guest_access/" + idSetting,
				csrfToken, unifises, getDataHotspot.toString());
		log.info(currentUser.getUsername() + " - Post: it4u/" + id + "/hotspot");
		return createHotspot.toString();
	}

	@Operation(description = "Get system config information")
	@GetMapping("/it4u/systemConfig")
	public String getSystemConfigInfo() {
		SystemConfig systemConfig = systemConfigService.findSystemConfig();
		JSONObject convertSystemConfig = new JSONObject(systemConfig);
		return convertSystemConfig.toString();
	}

	@Operation(description = "Update system config information")
	@PutMapping("/it4u/systemConfig")
	public ResponseEntity<?> updateSystemConfigInfo(@RequestBody SystemConfigSummary data, Locale locale) {
		systemConfigService.updateSystemConfig(data);
		return ResponseEntity.ok(apiResponse.success(1001, locale));
	}

	@Operation(description = "Add system config information")
	@PostMapping("/it4u/systemConfig")
	public ResponseEntity<?> createSystemConfigInfo(@RequestBody SystemConfigSummary data, Locale locale) {
		systemConfigService.createSystemConfig(data);
		return ResponseEntity.ok(apiResponse.success(200, locale));
	}

	public String getCookies() {
		ApiRequest apiRequest = new ApiRequest();
		SystemConfig systemConfig = systemConfigService.findSystemConfig();
		username = systemConfig.getUsernameUbnt();
		password = systemConfig.getPwUbnt();
		String dataPost = "{\"username\":\"" + username + "\",\"password\":\"" + password
				+ "\",\"remember\":\"true\",\"strict\":\"true\"}";
		String getCookies = apiRequest.postRequestIt4u(urlIt4u, "/login", dataPost);
		String[] arr = getCookies.split(";");
		String getToken = arr[0];
		String[] arrToken = getToken.split("=");
		csrfToken = arrToken[1];
		String getUnifise = arr[2];
		String[] arrUnifise = getUnifise.split("=");
		unifises = arrUnifise[1];
		try {
			return getCookies.toString();
		} catch (Exception e) {
			return e.toString();
		}
	}
}

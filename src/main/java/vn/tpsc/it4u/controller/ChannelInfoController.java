package vn.tpsc.it4u.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.modelmapper.ModelMapper;

import vn.tpsc.it4u.model.ChannelAttribute;
import vn.tpsc.it4u.model.ChannelDetail;
import vn.tpsc.it4u.model.ChannelName;
import vn.tpsc.it4u.model.ChannelValue;
import vn.tpsc.it4u.model.Contract;
import vn.tpsc.it4u.model.HistoryChannel;
import vn.tpsc.it4u.model.SystemConfig;
import vn.tpsc.it4u.payload.ChannelAttributeRequest;
import vn.tpsc.it4u.repository.ChannelAttributeRepository;
import vn.tpsc.it4u.repository.ChannelDetailRepository;
import vn.tpsc.it4u.repository.ChannelNameRepository;
import vn.tpsc.it4u.repository.ChannelValueRepository;
import vn.tpsc.it4u.repository.ContractRepository;
import vn.tpsc.it4u.repository.HistoryChannelRepository;
import vn.tpsc.it4u.util.ApiRequest;
import vn.tpsc.it4u.util.Calculator;
import vn.tpsc.it4u.service.ChannelInfoService;
import vn.tpsc.it4u.service.ConfigTokenService;
import vn.tpsc.it4u.service.SystemConfigService;
import vn.tpsc.it4u.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;

@RestController
@RequestMapping("${app.api.version}")
public class ChannelInfoController {
	@Value("${app.ucrm.url}")
	public String urlUCRM;

	@Value("${app.ubnt.url}")
	public String urlIt4u;

	@Value("${app.ucrm.x_auth_app_Key}")
	public String authAppKey;

	@Value("${app.ubnt.csrf_token}")
	public String csrfToken;

	@Value("${app.ubnt.unifises}")
	public String unifises;

	@Value("${app.firebase.senderId}")
	public String senderId;

	@Value("${app.firebase.getAccessToken}")
	public String getAccessToken;

	@Value("${app.firebase.url}")
	public String urlFirebase;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ChannelAttributeRepository channelAttributeRepository;

	@Autowired
	private HistoryChannelRepository historyChannelRepository;

	@Autowired
	private ChannelValueRepository channelValueRepository;

	@Autowired
	private ChannelNameRepository channelNameRepository;

	@Autowired
	private ChannelDetailRepository channelDetailRepository;

	@Autowired
	private ChannelInfoService channelInfoService;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	UserService userService;

	@Autowired
	ConfigTokenService configTokenService;

	@Autowired
	SystemConfigService systemConfigService;

	@Operation(description = "Get clients")
	@GetMapping("/it4u/clients")
	public String getAllClient() {
		ApiRequest apiRequest = new ApiRequest();
		/// clients/180/services
		JSONObject itemData = new JSONObject();
		List<String> result = new ArrayList<>();
		SystemConfig systemConfig = systemConfigService.findSystemConfig();
		authAppKey = systemConfig.getTokenUcrm();
		String getInfoClient = apiRequest.getRequestUCRM(urlUCRM + "/clients", authAppKey);
		JSONArray convertInfoClient = new JSONArray(getInfoClient);
		for (int i = 0; i < convertInfoClient.length(); i++) {
			JSONObject getItem = (JSONObject) convertInfoClient.get(i);
			itemData.put("id", getItem.getInt("id"));
			itemData.put("userIdent", getItem.getString("userIdent"));
			try {
				itemData.put("companyName", getItem.getString("companyName"));
			} catch (Exception e) {
				itemData.put("companyName", getItem.getString("firstName") + " " + getItem.getString("lastName"));
			}
			result.add(itemData.toString());
		}
		return result.toString();
	}

	@Operation(description = "Get MAC")
	@GetMapping("/it4u/info/mac")
	public String getInfoMAC() {
		List<String> result = new ArrayList<>();
		ApiRequest apiRequest = new ApiRequest();
		SystemConfig systemConfig = systemConfigService.findSystemConfig();
		authAppKey = systemConfig.getTokenUcrm();
		String getInfoClient = apiRequest.getRequestUCRM(urlUCRM + "/clients", authAppKey);
		JSONArray convertInfoClient = new JSONArray(getInfoClient);
		for (int i = 0; i < convertInfoClient.length(); i++) {
			String getCompanyName = "";
			JSONObject getItem = (JSONObject) convertInfoClient.get(i);
			List<String> listMac = new ArrayList<>();
			JSONObject itemData = new JSONObject();
			try {
				getCompanyName = getItem.getString("companyName");
			} catch (Exception e) {
				getCompanyName = getItem.getString("firstName") + " " + getItem.getString("lastName");
			}
			JSONArray getAttribute = getItem.getJSONArray("attributes");
			for (int j = 0; j < getAttribute.length(); j++) {
				JSONObject getItemAttribute = (JSONObject) getAttribute.get(j);
				if (getItemAttribute.getString("name").equals("AP1") && !getItemAttribute.getString("value").isEmpty())
					listMac.add(getItemAttribute.getString("value"));
				if (getItemAttribute.getString("name").equals("AP2") && !getItemAttribute.getString("value").isEmpty())
					listMac.add(getItemAttribute.getString("value"));
				if (getItemAttribute.getString("name").equals("AP3") && !getItemAttribute.getString("value").isEmpty())
					listMac.add(getItemAttribute.getString("value"));
				if (getItemAttribute.getString("name").equals("AP4") && !getItemAttribute.getString("value").isEmpty())
					listMac.add(getItemAttribute.getString("value"));
			}
			itemData.put(getCompanyName, listMac);
			result.add(itemData.toString());
		}
		return result.toString();
	}

	@Operation(description = "Get all custom id")
	@GetMapping("/it4u/customId")
	public String getAllCustomId() {
		ApiRequest apiRequest = new ApiRequest();
		JSONObject itemData = new JSONObject();
		List<String> result = new ArrayList<>();
		SystemConfig systemConfig = systemConfigService.findSystemConfig();
		authAppKey = systemConfig.getTokenUcrm();
		String getInfoClient = apiRequest.getRequestUCRM(urlUCRM + "/clients", authAppKey);
		JSONArray convertInfoClient = new JSONArray(getInfoClient);
		for (int i = 0; i < convertInfoClient.length(); i++) {
			JSONObject getItem = (JSONObject) convertInfoClient.get(i);
			itemData.put("id", getItem.getInt("id"));
			itemData.put("name", getItem.getString("userIdent"));
			result.add(itemData.toString());
		}
		return result.toString();
	}

	@Operation(description = "Get all client name")
	@GetMapping("/it4u/clientName")
	public String getAllClientName() {
		ApiRequest apiRequest = new ApiRequest();
		JSONObject itemData = new JSONObject();
		List<String> result = new ArrayList<>();
		SystemConfig systemConfig = systemConfigService.findSystemConfig();
		authAppKey = systemConfig.getTokenUcrm();
		String getInfoClient = apiRequest.getRequestUCRM(urlUCRM + "/clients", authAppKey);
		JSONArray convertInfoClient = new JSONArray(getInfoClient);
		for (int i = 0; i < convertInfoClient.length(); i++) {
			JSONObject getItem = (JSONObject) convertInfoClient.get(i);
			try {
				itemData.put("name", getItem.getString("companyName"));
			} catch (Exception e) {
				itemData.put("name",
						getItem.getString("firstName") + " " + getItem.getString("lastName"));
			}
			if (!result.contains(itemData.toString())) {
				result.add(itemData.toString());
			}
		}
		return result.toString();
	}

	@Operation(description = "Get clients")
	@GetMapping("/it4u/customer/{id}")
	public String getInfoCustomer(@PathVariable(value = "id") String userId) {
		ApiRequest apiRequest = new ApiRequest();
		/// clients/180/services
		JSONObject itemData = new JSONObject();
		SystemConfig systemConfig = systemConfigService.findSystemConfig();
		authAppKey = systemConfig.getTokenUcrm();
		String getInfoClient = apiRequest.getRequestUCRM(urlUCRM + "/clients/" + userId, authAppKey);
		String getServicePlan = apiRequest.getRequestUCRM(urlUCRM + "/clients/" + userId + "/services", authAppKey);
		JSONObject convertInfoClient = new JSONObject(getInfoClient);
		JSONArray convertServicePlan = new JSONArray(getServicePlan);
		JSONArray getAttributes = convertInfoClient.getJSONArray("attributes");
		for (int i = 0; i < getAttributes.length(); i++) {
			JSONObject getInfoAtt = (JSONObject) getAttributes.get(i);
			if (getInfoAtt.getString("key").equals("sHPNg")) {
				itemData.put("contracts", getInfoAtt.getString("value"));
			}
		}
		JSONObject servicePlan = (JSONObject) convertServicePlan.get(0);
		itemData.put("servicePlan", servicePlan.getString("name"));
		try {
			itemData.put("companyName", convertInfoClient.getString("companyName"));
		} catch (Exception e) {
			itemData.put("companyName",
					convertInfoClient.getString("firstName") + " " + convertInfoClient.getString("lastName"));
		}
		itemData.put("street", convertInfoClient.getString("street1"));
		return itemData.toString();
	}

	@Operation(description = "Post channel attribute")
	@PostMapping("/it4u/channel/attribute")
	public String PostChannelAttribute(@RequestBody final ChannelAttributeRequest postData) {
		ChannelValue getChannelValue = channelValueRepository.findByServicePack(postData.getChannelValue());
		ChannelAttribute getChannelAttribute = new ChannelAttribute(
				postData.getCustomer(),
				postData.getStatus(),
				postData.getVirtualNum(),
				postData.getUsernamePPPoE(),
				getChannelValue);
		channelAttributeRepository.save(getChannelAttribute);
		String result = getAllChannelAttribute();
		return result;
	}

	@Operation(description = "Post channel name")
	@PostMapping("/it4u/channel/name")
	public ResponseEntity<?> postChannelName(@RequestBody final ChannelName postData) {
		ChannelName createChannelName = new ChannelName(postData.getName());
		channelNameRepository.save(createChannelName);
		List<ChannelName> channelName = channelNameRepository.findAll();
		return ResponseEntity.ok(channelName);
	}

	@Operation(description = "Post channel value")
	@PostMapping("/it4u/channel/value")
	public ResponseEntity<?> postChannelValue(@RequestBody final ChannelValue postData) {
		JSONObject getChannelName = new JSONObject(postData.getChannelName());
		ChannelName channelName = channelNameRepository.findByName(getChannelName.getString("name"));
		ChannelValue createChannelValue = new ChannelValue(
				postData.getServicePack(),
				postData.getValue(),
				channelName);
		channelValueRepository.save(createChannelValue);
		List<ChannelValue> channelValue = channelValueRepository.findByChannelName(channelName);
		return ResponseEntity.ok(channelValue);
	}

	@Operation(description = "Get all channel attribute")
	@GetMapping("/it4u/channel/attribute.all")
	public String getAllChannelAttribute() {
		List<String> result = new ArrayList<>();
		JSONObject data = new JSONObject();
		JSONArray getChannelAttribute = new JSONArray(channelInfoService.findAll());
		for (int i = 0; i < getChannelAttribute.length(); i++) {
			JSONObject getItem = (JSONObject) getChannelAttribute.get(i);
			// if (!getItem.getString("status").equals("Online")) {
			try {
				JSONObject getChannelValue = getItem.getJSONObject("channelValue");
				JSONObject getChannelName = getChannelValue.getJSONObject("channelName");
				data.put("id", getItem.getLong("id"));
				data.put("name", getChannelName.getString("name"));
				data.put("servicePack", getChannelValue.getString("servicePack"));
				data.put("value", getChannelValue.getString("value"));
				data.put("status", getItem.getString("status"));
				data.put("customer", getItem.getString("customer"));
				data.put("virtualNum", getItem.getString("virtualNum"));
				data.put("usernamePPPoE", getItem.getString("usernamePPPoE"));
				result.add(data.toString());
			} catch (Exception e) {
				// }
			}
		}
		return result.toString();
	}

	@Operation(description = "Get channel attribute by status")
	@GetMapping("/it4u/channel/status.{status}")
	public String getChannelAttributeByStatus(@PathVariable("status") String status) {
		List<String> result = new ArrayList<>();
		JSONObject data = new JSONObject();
		JSONArray getChannelAttribute = new JSONArray(channelInfoService.findChannelAttributeByStatus(status));
		for (int i = 0; i < getChannelAttribute.length(); i++) {
			JSONObject getItem = (JSONObject) getChannelAttribute.get(i);
			// if (!getItem.getString("status").equals("Online")) {
			try {
				JSONObject getChannelValue = getItem.getJSONObject("channelValue");
				JSONObject getChannelName = getChannelValue.getJSONObject("channelName");
				data.put("id", getItem.getLong("id"));
				data.put("name", getChannelName.getString("name"));
				data.put("servicePack", getChannelValue.getString("servicePack"));
				data.put("value", getChannelValue.getString("value"));
				data.put("status", getItem.getString("status"));
				data.put("customer", getItem.getString("customer"));
				data.put("virtualNum", getItem.getString("virtualNum"));
				data.put("usernamePPPoE", getItem.getString("usernamePPPoE"));
				result.add(data.toString());
			} catch (Exception e) {
				// }
			}
		}
		return result.toString();
	}

	@Operation(description = "Search channel Attribute by date")
	@PostMapping("/it4u/channel/attribute.date")
	public String getChannelAttributeByDate(@RequestBody String data) {
		JSONObject convertDate = new JSONObject(data);
		List<String> result = new ArrayList<>();
		Calculator getCalculator = new Calculator();
		String fromDate = convertDate.getString("fromDate");
		String toDate = convertDate.getString("toDate");
		Timestamp convertFromDate = getCalculator.convertStringToTimestamp(fromDate);
		Timestamp convertToDate = getCalculator.convertStringToTimestamp(toDate);
		List<ChannelAttribute> listChannelAttribute = channelInfoService.findChannelAttributeByDate(convertFromDate,
				convertToDate);
		for (int i = 0; i < listChannelAttribute.size(); i++) {
			ChannelAttribute channelAttribute = listChannelAttribute.get(i);
			ChannelDetail channelDetail = channelDetailRepository.findByChannelAttribute(channelAttribute);
			try {
				JSONObject channelDetailJson = new JSONObject(channelDetail);
				result.add(channelDetailJson.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result.toString();
	}

	@Operation(description = "Get channel attribute by id")
	@GetMapping("/it4u/channel/attribute.{id}")
	public String getChannelAttributeById(@PathVariable("id") long id) {
		JSONObject data = new JSONObject();
		JSONObject getChannelAttribute = new JSONObject(channelAttributeRepository.findById(id));
		try {
			JSONObject getChannelValue = getChannelAttribute.getJSONObject("channelValue");
			JSONObject getChannelName = getChannelValue.getJSONObject("channelName");
			data.put("id", getChannelAttribute.getLong("id"));
			data.put("name", getChannelName.getString("name"));
			data.put("servicePack", getChannelValue.getString("servicePack"));
			data.put("value", getChannelValue.getString("value"));
			data.put("status", getChannelAttribute.getString("status"));
			data.put("customer", getChannelAttribute.getString("customer"));
			data.put("virtualNum", getChannelAttribute.getString("virtualNum"));
			data.put("usernamePPPoE", getChannelAttribute.getString("usernamePPPoE"));
		} catch (Exception e) {
		}
		return data.toString();
	}

	@Operation(description = "Put channel attribute by id")
	@PutMapping("/it4u/channel/attribute.{id}")
	public String putChannelAttributeById(@PathVariable("id") long id, @RequestBody String dataRequest) {
		JSONObject data = new JSONObject(dataRequest);
		channelInfoService.updateChannelAttribute(id, data);
		String result = getAllChannelAttribute();
		return result;
	}

	@Operation(description = "Get channel attribute by id")
	@DeleteMapping("/it4u/channel/attribute.{id}")
	public Boolean deleteChannelAttribute(@PathVariable(value = "id") Long id) {
		channelInfoService.deleteChannelAttribute(id);
		return true;
	}

	@Operation(description = "Get channel attribute by name")
	@GetMapping("/it4u/channel/attribute.all.{name}")
	public String getInfoChannelAttributeFromName(@PathVariable(value = "name") String name) {
		List<String> result = new ArrayList<>();
		JSONObject data = new JSONObject();
		JSONArray getChannelAttribute = new JSONArray(channelInfoService.findAll());
		for (int i = 0; i < getChannelAttribute.length(); i++) {
			JSONObject getItem = (JSONObject) getChannelAttribute.get(i);
			try {
				JSONObject getChannelValue = getItem.getJSONObject("channelValue");
				JSONObject getChannelName = getChannelValue.getJSONObject("channelName");
				String getStatus = getItem.getString("status");
				if (getChannelName.getString("name").equals(name) &&
						(!getStatus.equals("online"))) {
					if (!getStatus.equals("paused")) {
						data.put("name", getChannelName.getString("name"));
						data.put("servicePack", getChannelValue.getString("servicePack"));
						data.put("value", getChannelValue.getString("value"));
						data.put("status", getItem.getString("status"));
						data.put("customer", getItem.getString("customer"));
						data.put("virtualNum", getItem.getString("virtualNum"));
						data.put("usernamePPPoE", getItem.getString("usernamePPPoE"));
						if (!result.contains(data.toString())) {
							result.add(data.toString());
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return result.toString();
	}

	@Operation(description = "Get channel attribute to name")
	@GetMapping("/it4u/channel/value.all.{name}")
	public ResponseEntity<?> getInfoChannelValueFromName(@PathVariable(value = "name") String name) {
		ChannelName channelName = channelNameRepository.findByName(name);
		List<ChannelValue> channelValue = channelValueRepository.findByChannelName(channelName);
		return ResponseEntity.ok(channelValue);
	}

	@Operation(description = "Get channel attribute to name")
	@GetMapping("/it4u/channel.name")
	public ResponseEntity<?> getChannelName() {
		List<ChannelName> channelName = channelNameRepository.findAll();
		return ResponseEntity.ok(channelName);
	}

	@Operation(description = "Create a channel detail")
	@PostMapping("/it4u/channel/detail")
	public ResponseEntity<?> createChannelDetail(@RequestBody String data) {
		JSONObject getData = new JSONObject(data);
		try {
			if (channelDetailRepository.existsById(getData.getLong("id"))) {
				channelInfoService.updateInfoChannelDetail(getData.getLong("id"), getData);
				return ResponseEntity.ok(channelInfoService.findAllChannelDetail());
			}
		} catch (Exception e) {

		}
		HistoryChannel historyChannel = new HistoryChannel();
		if (channelAttributeRepository.existsIdByCondition(getData.getString("virtualNum"),
				getData.getString("usernamePPPoE"))) {
			if (!getData.getString("status").equals("offline")) {
				long idChannelAttribtue = channelAttributeRepository.getIdByCondition(getData.getString("virtualNum"),
						getData.getString("usernamePPPoE"));
				ChannelAttribute channelAttribute = channelAttributeRepository.findById(idChannelAttribtue);
				ChannelDetail channeDetail = channelDetailRepository.findByChannelAttribute(channelAttribute);
				Contract contract = channeDetail.getContract();
				historyChannel.setChannelAttribute(channelAttribute);
				historyChannel.setContract(contract);
				historyChannelRepository.save(historyChannel);
			}
		}
		ChannelDetail createChannelDetail = new ChannelDetail(
				getData.getString("routerType"),
				getData.getString("deviceStatus"),
				getData.getString("votesRequire"),
				getData.getString("ipType"),
				getData.getString("regionalEngineer"),
				getData.getLong("deployRequestDate"),
				getData.getLong("dateAcceptance"),
				getData.getString("ipAddress"),
				getData.getString("fees"));
		ChannelValue getChannelValue = channelValueRepository.findByServicePack(getData.getString("servicePack"));
		List<ChannelAttribute> getChannelAttribute = channelAttributeRepository.findByChannelValue(getChannelValue);
		JSONArray channelAttributeArr = new JSONArray(getChannelAttribute);
		for (int i = 0; i < channelAttributeArr.length(); i++) {
			JSONObject getItem = (JSONObject) channelAttributeArr.get(i);
			if (!getItem.getString("status").equals("online")) {
				ChannelAttribute channelAttribute = channelAttributeRepository.findById(getItem.getLong("id"));
				channelAttribute.setStatus("online");
				channelAttributeRepository.save(channelAttribute);
				createChannelDetail.setChannelAttribute(channelAttribute);
				break;
			}
		}
		if (contractRepository.existsByCustomId(getData.getString("customId"))) {
			Contract getContract = contractRepository.findByCustomId(getData.getString("customId"));
			createChannelDetail.setContract(getContract);
			channelDetailRepository.save(createChannelDetail);
			historyChannel.setContract(getContract);
			return ResponseEntity.ok(channelInfoService.findAllChannelDetail());
		} else {
			Contract createContract = new Contract(
					getData.getString("customId"),
					getData.getString("numContract"),
					getData.getString("clientName"),
					getData.getString("servicePlans"),
					getData.getString("street"),
					null);
			contractRepository.save(createContract);
			createChannelDetail.setContract(createContract);
			historyChannel.setContract(createContract);
			historyChannelRepository.save(historyChannel);
			channelDetailRepository.save(createChannelDetail);
			return ResponseEntity.ok(channelInfoService.findAllChannelDetail());
		}
	}

	@Operation(description = "Get all channel detail")
	@GetMapping("/it4u/channel/detail.all")
	public String getAllChannelDetail() {
		JSONArray getAllChannelDetail = new JSONArray(channelInfoService.findAllChannelDetail());
		return getAllChannelDetail.toString();
	}

	@Operation(description = "Import data")
	@PostMapping("/it4u/import/channel")
	public String importChannelDetail(@RequestBody String data) {
		String result = "";
		JSONArray convertData = new JSONArray(data);
		for (int i = 0; i < convertData.length(); i++) {
			JSONObject getData = (JSONObject) convertData.get(i);
			String status = getData.getString("status");
			Calculator getCalculator = new Calculator();
			HistoryChannel historyChannel = new HistoryChannel();
			ChannelDetail createChannelDetail = new ChannelDetail(
					getData.getString("routerType"),
					getData.getString("deviceStatus"),
					getData.getString("votesRequire"),
					getData.getString("ipType"),
					getData.getString("regionalEngineer"),
					getCalculator.ConvertStringToSecond(getData.getString("deployRequestDate")),
					getCalculator.ConvertStringToSecond(getData.getString("dateAcceptance")),
					getData.getString("ipAddress"),
					getData.getString("fees"));
			try {
				if (!channelNameRepository.existsByName(getData.getString("name"))) {
					ChannelName channelName = new ChannelName(getData.getString("name"));
					channelNameRepository.save(channelName);
				}
				if (!channelValueRepository.existsByServicePack(getData.getString("servicePack"))) {
					ChannelName getChannelName = channelNameRepository.findByName(getData.getString("name"));
					ChannelValue channelValue = new ChannelValue(
							getData.getString("servicePack"),
							getData.getString("value"),
							getChannelName);
					channelValueRepository.save(channelValue);
				}
				if (!getData.getString("customerMove").equals("")) {
					status = "online";
				}
				ChannelValue getChannelValue = channelValueRepository
						.findByServicePack(getData.getString("servicePack"));
				ChannelAttribute channelAttribute = new ChannelAttribute(
						getData.getString("customer"),
						status,
						getData.getString("virtualNum"),
						getData.getString("usernamePPPoE"),
						getChannelValue);
				channelAttributeRepository.save(channelAttribute);
				historyChannel.setChannelAttribute(channelAttribute);
				createChannelDetail.setChannelAttribute(channelAttribute);
				if (contractRepository.existsByCustomId(getData.getString("customId"))) {
					Contract getContract = contractRepository.findByCustomId(getData.getString("customId"));
					createChannelDetail.setContract(getContract);
					channelDetailRepository.save(createChannelDetail);
				} else {
					Contract createContract = new Contract(
							getData.getString("customId"),
							getData.getString("numContract"),
							getData.getString("clientName"),
							getData.getString("servicePlans"),
							getData.getString("street"),
							null);
					contractRepository.save(createContract);
					createChannelDetail.setContract(createContract);
					channelDetailRepository.save(createChannelDetail);
				}
				if (!getData.getString("customerMove").equals("")) {
					if (contractRepository.existsByClientName(getData.getString("customerMove"))) {
						Contract historyContract = contractRepository
								.findByClientName(getData.getString("customerMove"));
						historyChannel.setContract(historyContract);
					} else {
						JSONObject getContractByCustomerMove = new JSONObject(
								getContractByClientName(getData.getString("customerMove")));
						Contract createContractHistory = new Contract(
								getContractByCustomerMove.getString("customId"),
								getContractByCustomerMove.getString("numContract"),
								getData.getString("customerMove"),
								getContractByCustomerMove.getString("servicePlans"),
								getContractByCustomerMove.getString("street"),
								null);
						historyChannel.setContract(createContractHistory);
					}
					historyChannelRepository.save(historyChannel);
				}
			} catch (Exception e) {
			}
		}
		JSONArray getAllChannelDetail = new JSONArray(channelInfoService.findAllChannelDetail());
		return getAllChannelDetail.toString();
	}

	@Operation(description = "Find history channel by virtualNum")
	@GetMapping("/it4u/history/channel/{id}")
	public String getHistoryChannel(@PathVariable(value = "id") long id) {
		ChannelAttribute channelAttribute = channelAttributeRepository.findById(id);
		List<HistoryChannel> historyChannel = historyChannelRepository.findByChannelAttribute(channelAttribute);
		JSONArray channelDetailToJson = new JSONArray(historyChannel);
		return channelDetailToJson.toString();
	}

	@Operation(description = "Delete channel detail by id")
	@DeleteMapping("/it4u/channel/detail.{id}")
	public boolean deleteChannelDetailById(@PathVariable(value = "id") long id) {
		channelInfoService.deleteChannelDetail(id);
		return true;
	}

	@Operation(description = "Get contract by customId")
	@GetMapping("/it4u/contract/customId.{customId}")
	public ResponseEntity<?> getContractByCustomId(@PathVariable("customId") String customId) {
		ApiRequest apiRequest = new ApiRequest();
		JSONObject itemData = new JSONObject();
		SystemConfig systemConfig = systemConfigService.findSystemConfig();
		authAppKey = systemConfig.getTokenUcrm();
		String getInfoClient = apiRequest.getRequestUCRM(urlUCRM + "/clients", authAppKey);
		JSONArray convertInfoClient = new JSONArray(getInfoClient);
		if (!contractRepository.existsByCustomId(customId)) {
			for (int i = 0; i < convertInfoClient.length(); i++) {
				JSONObject getItem = (JSONObject) convertInfoClient.get(i);
				if (getItem.getString("userIdent").equals(customId)) {
					String getServicePlan = apiRequest
							.getRequestUCRM(urlUCRM + "/clients/" + getItem.getInt("id") + "/services", authAppKey);
					JSONArray convertServicePlan = new JSONArray(getServicePlan);
					JSONArray getAttributes = getItem.getJSONArray("attributes");
					itemData.put("customId", customId);
					for (int j = 0; j < getAttributes.length(); j++) {
						JSONObject getInfoAtt = (JSONObject) getAttributes.get(j);
						if (getInfoAtt.getString("key").equals("sHPNg")) {
							itemData.put("contracts", getInfoAtt.getString("value"));
						}
						if (getInfoAtt.getString("key").equals("iNThoINgILiNHLPT")) {
							itemData.put("phone", getInfoAtt.getString("value"));
						}
					}
					JSONObject servicePlan = (JSONObject) convertServicePlan.get(0);
					itemData.put("servicePlan", servicePlan.getString("name"));
					try {
						itemData.put("companyName", getItem.getString("companyName"));
					} catch (Exception e) {
						itemData.put("companyName",
								getItem.getString("firstName") + " " + getItem.getString("lastName"));
					}
					itemData.put("street", getItem.getString("street1"));
				}

			}
			Contract createContract = new Contract(
					itemData.getString("customId"),
					itemData.getString("contracts"),
					itemData.getString("companyName"),
					itemData.getString("servicePlan"),
					itemData.getString("street"),
					itemData.getString("phone"));
			contractRepository.save(createContract);
		}
		return ResponseEntity.ok(channelInfoService.findContractByCustomId(customId));
	}

	@Operation(description = "Get contract by customId")
	@GetMapping("/it4u/contract.{clientName}")
	public String getContractByClientName(@PathVariable("clientName") String clientName) {
		ApiRequest apiRequest = new ApiRequest();
		JSONObject itemData = new JSONObject();
		String companyName = "";
		if (!contractRepository.existsByClientName(clientName)) {
			SystemConfig systemConfig = systemConfigService.findSystemConfig();
			authAppKey = systemConfig.getTokenUcrm();
			String getInfoClient = apiRequest.getRequestUCRM(urlUCRM + "/clients", authAppKey);
			JSONArray convertInfoClient = new JSONArray(getInfoClient);
			for (int i = 0; i < convertInfoClient.length(); i++) {
				JSONObject getItem = (JSONObject) convertInfoClient.get(i);
				try {
					companyName = getItem.getString("companyName");
				} catch (Exception e) {
					companyName = getItem.getString("firstName") + " " + getItem.getString("lastName");
				}
				if (companyName.equals(clientName)) {
					String getServicePlan = apiRequest
							.getRequestUCRM(urlUCRM + "/clients/" + getItem.getInt("id") + "/services", authAppKey);
					JSONArray convertServicePlan = new JSONArray(getServicePlan);
					JSONArray getAttributes = getItem.getJSONArray("attributes");
					itemData.put("clientName", clientName);
					for (int j = 0; j < getAttributes.length(); j++) {
						JSONObject getInfoAtt = (JSONObject) getAttributes.get(j);
						if (getInfoAtt.getString("key").equals("sHPNg")) {
							itemData.put("contracts", getInfoAtt.getString("value"));
						}
						if (getInfoAtt.getString("key").equals("iNThoINgILiNHLPT")) {
							itemData.put("phone", getInfoAtt.getString("value"));
						}
					}
					itemData.put("customId", getItem.getString("userIdent"));
					JSONObject servicePlan = (JSONObject) convertServicePlan.get(0);
					itemData.put("servicePlan", servicePlan.getString("name"));
					itemData.put("street", getItem.getString("street1"));
				}

			}
			Contract createContract = new Contract(
					itemData.getString("customId"),
					itemData.getString("contracts"),
					itemData.getString("clientName"),
					itemData.getString("servicePlan"),
					itemData.getString("street"),
					itemData.getString("phone"));
			contractRepository.save(createContract);
		}
		Contract contract = channelInfoService.findContractByClientName(clientName);
		JSONObject result = new JSONObject(contract);
		return result.toString();
	}

	@Operation(description = "Monitor daily traffic and client for customers")
	@GetMapping("/it4u/monitor/daily/trafficAndClient")
	public String getDailyTrafficAndClient() {
		String result = "";
		String title = "Báo cáo theo ngày";
		ApiRequest apiRequest = new ApiRequest();
		JSONArray getCookies = new JSONArray(configTokenService.findAll());
		JSONArray getUsers = new JSONArray(userService.findAll());
		JSONObject body = (JSONObject) getCookies.get(0);
		csrfToken = body.getString("csrfToken");
		unifises = body.getString("unifises");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Long endTime = timestamp.getTime() + 7 * 60 * 60 * 1000;
		Long startTime = endTime - 2 * 24 * 60 * 60 * 1000;
		String postData = "{\"attrs\":[\"wlan_bytes\",\"wlan-num_sta\",\"time\"], \"start\":\"" + startTime
				+ "\", \"end\":\"" + endTime + "\"}";
		for (int i = 0; i < getUsers.length(); i++) {
			JSONObject getUser = (JSONObject) getUsers.get(i);
			try {
				String getRegistrationId = getUser.getString("registrationId");
				JSONArray getSitenames = getUser.getJSONArray("sitename");
				for (int j = 0; j < getSitenames.length(); j++) {
					JSONObject getSitename = (JSONObject) getSitenames.get(j);
					String getDataDaily = apiRequest.postRequestApi(urlIt4u,
							"/s/" + getSitename.getString("idname") + "/stat/report/daily.site/", csrfToken, unifises,
							postData);
					JSONObject convertDataDaily = new JSONObject(getDataDaily);
					JSONArray getData = convertDataDaily.getJSONArray("data");
					for (int k = 0; k < getData.length(); k++) {
						JSONObject getPosStart = (JSONObject) getData.get(k);
						JSONObject getPosEnd = (JSONObject) getData.get(k + 1);
						long getStartTime = getPosStart.getLong("time");
						long getEndTime = getPosEnd.getLong("time");
						Integer getStartNumSta = getPosStart.getInt("wlan-num_sta");
						Integer getEndNumSta = getPosEnd.getInt("wlan-num_sta");
						if (getStartTime != getEndTime) {
							result = notification(getRegistrationId, getStartNumSta, getEndNumSta, title);
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	@Operation(description = "Monitor weekly traffic and client for customers")
	@GetMapping("/it4u/monitor/weekly/trafficAndClient")
	public String getWeeklyTrafficAndClient() {
		String result = "";
		String title = "Báo cáo theo tuần";
		int firstNumSta = 0;
		int secondNumSta = 0;
		ApiRequest apiRequest = new ApiRequest();
		JSONArray getCookies = new JSONArray(configTokenService.findAll());
		JSONArray getUsers = new JSONArray(userService.findAll());
		JSONObject body = (JSONObject) getCookies.get(0);
		csrfToken = body.getString("csrfToken");
		unifises = body.getString("unifises");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Long endTimeSecondWeek = timestamp.getTime() + 7 * 60 * 60 * 1000;
		Long startTimeSecondWeek = endTimeSecondWeek - 7 * 24 * 60 * 60 * 1000;
		Long startTimeFirstWeek = startTimeSecondWeek - 7 * 24 * 60 * 60 * 1000;
		String postDataFirstTime = "{\"attrs\":[\"wlan_bytes\",\"wlan-num_sta\",\"time\"], \"start\":\""
				+ startTimeFirstWeek
				+ "\", \"end\":\"" + startTimeSecondWeek + "\"}";
		String postDataSecondTime = "{\"attrs\":[\"wlan_bytes\",\"wlan-num_sta\",\"time\"], \"start\":\""
				+ startTimeSecondWeek
				+ "\", \"end\":\"" + endTimeSecondWeek + "\"}";
		for (int i = 0; i < getUsers.length(); i++) {
			JSONObject getUser = (JSONObject) getUsers.get(i);
			try {
				String getRegistrationId = getUser.getString("registrationId");
				JSONArray getSitenames = getUser.getJSONArray("sitename");
				for (int j = 0; j < getSitenames.length(); j++) {
					JSONObject getSitename = (JSONObject) getSitenames.get(j);
					String getDataFirstWeekly = apiRequest.postRequestApi(urlIt4u,
							"/s/" + getSitename.getString("idname") + "/stat/report/daily.site/", csrfToken, unifises,
							postDataFirstTime);
					String getDataSecondWeekly = apiRequest.postRequestApi(urlIt4u,
							"/s/" + getSitename.getString("idname") + "/stat/report/daily.site/", csrfToken, unifises,
							postDataSecondTime);
					JSONObject convertDataFirstWeekly = new JSONObject(getDataFirstWeekly);
					JSONArray dataFirstWeekly = convertDataFirstWeekly.getJSONArray("data");
					JSONObject convertDataSecondWeekly = new JSONObject(getDataSecondWeekly);
					JSONArray dataSecondWeekly = convertDataSecondWeekly.getJSONArray("data");
					for (int k = 0; k < dataFirstWeekly.length() - 1; k++) {
						JSONObject getPosStart = (JSONObject) dataFirstWeekly.get(k);
						JSONObject getPosEnd = (JSONObject) dataFirstWeekly.get(k + 1);
						long getStartTime = getPosStart.getLong("time");
						long getEndTime = getPosEnd.getLong("time");
						if (getStartTime != getEndTime) {
							firstNumSta = firstNumSta + getPosEnd.getInt("wlan-num_sta");
						}
					}
					for (int k = 0; k < dataSecondWeekly.length() - 1; k++) {
						JSONObject getPosStart = (JSONObject) dataSecondWeekly.get(k);
						JSONObject getPosEnd = (JSONObject) dataSecondWeekly.get(k + 1);
						long getStartTime = getPosStart.getLong("time");
						long getEndTime = getPosEnd.getLong("time");
						if (getStartTime != getEndTime) {
							secondNumSta = secondNumSta + getPosEnd.getInt("wlan-num_sta");
						}
					}
				}
				result = notification(getRegistrationId, firstNumSta, secondNumSta, title);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	public String notification(String registrationId, int startData, int endData, String title) {
		String result = "";
		ApiRequest apiRequest = new ApiRequest();
		Boolean checkNotif = false;
		String body = "";
		if (startData < endData && endData > 20) {
			if (Math.round(endData / startData) > 1) {
				checkNotif = true;
				body = "Số lượng thiết bị sử dụng tăng nhanh, login app IT4U để xem chi tiết.";
			}
			if (Math.round(endData / startData) > 2) {
				checkNotif = true;
				body = "Số lượng thiết bị sử dụng tăng rất nhanh, nếu có phát hiện mạng chập chờn vui lòng liên hệ: 02363 575788";
			}
		}
		if ((startData > endData) && (startData > 20)) {
			if ((startData / endData) > 0) {
				checkNotif = true;
				body = "Số lượng thiết bị sử dụng giảm, login vào app IT4U để xem chi tiết.";
			}
		}
		if (checkNotif) {
			String dataPost = "{\"registration_ids\":[\"" + registrationId + "\"],\"notification\": {\"title\": \""
					+ title + "\", \"body\": \"" + body + "\"}}";
			try {
				result = apiRequest.getConnectionFirebase(urlFirebase, senderId, getAccessToken, dataPost);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public String regex(String regex, String data) {
		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(data);
		String group1 = "";
		while (matcher.find()) {
			System.out.println("Full match: " + matcher.group(0));
			for (int i = 1; i <= matcher.groupCount(); i++) {
				System.out.println("Group " + i + ": " + matcher.group(i));
				group1 = matcher.group(i);
			}
		}
		return group1;
	}
}

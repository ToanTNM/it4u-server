package vn.tpsc.it4u.controller;

import vn.tpsc.it4u.util.ApiRequest;
import vn.tpsc.it4u.util.ApiResponseUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import vn.tpsc.it4u.model.SitesName;
import vn.tpsc.it4u.repository.SitesNameRepository;
import vn.tpsc.it4u.service.ConfigTokenService;

@RestController
@RequestMapping("${app.api.version}")
public class ConfigController {
    @Value("${app.ubnt.url_test}")
    private String urlIt4u;

    @Value("${app.ubnt.csrf_token}")
    private String csrfToken;

    @Value("${app.ubnt.unifises}")
    private String unifises;

    @Autowired
    private ConfigTokenService configTokenService;

    @Autowired
    private SitesNameRepository sitesNameRepository;

    @ApiOperation(value = "Create vlan group")
    @PostMapping("it4u/{id}/wlangroup")
    public String createVLGroup(@PathVariable(value = "id") String id, @RequestBody String postData) {
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        csrfToken = body.getString("csrfToken");
        unifises = body.getString("unifises");
        ApiRequest apiRequest = new ApiRequest();
        String createVlanGroup = apiRequest.postRequestApi(urlIt4u, "/s/" + id + "/rest/wlangroup", csrfToken, unifises,
                postData);
        return createVlanGroup;
    }

    @ApiOperation(value = "Get vlan group")
    @GetMapping("it4u/{id}/wlangroup")
    public String getVLGroup(@PathVariable(value = "id") String id) {
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        csrfToken = body.getString("csrfToken");
        unifises = body.getString("unifises");
        ApiRequest apiRequest = new ApiRequest();
        String getData = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlangroup", csrfToken, unifises);
        JSONObject convertData = new JSONObject(getData);
        JSONArray data = convertData.getJSONArray("data");
        return data.toString();
    }

    @ApiOperation(value = "Create SSID to vlan group")
    @PostMapping("it4u/{id}/wlanconf")
    public String createSSID(@PathVariable(value = "id") String id, @RequestBody String postData) {
        ApiRequest apiRequest = new ApiRequest();
        String createVlanGroup = apiRequest.postRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken, unifises,
                postData);
        JSONObject convertData = new JSONObject(createVlanGroup);
        JSONArray data = convertData.getJSONArray("data");
        return data.toString();
    }

    @ApiOperation(value = "Assign vlan group to APs")
    @PostMapping("it4u/{id}/group/device")
    public String assignVG(@PathVariable(value = "id") String id, @RequestBody String postData) {
        ApiRequest apiRequest = new ApiRequest();
        JSONObject createPostData = new JSONObject();
        List<String> itemDevicesArray = new ArrayList<>();
        String getDeviceId = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/stat/device", csrfToken, unifises);
        JSONObject convertData = new JSONObject(getDeviceId);
        JSONArray getDataDevices = convertData.getJSONArray("data");
        for (int i = 0; i < getDataDevices.length(); i++) {
            JSONObject itemDevice = (JSONObject) getDataDevices.get(i);
            itemDevicesArray.add(itemDevice.getString("device_id"));
        }
        createPostData.put("id", itemDevicesArray);
        createPostData.put("data", postData);
        String putData = "{\"id\":" + itemDevicesArray + ", \"data\":" + postData + "}";
        JSONObject convertPutData = new JSONObject(putData);
        String assignVG = apiRequest.putRequestApi(urlIt4u, "/s/" + id + "/group/device", csrfToken, unifises,
                convertPutData.toString());
        return assignVG;
    }

    @ApiOperation(value = "Create a sitename")
    @PostMapping("it4u/{id}/sitemgr")
    public String createSitename(@PathVariable(value = "id") String id, @RequestBody String postData) {
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        csrfToken = body.getString("csrfToken");
        unifises = body.getString("unifises");
        ApiRequest apiRequest = new ApiRequest();
        String createSitename = apiRequest.postRequestApi(urlIt4u, "/s/" + id + "/cmd/sitemgr", csrfToken, unifises,
                postData);
        JSONObject convertData = new JSONObject(createSitename);
        JSONArray getData = convertData.getJSONArray("data");
        JSONObject data = (JSONObject) getData.get(0);
        final SitesName addSitename = new SitesName(data.getString("desc"), data.getString("name"));
        sitesNameRepository.save(addSitename);
        return data.toString();
    }

    @ApiOperation(value = "Create a hotspot")
    @PostMapping("it4u/{id}/hotspot")
    public String createHotspot(@PathVariable(value = "id") String id, @RequestBody String postData) {
        String result = "";

        return result;
    }
}
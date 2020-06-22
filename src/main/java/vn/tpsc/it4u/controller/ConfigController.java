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

    @Value("${app.ubnt.hotspot_template}")
    private String hotspotTemplate;

    @Value("${app.ubnt.csrf_token}")
    private String csrfToken;

    @Value("${app.ubnt.unifises}")
    private String unifises;

    @Value("${app.ubnt.username}")
    private String username;

    @Value("${app.ubnt.password}")
    private String password;

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
        try {
            String createVlanGroup = apiRequest.postRequestApi(urlIt4u, "/s/" + id + "/rest/wlangroup", csrfToken,
                    unifises, postData);
            JSONObject getData = new JSONObject(createVlanGroup);
            JSONArray result = getData.getJSONArray("data");
            return result.toString();
        } catch (Exception e) {
            getCookies();
            String createVlanGroup = apiRequest.postRequestApi(urlIt4u, "/s/" + id + "/rest/wlangroup", csrfToken,
                    unifises, postData);
            JSONObject getData = new JSONObject(createVlanGroup);
            JSONArray result = getData.getJSONArray("data");
            return result.toString();
        }
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
        try {
            String getData = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlangroup", csrfToken, unifises);
            JSONObject convertData = new JSONObject(getData);
            JSONArray data = convertData.getJSONArray("data");
            return data.toString();
        } catch (Exception e) {
            getCookies();
            String getData = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlangroup", csrfToken, unifises);
            JSONObject convertData = new JSONObject(getData);
            JSONArray data = convertData.getJSONArray("data");
            return data.toString();
        }
        
    }

    @ApiOperation(value = "Create SSID to vlan group")
    @PostMapping("it4u/{id}/wlanconf")
    public String createSSID(@PathVariable(value = "id") String id, @RequestBody String postData) {
        ApiRequest apiRequest = new ApiRequest();
        try {
            String createVlanGroup = apiRequest.postRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken,
                    unifises, postData);
            JSONObject convertData = new JSONObject(createVlanGroup);
            JSONArray data = convertData.getJSONArray("data");
            return data.toString();
        } catch (Exception e) {
            getCookies();
            String createVlanGroup = apiRequest.postRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken,
                    unifises, postData);
            JSONObject convertData = new JSONObject(createVlanGroup);
            JSONArray data = convertData.getJSONArray("data");
            return data.toString();
        }
    }

    @ApiOperation(value = "Get wlanconf to vlan group")
    @GetMapping("it4u/{id}/wlanconf/{wlan}")
    public String getWlanconf(@PathVariable(value = "id") String id, @PathVariable(value = "wlan") String wlan) {
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
        for (int i=0; i < data.length(); i++) {
            JSONObject getItem = (JSONObject) data.get(i);
            if (wlan.equals(getItem.getString("wlangroup_id"))) {
                result.add(getItem.toString());
            }
        }
        return result.toString();
    }

    @ApiOperation(value = "Get SSID to vlan group")
    @GetMapping("it4u/{id}/essid/{wlan}")
    public String getSSID(@PathVariable(value = "id") String id, @PathVariable(value = "wlan") String wlan) {
        JSONObject result = new JSONObject();
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
            if (wlan.equals(getItem.getString("_id"))) {
                result = getItem = (JSONObject) data.get(i);
                break;
            }
        }
        return result.toString();
    }

    @ApiOperation(value = "Put SSID to vlan group")
    @PutMapping("it4u/{id}/wlanconf/{ssid}")
    public String putSSID(@PathVariable(value = "id") String id, @PathVariable(value = "ssid") String ssid, @RequestBody String postData) {
        List<String> result = new ArrayList<>();
        ApiRequest apiRequest = new ApiRequest();
        JSONArray data = new JSONArray();
        JSONObject convertDataPost = new JSONObject(postData);
        String putData = "";
        try {
            String getVlanGroup = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken,
                    unifises);
            JSONObject convertVlanGroup = new JSONObject(getVlanGroup);
            JSONArray getVlanGroupJson = convertVlanGroup.getJSONArray("data");
            for (int i = 0; i< getVlanGroupJson.length(); i++) {
                JSONObject getItem = (JSONObject) getVlanGroupJson.get(i);
                if (ssid.equals(getItem.getString("_id"))) {
                    if (convertDataPost.getString("security").equals("wpapsk")) {
                        getItem.put("security", "wpapsk");
                        getItem.put("x_passphrase", convertDataPost.getString("x_passphrase"));
                        getItem.put("name", convertDataPost.getString("name"));
                        putData = getItem.toString();
                        break;
                    }
                    else {
                        getItem.put("security", "open");
                        getItem.put("name", convertDataPost.getString("name"));
                        putData = getItem.toString();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            getCookies();
            String getVlanGroup = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf", csrfToken, unifises);
            JSONObject convertVlanGroup = new JSONObject(getVlanGroup);
            JSONArray getVlanGroupJson = convertVlanGroup.getJSONArray("data");
            for (int i = 0; i < getVlanGroupJson.length(); i++) {
                JSONObject getItem = (JSONObject) getVlanGroupJson.get(i);
                if (ssid.equals(getItem.getString("_id"))) {
                    if (convertDataPost.getString("security").equals("wpapsk")) {
                        getItem.put("security", "wpapsk");
                        getItem.put("x_passphrase", convertDataPost.getString("x_passphrase"));
                        getItem.put("name", convertDataPost.getString("name"));
                        putData = getItem.toString();
                        break;
                    } else {
                        getItem.put("security", "open");
                        getItem.put("name", convertDataPost.getString("name"));
                        putData = getItem.toString();
                        break;
                    }
                }
            }
        }
        String createVlanGroup = apiRequest.putRequestApi(urlIt4u, "/s/" + id + "/rest/wlanconf/" + ssid, csrfToken,
                unifises, putData);
        JSONObject convertData = new JSONObject(createVlanGroup);
        data = convertData.getJSONArray("data");
        return data.toString();
    }

    @ApiOperation(value = "Assign vlan group to APs")
    @PostMapping("it4u/{id}/group/device")
    public String assignVG(@PathVariable(value = "id") String id, @RequestBody String postData) {
        ApiRequest apiRequest = new ApiRequest();
        JSONObject createPostData = new JSONObject();
        List<String> itemDevicesArray = new ArrayList<>();
        String getDeviceId = "";
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
        return data.toString();
    }

    @ApiOperation(value = "Get a hotspot")
    @GetMapping("it4u/{id}/hotspot")
    public String getHotspot(@PathVariable(value = "id") String id) {
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
        return getData.toString();
    }

    @ApiOperation(value = "Create a hotspot")
    @PostMapping("it4u/{id}/hotspot")
    public String createHotspot(@PathVariable(value = "id") String id, @RequestBody String data) {
        JSONObject postData = new JSONObject(data);
        String result = "";
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
        Boolean portal = true;
        int positionPortal = 0;
        int positionPortalSetting = 0;
        for (int i = 0; i < getDataFromPostData.length(); i++) {
            JSONObject getItem = (JSONObject) getDataFromPostData.get(i);
            try {
                portal = getItem.getBoolean("portal_enabled");
                positionPortalSetting = i;
            } catch (Exception e) {
                //TODO: handle exception
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
        return createHotspot.toString();
    }
    
    public String getCookies() {
        ApiRequest apiRequest = new ApiRequest();
        String dataPost = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\",\"remember\":\"true\",\"strict\":\"true\"}";
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

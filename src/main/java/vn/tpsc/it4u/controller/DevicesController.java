package vn.tpsc.it4u.controller;

import org.hibernate.property.access.spi.GetterFieldImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import vn.tpsc.it4u.util.ApiRequest;
import vn.tpsc.it4u.security.CurrentUser;
import vn.tpsc.it4u.security.CustomUserDetails;
import vn.tpsc.it4u.service.ConfigTokenService;
import vn.tpsc.it4u.util.Calculator;

@RestController
@Slf4j
@RequestMapping("${app.api.version}")
public class DevicesController {
    @Value("${app.ubnt.url}")
    private String urlIt4u;

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

    @ApiOperation(value = "Get devices information")
    @GetMapping("it4u/{id}/devices")
    public String getDevicesInf(@PathVariable(value = "id") String id, @CurrentUser CustomUserDetails currentUser) {
        List<String> txBytes = new ArrayList<>();
        List<String> rxBytes = new ArrayList<>();
        List<String> result = new ArrayList<>();
        String getMac = "";
        JSONArray getData = new JSONArray();
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        csrfToken = body.getString("csrfToken");
        unifises = body.getString("unifises");
        ApiRequest apiRequest = new ApiRequest();
        try {
            String getDeviceInf = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/stat/sta", csrfToken, unifises);
            getMac = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/stat/device-basic", csrfToken, unifises);
            JSONObject getDataDeviceInf = new JSONObject(getDeviceInf);
            getData = getDataDeviceInf.getJSONArray("data");
        } catch (Exception e) {
            getCookies();
            String getDeviceInf = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/stat/sta", csrfToken, unifises);
            getMac = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/stat/device-basic", csrfToken, unifises);
            JSONObject getDataDeviceInf = new JSONObject(getDeviceInf);
            getData = getDataDeviceInf.getJSONArray("data");
        }
        JSONObject convertDataMac = new JSONObject(getMac);
        JSONArray getDataMac = convertDataMac.getJSONArray("data");
        Calculator getCalculator = new Calculator();
        for(int i=0; i<getData.length(); i++) {
            JSONObject getItem = (JSONObject) getData.get(i);
            long getRxBytes = getItem.getLong("rx_bytes");
            long getTxBytes = getItem.getLong("tx_bytes");
            rxBytes = getCalculator.ConvertBytes(getRxBytes);
            txBytes = getCalculator.ConvertBytes(getTxBytes);
            for (int j = 0; j < getDataMac.length(); j++) {
                JSONObject getItemMac = (JSONObject) getDataMac.get(j);
                if (getItemMac.getString("mac").equals(getItem.getString("ap_mac"))) {
                    getItem.put("mac_name", getItemMac.getString("name"));
                }
            }
            if(getItem.getString("radio").equals("ng")) {
                getItem.put("radio_name", "2.4GHz");
            } else getItem.put("radio_name", "5GHz");
            getItem.put("rx_bytes", rxBytes.get(0)+ rxBytes.get(1));
            getItem.put("tx_bytes", txBytes.get(0) + txBytes.get(1));
            result.add(getItem.toString());
        }
        log.info(currentUser.getUsername() + " - it4u/" + id + "/devices");
        return result.toString();
    }

    @ApiOperation(value = "Get block devices infomation")
    @GetMapping("it4u/{id}/block/info")
    public String getBlockInfo(@PathVariable(value = "id") String id, @CurrentUser CustomUserDetails currentUser) {
        JSONArray getData = new JSONArray();
        List<String> result = new ArrayList<>();
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        csrfToken = body.getString("csrfToken");
        unifises = body.getString("unifises");
        ApiRequest apiRequest = new ApiRequest();
        try {
            String getDeviceInf = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/stat/alluser", csrfToken, unifises);
            JSONObject getDataDeviceInf = new JSONObject(getDeviceInf);
            getData = getDataDeviceInf.getJSONArray("data");
            // return result.toString();
        } catch (Exception e) {
            getCookies();
            String getDeviceInf = apiRequest.getRequestApi(urlIt4u, "/s/" + id + "/stat/alluser", csrfToken, unifises);
            JSONObject getDataDeviceInf = new JSONObject(getDeviceInf);
            getData = getDataDeviceInf.getJSONArray("data");
            // return result.toString();
        }
        for(int i=0; i< getData.length(); i++) {
            JSONObject getItem = (JSONObject) getData.get(i);
            try {
                if (getItem.getBoolean("blocked")) {
                    result.add(getItem.toString());
                }
            } catch (Exception e) {
            }
        }
        log.info(currentUser.getUsername() + " - it4u/" + id + "/block/info");
        return result.toString();
    }

    @ApiOperation(value = "Block and unblock device")
    @PostMapping("it4u/{id}/block/device")
    public String blockAndUnBlockDevice(@PathVariable(value = "id") String id, @CurrentUser CustomUserDetails currentUser, @RequestBody String postData) {
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        csrfToken = body.getString("csrfToken");
        unifises = body.getString("unifises");
        ApiRequest apiRequest = new ApiRequest();
        try {
            String getDeviceInf = apiRequest.postRequestApi(urlIt4u , "/s/" + id + "/cmd/stamgr", csrfToken, unifises, postData);
            JSONObject getDataDeviceInf = new JSONObject(getDeviceInf);
            JSONArray result = getDataDeviceInf.getJSONArray("data");
            log.info(currentUser.getUsername() + " - it4u/" + id + "/block/device");
            return result.toString();
        } catch (Exception e) {
            getCookies();
            String getDeviceInf = apiRequest.postRequestApi(urlIt4u , "/s/" + id + "/cmd/stamgr", csrfToken, unifises, postData);
            JSONObject getDataDeviceInf = new JSONObject(getDeviceInf);
            JSONArray result = getDataDeviceInf.getJSONArray("data");
            log.info(currentUser.getUsername() + " - it4u/" + id + "/block/device");
            return result.toString();
        }
    }

    public String getCookies() {
        ApiRequest apiRequest = new ApiRequest();
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
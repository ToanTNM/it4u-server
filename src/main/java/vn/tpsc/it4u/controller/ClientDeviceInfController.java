package vn.tpsc.it4u.controller;

import java.util.Locale;

import javax.ws.rs.DELETE;

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

import io.swagger.annotations.ApiOperation;
import vn.tpsc.it4u.service.ClientDeviceInfService;
import vn.tpsc.it4u.util.ApiRequest;
import vn.tpsc.it4u.util.ApiResponseUtils;

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
    ApiResponseUtils apiResponse;
    
    @ApiOperation(value = "Create client device information")
    @PostMapping("/it4u/clientDeviceInf")
    public ResponseEntity<?> createClientDeviceInf(@RequestBody String data, Locale locale) {
        JSONObject convertDataToJson = new JSONObject(data);
        clientDeviceInfService.createClientDeviceInf(convertDataToJson);
        return ResponseEntity.ok(apiResponse.success(200, locale));
    }

    @ApiOperation(value = "Upload client device infomation")
    @PostMapping("/it4u/upload/ClientDeviceInf")
    public ResponseEntity<?> uploadClientDeviceInf(@RequestBody String data, Locale locale) {
        JSONArray convertDataToJson = new JSONArray(data);
        ApiRequest apiRequest = new ApiRequest();
        for(int i=0; i<convertDataToJson.length(); i++) {
            JSONObject getData = (JSONObject) convertDataToJson.get(i);
            JSONObject infoContract = new JSONObject();
            String getSiteName = getData.getString("siteName");
            try {
                String getInfoClient = apiRequest.getRequestUCRM(urlUCRM + "/clients?customAttributeKey=sitename&customAttributeValue=" + getSiteName, authAppKey);
                JSONArray convertInfoClient = new JSONArray(getInfoClient);
                if (convertInfoClient.length() > 0) {
                    JSONObject infoClient = (JSONObject) convertInfoClient.get(0);
                    JSONArray getCustomAttributes = infoClient.getJSONArray("attributes");
                    for(int j=0; j<getCustomAttributes.length(); j++) {
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
                            JSONObject servicePlan = (JSONObject) convertServicePlan.get(0);
                            infoContract.put("servicePlan", servicePlan.getString("name"));
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
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        return ResponseEntity.ok(apiResponse.success(200, locale));
    }

    @ApiOperation(value = "Update client device information by id")
    @PutMapping("/it4u/clientDeviceInf/{id}")
    public ResponseEntity<?> updateClientDeviceInfById(@PathVariable(value = "id") long id, 
        @RequestBody String data, Locale locale) {
        JSONObject convertDataToJson = new JSONObject(data);
        clientDeviceInfService.updateClientDeviceInf(id, convertDataToJson);
        return ResponseEntity.ok(apiResponse.success(200, locale));
    }

    @ApiOperation(value = "Get all client device information")
    @GetMapping("/it4u/clientDeviceInf")
    public String getClientDeviceInf() {
        JSONArray getData = new JSONArray(clientDeviceInfService.findAllClientDeviceInf());
        return getData.toString();
    }

    @ApiOperation(value = "Get limit client device information")
    @GetMapping("/it4u/clientDeviceInfLimit/{limit}")
    public String getLimitClientDeviceInf(@PathVariable(value = "limit") String limit) {
        if (limit.equals("all")) {
            JSONArray getData = new JSONArray(clientDeviceInfService.findAllClientDeviceInf());
            return getData.toString();
        }
        else {
            long num = Long.parseLong(limit);
            JSONArray getData = new JSONArray(clientDeviceInfService.findLimitClientDeviceInf(num));
            return getData.toString();
        }
    }

    @ApiOperation(value = "Search client device info by param")
    @PostMapping("/it4u/searchClientDeviceInf")
    public String searchClientDeviceInf(@RequestBody String data) {
        JSONObject convertDataToJson = new JSONObject(data);
        String content = convertDataToJson.getString("content");
        JSONArray getData = new JSONArray(clientDeviceInfService.findAllByParam(content));
        return getData.toString();
    }

    @ApiOperation(value = "Delete client device information by id")
    @DeleteMapping("/it4u/clientDeviceInf/{id}")
    public ResponseEntity<?> deleteClientDeviceInf(@PathVariable(value = "id") Long id, Locale locale) {
        clientDeviceInfService.deleteClientDeviceInf(id);
        return ResponseEntity.ok(apiResponse.success(200, locale));
    }

}

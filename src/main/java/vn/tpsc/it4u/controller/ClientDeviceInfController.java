package vn.tpsc.it4u.controller;

import java.util.Locale;

import javax.ws.rs.DELETE;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import vn.tpsc.it4u.util.ApiResponseUtils;

@RestController
@RequestMapping("${app.api.version}")
public class ClientDeviceInfController {
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
        for(int i=0; i<convertDataToJson.length(); i++) {
            JSONObject getData = (JSONObject) convertDataToJson.get(i);
            clientDeviceInfService.uploadClientDeviceInf(getData);
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

    @ApiOperation(value = "Delete client device information by id")
    @DeleteMapping("/it4u/clientDeviceInf/{id}")
    public ResponseEntity<?> deleteClientDeviceInf(@PathVariable(value = "id") Long id, Locale locale) {
        clientDeviceInfService.deleteClientDeviceInf(id);
        return ResponseEntity.ok(apiResponse.success(200, locale));
    }

}

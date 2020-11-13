package vn.tpsc.it4u.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import io.swagger.annotations.ApiOperation;
import vn.tpsc.it4u.util.ApiRequest;
import vn.tpsc.it4u.repository.ClientSegmentRepository;
import vn.tpsc.it4u.model.ClientSegment;

@RestController
@Slf4j
@RequestMapping("${app.api.version}")
public class StatisticController {
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

    @Autowired
    ClientSegmentRepository clientSegmentRepository;

    @ApiOperation(value = "Statistic segments for client")
    @PostMapping("/it4u/segments/client")
    public String getSegmentClient(@RequestBody String postData) {
        ApiRequest apiRequest = new ApiRequest();
        // JSONObject itemData = new JSONObject();
        List<String> result = new ArrayList<>();
        List<String> getSegments = new ArrayList<>();
        List<Integer> listData = new ArrayList<>();
        List<String> listSegment = new ArrayList<>();
        JSONObject itemData = new JSONObject();
        JSONObject convertPostData = new JSONObject(postData);
        String getInfoClient = apiRequest.getRequestUCRM(urlUCRM + "/clients", authAppKey);
        JSONArray convertInfoClient = new JSONArray(getInfoClient);
        for (int i = 0; i < convertInfoClient.length(); i++) {
            JSONObject getItem = (JSONObject) convertInfoClient.get(i);
            try {
                JSONArray getAttributes = getItem.getJSONArray("attributes");
                for (int j = 0; j < getAttributes.length(); j++) {
                    JSONObject getAttribute = (JSONObject) getAttributes.get(j);
                    String getSegment = getAttribute.getString("key");
                    if (getSegment.equals("segment")) {
                        getSegments.add(getAttribute.getString("value"));
                    }
                }
            } catch (Exception e) {
            }
        }
        for (int i = 0; i < getSegments.size(); i++)
        {
            if (!clientSegmentRepository.existsByName(getSegments.get(i))) {
                final ClientSegment createClientSegment = new ClientSegment(getSegments.get(i));
                clientSegmentRepository.save(createClientSegment);
            }
        }
        JSONArray getClientSegments = new JSONArray(clientSegmentRepository.findAll());
        for (int i = 0; i < getClientSegments.length(); i++) {
            int count =0;
            JSONObject getClientSegment = (JSONObject) getClientSegments.get(i);
            for (int j = 0; j < getSegments.size(); j++)
            {
                if (getClientSegment.getString("name").equals(getSegments.get(j))) {
                    count = count + 1;
                }
            }
            listData.add(count);
            listSegment.add(getClientSegment.getString("name"));
        }
        itemData.put("label", listSegment);
        itemData.put("name", convertPostData.getString("segment"));
        itemData.put("data", listData);
        result.add(itemData.toString());
        log.info("/it4u/segments/client");
        return result.toString();
    }

    @ApiOperation(value = "Get all service plans")
    @GetMapping("/it4u/service/plans")
    public String getServicePlans() {
        ApiRequest apiRequest = new ApiRequest();
        List<String> listServicePlanName = new ArrayList<>();
        List<String> getListSPName = new ArrayList<>();
        List<String> result = new ArrayList<>();
        List<String> listSPName = new ArrayList<>();
        List<Integer> listData = new ArrayList<>();
        JSONObject itemData = new JSONObject();
        String getServicePlans = apiRequest.getRequestUCRM(urlUCRM + "/clients/services", authAppKey);
        JSONArray convertSPToJson = new JSONArray(getServicePlans);
        for (int i = 0; i < convertSPToJson.length(); i++) {
            JSONObject getItem = (JSONObject) convertSPToJson.get(i);
            String getServicePlanName = getItem.getString("servicePlanName");  
            String[] splitSPByDot = getServicePlanName.split("[.]");
            String[] splitSPByComma = splitSPByDot[0].split(",");
            listServicePlanName.add(splitSPByComma[0]);
        }
        for (int i = 0; i < listServicePlanName.size(); i++) {
            String getItem = listServicePlanName.get(i);
            boolean contains = getListSPName.contains(getItem);
            if (!contains) {
                getListSPName.add(getItem);
            }
        }
        for (int i = 0; i < getListSPName.size(); i++) {
            int count = 0;
            for (int j = 0; j < listServicePlanName.size(); j++) {
                if (getListSPName.get(i).equals(listServicePlanName.get(j))) {
                    count = count + 1;
                }
            }
            listData.add(count);
            listSPName.add(getListSPName.get(i));
        }
        itemData.put("label", listSPName);
        itemData.put("name", "Service Plans");
        itemData.put("data", listData);
        result.add(itemData.toString());
        log.info("/it4u/service/plans");
        return result.toString();
    }
    @ApiOperation(value = "Get all service status")
    @GetMapping("/it4u/service/status")
    public String getServiceStatus() {
        String result = "";
        ApiRequest apiRequest = new ApiRequest();
        String getInfoClient = apiRequest.getRequestUCRM(urlUCRM + "/clients", authAppKey);
        return getInfoClient;
    }
}

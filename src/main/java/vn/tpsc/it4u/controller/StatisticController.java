package vn.tpsc.it4u.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import io.swagger.annotations.Api;
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
    @GetMapping("/it4u/segments/client")
    public String getSegmentClient() {
        ApiRequest apiRequest = new ApiRequest();
        // JSONObject itemData = new JSONObject();
        List<String> result = new ArrayList<>();
        List<String> getSegments = new ArrayList<>();
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
            JSONObject itemData = new JSONObject();
            JSONObject getClientSegment = (JSONObject) getClientSegments.get(i);
            for (int j = 0; j < getSegments.size(); j++)
            {
                if (getClientSegment.getString("name").equals(getSegments.get(j))) {
                    count = count + 1;
                }
            }
            itemData.put("name", getClientSegment.getString("name"));
            itemData.put("y", count);
            result.add(itemData.toString());
        }
        log.info("/it4u/segments/client");
        return result.toString();
    }
}

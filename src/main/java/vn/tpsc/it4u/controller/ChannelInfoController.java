package vn.tpsc.it4u.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.modelmapper.ModelMapper;

import io.swagger.annotations.ApiOperation;
import vn.tpsc.it4u.model.ChannelAttribute;
import vn.tpsc.it4u.model.ChannelName;
import vn.tpsc.it4u.model.ChannelValue;
import vn.tpsc.it4u.payload.ChannelAttributeRequest;
import vn.tpsc.it4u.repository.ChannelAttributeRepository;
import vn.tpsc.it4u.repository.ChannelNameRespository;
import vn.tpsc.it4u.repository.ChannelValueRespository;
import vn.tpsc.it4u.util.ApiRequest;
import vn.tpsc.it4u.service.ChannelAttributeService;
import vn.tpsc.it4u.service.ConfigTokenService;
import vn.tpsc.it4u.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;

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
    private ChannelValueRespository channelValueRespository;

    @Autowired
    private ChannelNameRespository channelNameRespository;

    @Autowired
    private ChannelAttributeService channelAttributeService;

    @Autowired UserService userService;

    @Autowired
    ConfigTokenService configTokenService;

    @ApiOperation(value = "Get clients")
    @GetMapping("/it4u/clients")
    public String getAllClient() {
        ApiRequest apiRequest = new ApiRequest();
        /// clients/180/services
        JSONObject itemData = new JSONObject();
        List<String> result = new ArrayList<>();
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

    @ApiOperation(value = "Get clients")
    @GetMapping("/it4u/client/{id}")
    public String getInfoClient(@PathVariable(value = "id") String userId) {
        ApiRequest apiRequest = new ApiRequest();
        /// clients/180/services
        JSONObject itemData = new JSONObject();
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
        return itemData.toString();
    }

    @ApiOperation(value = "Post channel attribute")
    @PostMapping("it4u/channel/attribute")
    public ResponseEntity<?> PostChannelAttribute(@RequestBody final ChannelAttributeRequest postData) {
        ChannelAttribute getChannelAttribute = new ChannelAttribute(postData.getStatus());
        Set<ChannelName> channelName = new HashSet<>();
        ChannelName getChannelName = channelNameRespository.findByName(postData.getChannelName());
        channelName.add(getChannelName);
        getChannelAttribute.setChannelName(channelName);
        ChannelValue getChannelValue = channelValueRespository.findByServicePack(postData.getChannelValue());
        Set<ChannelValue> channelValue = new HashSet<>();
        channelValue.add(getChannelValue);
        getChannelAttribute.setChannelValue(channelValue);
        channelAttributeRepository.save(getChannelAttribute);
        return ResponseEntity.ok("ok");
    }

    @ApiOperation(value = "Post channel name")
    @PostMapping("it4u/channel/name")
    public ResponseEntity<?> postChannelName(@RequestBody final ChannelName postData) {
        ChannelName getChannelName = new ChannelName(postData.getName());
        channelNameRespository.save(getChannelName);
        return ResponseEntity.ok("ok");
    }

    @ApiOperation(value = "Post channel value")
    @PostMapping("it4u/channel/value")
    public ResponseEntity<?> postChannelValue(@RequestBody final ChannelValue postData) {
        ChannelValue getChannelValue = new ChannelValue(postData.getServicePack(), postData.getValue());
        channelValueRespository.save(getChannelValue);
        return ResponseEntity.ok("ok");
    }

    @ApiOperation(value = "Get channel attribute")
    @GetMapping("it4u/channel/attribute.all")
    public String getAllChannelAttribute() {
        List<String> result = new ArrayList<>();
        JSONObject data = new JSONObject();
        JSONArray getChannelAttribute = new JSONArray(channelAttributeService.findAll());
        for (int i = 0; i < getChannelAttribute.length(); i++) {
            JSONObject getItem = (JSONObject) getChannelAttribute.get(i);
            JSONArray getChannelName = getItem.getJSONArray("channelName");
            JSONObject channelName = (JSONObject) getChannelName.get(0);
            JSONArray getChannelValue = getItem.getJSONArray("channelValue");
            JSONObject channelValue = (JSONObject) getChannelValue.get(0);
            data.put("name", channelName.getString("name"));
            data.put("servicePack", channelValue.getString("servicePack"));
            data.put("value", channelValue.getString("value"));
            data.put("status", getItem.getString("status"));
            result.add(data.toString());
        }
        return result.toString();
    }

    @ApiOperation(value = "Get channel attribute to name")
    @GetMapping("it4u/channel/attribute.all.{name}")
    public String getChannelToName(@PathVariable(value = "name") String name) {
        // String result = "";
        List<String> result = new ArrayList<>();
        JSONObject data = new JSONObject();
        JSONArray getChannelAttribute = new JSONArray(channelAttributeService.findChannelName(name));
        for (int i = 0; i < getChannelAttribute.length(); i++) {
            JSONObject getItem = (JSONObject) getChannelAttribute.get(i);
            if (!getItem.getString("status").equals("Online")) {
                JSONArray getChannelName = getItem.getJSONArray("channelName");
                JSONObject channelName = (JSONObject) getChannelName.get(0);
                JSONArray getChannelValue = getItem.getJSONArray("channelValue");
                JSONObject channelValue = (JSONObject) getChannelValue.get(0);
                data.put("name", channelName.getString("name"));
                data.put("servicePack", channelValue.getString("servicePack"));
                data.put("value", channelValue.getString("value"));
            }
            result.add(data.toString());
        }
        
        return result.toString();
    }

    @ApiOperation(value = "Monitor daily traffic and client for customers")
    @GetMapping("it4u/monitor/daily/trafficAndClient")
    public String getDailyTrafficAndClient()
    {
        String result = "";
        String title = "Báo cáo theo ngày";
        ApiRequest apiRequest = new ApiRequest();
        JSONArray getCookies = new JSONArray(configTokenService.findAll());
        JSONArray getUsers = new JSONArray(userService.findAll());
        JSONObject body = (JSONObject) getCookies.get(0);
        csrfToken = body.getString("csrfToken");
        unifises = body.getString("unifises");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long endTime = timestamp.getTime()+7*60*60*1000;
        Long startTime = endTime-2*24*60*60*1000;
        String postData = "{\"attrs\":[\"wlan_bytes\",\"wlan-num_sta\",\"time\"], \"start\":\"" + startTime + "\", \"end\":\"" + endTime + "\"}";
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
                        JSONObject getPosEnd = (JSONObject) getData.get(k+1);
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
                //TODO: handle exception
            }
        }
        return result;
    }

    @ApiOperation(value = "Monitor weekly traffic and client for customers")
    @GetMapping("it4u/monitor/weekly/trafficAndClient")
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
        String postDataFirstTime = "{\"attrs\":[\"wlan_bytes\",\"wlan-num_sta\",\"time\"], \"start\":\"" + startTimeFirstWeek
                + "\", \"end\":\"" + startTimeSecondWeek + "\"}";
        String postDataSecondTime = "{\"attrs\":[\"wlan_bytes\",\"wlan-num_sta\",\"time\"], \"start\":\"" + startTimeSecondWeek
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

    public String notification(String registrationId, int startData,  int endData, String title) {
        String result = "";
        ApiRequest apiRequest = new ApiRequest();
        Boolean checkNotif = false;
        String body = "";
        if (startData < endData && endData > 20) {
            if ( Math.round(endData / startData) > 1) {
                checkNotif = true;
                body = "Số lượng thiết bị sử dụng tăng nhanh, login app IT4U để xem chi tiết.";
            }
            if ( Math.round(endData / startData) > 2) {
                checkNotif = true;
                body = "Số lượng thiết bị sử dụng tăng rất nhanh, nếu có phát hiện mạng chập chờn vui lòng liên hệ: 02363 575788";
            }
        }
        if ((startData > endData) && (startData > 20)) {
            if ((startData / endData) > 1) {
                checkNotif = true;
                body = "Số lượng thiết bị sử dụng giảm, login vào app IT4U để xem chi tiết.";
            }
        }
        if (checkNotif) {
            String dataPost = "{\"registration_ids\":[\"" + registrationId + "\"],\"notification\": {\"title\": \"" + title + "\", \"body\": \"" + body +"\"}}";
            try {
                result = apiRequest.getConnectionFirebase(urlFirebase, senderId, getAccessToken,  dataPost);
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

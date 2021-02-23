package vn.tpsc.it4u.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

// import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import vn.tpsc.it4u.model.SitesName;
import vn.tpsc.it4u.model.SystemConfig;
import vn.tpsc.it4u.repository.SitesNameRepository;
import vn.tpsc.it4u.service.SitesNameService;
import vn.tpsc.it4u.service.SystemConfigService;
import vn.tpsc.it4u.model.ConfigToken;
import vn.tpsc.it4u.model.Role;
import vn.tpsc.it4u.service.ConfigTokenService;
import vn.tpsc.it4u.repository.ConfigTokenRepository;
import vn.tpsc.it4u.security.CustomUserDetails;
import vn.tpsc.it4u.security.CurrentUser;
import vn.tpsc.it4u.util.ApiRequest;
import vn.tpsc.it4u.util.ApiResponseUtils;
import vn.tpsc.it4u.util.Calculator;

@RestController
@Slf4j
@RequestMapping("${app.api.version}")
public class DashboardController {
    @Value("${app.ubnt.url}")
    public String urlIt4u;

    @Value("${app.ubnt.csrf_token}")
    public String csrfToken;

    @Value("${app.ubnt.unifises}")
    public String unifises;

    @Value("${app.ubnt.username}")
    private String username;

    @Value("${app.ubnt.password}")
    private String password;

    @Value("${app.dev.url}")
    private String urlDev;

    @Value("${app.dev.token}")
    private String tokenDev;

    @Value("${app.zabbix.url}")
    private String urlZabbix;

    @Value("${app.zabbix.username}")
    private String usernameZabbix;

    @Value("${app.zabbix.password}")
    private String passwordZabbix;

    @Value("${app.zabbix.token}")
    private String tokenZabbix;

    String sitesid="/self/sites";
    String printUrlVoucher = "https://ubnt.cloud.tpsc.vn/print/hotspot/vouchers/s";
    
    @Autowired 
    ApiResponseUtils apiResponse;

    @Autowired
    SitesNameRepository sitesNameRepository;

    @Autowired
    SitesNameService sitenameService;

    @Autowired
    ConfigTokenRepository configTokenRepository;

    @Autowired
    ConfigTokenService configTokenService;

    @Autowired
    SystemConfigService systemConfigService;
    @ApiOperation(value = "Cookies IT4U")
    @GetMapping("/it4u/cookies")
    public String getCookies() {
        ApiRequest apiRequest = new  ApiRequest();
        SystemConfig systemConfig = systemConfigService.findSystemConfig();
        username = systemConfig.getUsernameUbnt();
        password = systemConfig.getPwUbnt();
        String dataPost = "{'username':'" + username + "','password':'" + password + "','remember':'true','strict':'true'}";
        String getCookies = apiRequest.postRequestIt4u(urlIt4u, "/login", dataPost);
        String [] arr = getCookies.split(";");
        String getToken = arr[0];
        String [] arrToken = getToken.split("=");
        csrfToken = arrToken[1];
        String getUnifise = arr[2];
        String [] arrUnifise = getUnifise.split("=");
        unifises = arrUnifise[1];
        try {   
            return getCookies.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }


    @ApiOperation(value = "Post cookies ubnt")
    @GetMapping("/it4u/postCookiesUbnt")
    public String getPostCookiesUbnt() {
        String result = "Created Succesfully";
        try {
            final ConfigToken createCookie = new ConfigToken(csrfToken, unifises);
            configTokenRepository.save(createCookie);
            return result;
        } catch (Exception e) {
            return e.toString();
        }
    }

    @ApiOperation(value = "Check cookies ubnt")
    @GetMapping("/it4u/checkCookiesUbnt")
    public ResponseEntity<?> checkCookiesUbnt(Locale locale) {
        ApiRequest apiRequest = new ApiRequest();
        String getSites = apiRequest.getRequestApi(urlIt4u, sitesid, csrfToken, unifises);
        try {
            JSONObject jsonResult = new JSONObject(getSites);
            JSONArray data = jsonResult.getJSONArray("data");
            return ResponseEntity.ok(apiResponse.success(200, locale));
        } catch (Exception e) {
            String dataPost = "{\"username\":\"" + username + "\",\"password\":\"" + password
                    + "\",\"remember\":\"true\",\"strict\":\"true\"}";
            String getCookies = apiRequest.postRequestIt4u(urlIt4u, "/login", dataPost);
            String[] arr = getCookies.split(";");
            String getToken = arr[0];
            String[] arrToken = getToken.split("=");
            String getUnifise = arr[2];
            String[] arrUnifise = getUnifise.split("=");
            configTokenService.updateCookies(arrToken[1], arrUnifise[1]);
            return ResponseEntity.ok(apiResponse.success("csrfToken: " + arrToken[1] +";" + "unifises" + arrUnifise[1], locale));
        }
    }

    @ApiOperation(value = "Get cookies ubnt")
    @GetMapping("/it4u/getCookiesUbnt")
    public String getCookiesUbnt() {
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        csrfToken = body.getString("csrfToken");
        unifises = body.getString("unifises");
        return unifises;
    }

    @ApiOperation(value = "Cookies Zabbix")
    @GetMapping("/it4u/cookiesZabbix")
    public String getCookiesZabbix() {
        ApiRequest apiRequest = new  ApiRequest();
        String dataPostZabbix = "{\"jsonrpc\": \"2.0\",\"method\": \"user.login\",\"params\": {\"user\": \"" + usernameZabbix + "\",\"password\": \"" + passwordZabbix + "\"},\"id\": 1}";
        String getCookiesZabbix = apiRequest.postLoginZabbix(urlZabbix, "/api_jsonrpc.php", dataPostZabbix);
        JSONObject getToken = new JSONObject(getCookiesZabbix);
        tokenZabbix = getToken.getString("result");
        try {   
            return getCookiesZabbix.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    @GetMapping("/it4u/getSitename")
    public String getSiteName() {
        JSONObject result = new JSONObject();
        JSONArray getSitename = new JSONArray(sitenameService.findAll());
        String siteName = "";
        String idName = "";
        List<String> dataList = new ArrayList<>();
        for (int i=0; i<getSitename.length(); i++) {
            JSONObject getData = (JSONObject) getSitename.get(i);
            try {
                siteName = getData.getString("sitename");
                idName = getData.getString("idname");
            } catch (Exception e) {
            }
            result.put("id", idName);
            result.put("name", siteName);
            dataList.add(result.toString());
        }        
        return dataList.toString();
    }

    // Register sitesname
    @ApiOperation(value = "Create sites name request")
    @GetMapping("/it4u/createSitesName")
    public Boolean createSitesName() {        
        ApiRequest apiRequest = new ApiRequest();
        JSONArray data = new JSONArray();
        try {
            String getSites = apiRequest.getRequestApi(urlIt4u, sitesid, csrfToken, unifises);
            JSONObject jsonResult = new JSONObject(getSites);
            data = jsonResult.getJSONArray("data");
        } catch (Exception e) {
        }
        String siteName = "";
        String idName = "";
        for (int i = 0; i < data.length(); i++) {
            JSONObject getData = (JSONObject) data.get(i);
            try {
                siteName = getData.getString("desc");
                idName = getData.getString("name");
            } catch (Exception e) {
            }

            if (!sitesNameRepository.existsBySitename(siteName)) {
                try {
                    final SitesName createSitename = new SitesName(siteName, idName);
                    sitesNameRepository.save(createSitename);
                } catch (Exception e) {
                    SitesName createSitename = sitesNameRepository.findByIdname(idName);
                    createSitename.setSitename(siteName);
                    sitesNameRepository.save(createSitename);
                }
            }                          
        }
        return true;
    }


    @ApiOperation(value = "Sites id")
    @GetMapping("/it4u/sites")
    public String getSitesId() {
        ApiRequest apiRequest = new  ApiRequest();
        JSONObject result = new JSONObject();
        List<String> dataList = new ArrayList<>();
        JSONArray data = new JSONArray();
        try {   
            String getSites = apiRequest.getRequestApi(urlIt4u,sitesid,csrfToken,unifises);
            JSONObject jsonResult = new JSONObject(getSites);
            data = jsonResult.getJSONArray("data");
        } catch (Exception e) {
        }
        String siteName = "";
        String idName = "";
        for (int i=0;i<data.length();i++)
        {
            JSONObject getData = (JSONObject) data.get(i);
            try {
                siteName = getData.getString("desc");
                idName = getData.getString("name");
            } catch (Exception e) {
            }
            result.put("id",idName);
            result.put("name",siteName);
            dataList.add(result.toString());
        }
        return dataList.toString();
    }
    
    @ApiOperation(value = "Client Essid")
    @GetMapping("it4u/{id}/clientEssid")
    public String getClientEssid(@PathVariable(value = "id") String userId,
        @CurrentUser CustomUserDetails currentUser) {
        ApiRequest apiRequest = new  ApiRequest();
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> listSsid = new ArrayList<>();
        List<String> result = new ArrayList<>();
        JSONObject getResult = new JSONObject();
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        csrfToken = body.getString("csrfToken");
        unifises = body.getString("unifises");
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/sta",csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        try {
            JSONObject itemDevice0 = (JSONObject) data.get(0);
            String devices0 = itemDevice0.getString("essid");
            listSsid.add(devices0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            for (int i = 0; i < data.length(); i++) {
                int k = 0;
                JSONObject listDevice = (JSONObject) data.get(i);
                for (int j = 0; j < listSsid.size(); j++) {
                    if (listDevice.getString("essid").equals(listSsid.get(j))) {
                        k = k + 1;
                    }
                }
                if (k == 0) {
                    listSsid.add(listDevice.getString("essid"));
                }
            }
        } catch (Exception e) {
            //TODO: handle exception
        }

        for (int j=0; j<listSsid.size(); j++) {
            int k = 0;
            try {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject listDevice = (JSONObject) data.get(i);
                    if (listDevice.getString("essid").equals(listSsid.get(j))) {
                        k = k + 1;
                    }
                }
            } catch (Exception e) {
                //TODO: handle exception
            }
            getResult.put("name", listSsid.get(j));
            getResult.put("y",k);
            result.add(getResult.toString());  
        }
        return result.toString();
    }

    @ApiOperation(value = "Current client usage")
    @GetMapping("it4u/{id}/clientUsage")
    public String getClientUsage(@PathVariable(value = "id") String userId, @CurrentUser CustomUserDetails currentUser) {
        JSONObject getResult = new JSONObject();
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> result = new ArrayList<>();
        ApiRequest apiRequest = new  ApiRequest();
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/device/",csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i=0; i<data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            getResult.put("name",getInfo.getString("name"));
            getResult.put("y",getInfo.getInt("num_sta"));
            result.add(getResult.toString());
        }
        return result.toString();
    }

    @ApiOperation(value = "Get mac AP")
    @GetMapping("it4u/{id}/getMacAp")
    public String getMacAp(@PathVariable(value = "id") String userId, @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        JSONObject getResult = new JSONObject();
        List<String> result = new ArrayList<>();
        ApiRequest apiRequest = new  ApiRequest();
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/device/",csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i=0; i<data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            getResult.put("name",getInfo.getString("name"));
            getResult.put("mac",getInfo.getString("mac"));
            result.add(getResult.toString());
        }
        return result.toString();
    }

    @ApiOperation(value = "Current traffic usage")
    @GetMapping("it4u/{id}/trafficUsage")
    public String getTrafficUsage(@PathVariable(value = "id") String userId, @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        JSONObject getResult = new JSONObject();
        List<String> result = new ArrayList<>();
        ApiRequest apiRequest = new  ApiRequest();
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/device/",csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        Calculator getCalculator = new Calculator();
        for (int i=0; i<data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            double convertTraffic = 0;
            try {
                long traffic = getInfo.getLong("bytes");
                convertTraffic = getCalculator.convertBytesToGb(traffic);
            } catch (Exception e) {
                //TODO: handle exception
            }
            getResult.put("name", getInfo.getString("name"));
            getResult.put("y", convertTraffic);
            result.add(getResult.toString());
        }
        return result.toString();
    }

    @ApiOperation(value = "Per radio type")
    @GetMapping("it4u/{id}/radioType")
    public String getClientRadio(@PathVariable(value = "id") String userId, @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        Integer lowRadio = 0;
        Integer highRadio = 0;
        List<String> result = new ArrayList<>();
        JSONObject getLowRadio = new JSONObject();
        JSONObject getHighRadio = new JSONObject();
        ApiRequest apiRequest = new ApiRequest();
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/device/",csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i=0; i<data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            JSONArray getRadioTable = getInfo.getJSONArray("radio_table_stats");
            for (int j=0; j<getRadioTable.length(); j++) {
                JSONObject getInfoRadio = (JSONObject) getRadioTable.get(j);
                if (getInfoRadio.getString("radio").equals("ng")) {
                    lowRadio = lowRadio + getInfoRadio.getInt("num_sta");
                }
                if (getInfoRadio.getString("radio").equals("na")) {
                    highRadio = highRadio + getInfoRadio.getInt("num_sta");
                }
            }
        }
        getLowRadio.put("name","2.4 GHz");
        getLowRadio.put("y",lowRadio);
        result.add(getLowRadio.toString());
        getHighRadio.put("name","5 GHz");
        getHighRadio.put("y",highRadio);
        result.add(getHighRadio.toString());
        return result.toString();
    }

    @ApiOperation(value = "Quick look")
    @PostMapping("it4u/{id}/quickLook")
    public String getQuickLook(@PathVariable(value = "id") String userId, @RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser) {
        ApiRequest apiRequest = new ApiRequest();
        List<String> result = new ArrayList<>();
        String getClients = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/sta/",csrfToken,unifises);
        String getDevices = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/device/",csrfToken,unifises);
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        try {
            String longestConn = dashboard.longestConn(getClients, postData);
            String mostActiveClient = dashboard.mostActiveClient(getClients, postData);
            String mostActiveAp = dashboard.mostActiveAp(getDevices, postData);
            result.add(longestConn);
            result.add(mostActiveClient);
            result.add(mostActiveAp);
        } catch (Exception e) {
            //TODO: handle exception
        }
        return result.toString();
    }

    @ApiOperation(value = "Hourly Client")
    @PostMapping("it4u/{id}/hourlyClient")
    public String getHourlyClient(@RequestBody String postData,@PathVariable(value = "id") String userId, @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> result = new ArrayList<>();
        List<Integer> listClient = new ArrayList<Integer>();
        List<Double> listTraffic = new ArrayList<Double>();
        List<Double> listIncreaseTraffic = new ArrayList<Double>();
        JSONObject listClientJson = new JSONObject();
        JSONObject listTrafficJson = new JSONObject();
        JSONObject listIncreaseTrafficJson = new JSONObject();
        JSONObject client = new JSONObject();
        JSONObject traffic = new JSONObject();
        JSONObject getPostData = new JSONObject(postData);
        client.put("valueSuffix", " Client");
        traffic.put("valueSuffix", " GB");
        ApiRequest apiRequest = new ApiRequest();
        // String getMinute = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/hourly.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPosZero = (JSONObject) dataHourly.get(0);
        Integer countClient0 = getPosZero.getInt("wlan-num_sta");
        Long countTraffic0 = getPosZero.getLong("wlan_bytes");
        Calculator getCalculator = new Calculator();
        double convertTraffic0 = getCalculator.convertBytesToGb(countTraffic0);
        Double sumTraffic = convertTraffic0;
        listClient.add(countClient0);
        listTraffic.add(convertTraffic0);
        listIncreaseTraffic.add(sumTraffic);
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Integer countClient = getPosEnd.getInt("wlan-num_sta");
                Long countTraffic = getPosEnd.getLong("wlan_bytes");
                double convertTraffic = getCalculator.convertBytesToGb(countTraffic);
                sumTraffic = sumTraffic + convertTraffic;
                double resultSumTraffic = Math.round(sumTraffic * 100.0) / 100.0;
                listClient.add(countClient);
                listTraffic.add(convertTraffic);
                listIncreaseTraffic.add(resultSumTraffic);
            }
        }
        // pointStart: 1348226400000,
        // pointInterval: 60 * 60 * 1000,
        Long pointStart = getPosZero.getLong("time");
        listClientJson.put("pointStart",pointStart + 7*60*60*1000);
        listClientJson.put("pointInterval",60 * 60 * 1000);
        listClientJson.put("name", getPostData.getString("client"));
        listClientJson.put("data", listClient);
        listClientJson.put("yAxis",1);
        listClientJson.put("tooltip",client);

        // Long pointStart = getPosZero.getLong("time");
        listTrafficJson.put("pointStart", pointStart + 7 * 60 * 60 * 1000);
        listTrafficJson.put("pointInterval", 60 * 60 * 1000);
        listTrafficJson.put("name", getPostData.getString("traffic"));
        listTrafficJson.put("data", listTraffic);
        listTrafficJson.put("tooltip",traffic);
        listTrafficJson.put("yAxis",0);

        listIncreaseTrafficJson.put("pointStart", pointStart + 7 * 60 * 60 * 1000);
        listIncreaseTrafficJson.put("pointInterval", 60 * 60 * 1000);
        listIncreaseTrafficJson.put("name", getPostData.getString("total_traffic"));
        listIncreaseTrafficJson.put("data", listIncreaseTraffic);
        listIncreaseTrafficJson.put("tooltip", traffic);
        listIncreaseTrafficJson.put("yAxis", 0);

        result.add(listClientJson.toString());
        result.add(listTrafficJson.toString());
        result.add(listIncreaseTrafficJson.toString());
        log.info(currentUser.getUsername() + " - it4u/" + userId + "/hourlyClient");
        return result.toString();
    }

    @ApiOperation(value = "5 minutes Client")
    @PostMapping("it4u/{id}/5MinutesClient")
    public String getMinutesClient(@RequestBody String postData,@PathVariable(value = "id") String userId,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> result = new ArrayList<>();
        List<Integer> listClient = new ArrayList<Integer>();
        List<Double> listTraffic = new ArrayList<Double>();
        List<Double> listIncreaseTraffic = new ArrayList<Double>();
        JSONObject listClientJson = new JSONObject();
        JSONObject listTrafficJson = new JSONObject();
        JSONObject listIncreaseTrafficJson = new JSONObject();
        JSONObject client = new JSONObject();
        JSONObject traffic = new JSONObject();
        JSONObject getPostData = new JSONObject(postData);
        client.put("valueSuffix", " Client");
        traffic.put("valueSuffix", " GB");
        ApiRequest apiRequest = new ApiRequest();
        // String getMinute = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        String getMinute = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getMinute);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPosZero = (JSONObject) dataHourly.get(0);
        Integer countClient0 = getPosZero.getInt("wlan-num_sta");
        long countTraffic0 = getPosZero.getLong("wlan_bytes");
        Calculator getCalculator = new Calculator();
        double convertTraffic0 = getCalculator.convertBytesToGb(countTraffic0);
        Double sumTraffic = convertTraffic0;
        listClient.add(countClient0);
        listTraffic.add(convertTraffic0);
        listIncreaseTraffic.add(sumTraffic);
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Integer countClient = getPosEnd.getInt("wlan-num_sta");
                Long countTraffic = getPosEnd.getLong("wlan_bytes");
                double convertTraffic = getCalculator.convertBytesToGb(countTraffic);
                sumTraffic = sumTraffic + convertTraffic;
                double resultSumTraffic = Math.round(sumTraffic * 100.0) / 100.0;
                listClient.add(countClient);
                listTraffic.add(convertTraffic);
                listIncreaseTraffic.add(resultSumTraffic);
            }
        }
        Long pointStart = getPosZero.getLong("time");
        listClientJson.put("pointStart", pointStart + 7 * 60 * 60 * 1000);
        listClientJson.put("pointInterval", 5 * 60 * 1000);
        listClientJson.put("name",getPostData.getString("client"));
        listClientJson.put("data", listClient);
        listClientJson.put("yAxis",1);
        listClientJson.put("tooltip",client);

        listTrafficJson.put("pointStart", pointStart + 7 * 60 * 60 * 1000);
        listTrafficJson.put("pointInterval", 5 * 60 * 1000);
        listTrafficJson.put("name", getPostData.getString("traffic"));
        listTrafficJson.put("data", listTraffic);
        listTrafficJson.put("tooltip",traffic);
        listTrafficJson.put("yAxis",0);

        listIncreaseTrafficJson.put("pointStart", pointStart + 7 * 60 * 60 * 1000);
        listIncreaseTrafficJson.put("pointInterval", 5 * 60 * 1000);
        listIncreaseTrafficJson.put("name", getPostData.getString("total_traffic"));
        listIncreaseTrafficJson.put("data", listIncreaseTraffic);
        listIncreaseTrafficJson.put("tooltip", traffic);
        listIncreaseTrafficJson.put("yAxis", 0);

        result.add(listClientJson.toString());
        result.add(listTrafficJson.toString());
        result.add(listIncreaseTrafficJson.toString());
        log.info(currentUser.getUsername() + " - it4u/" + userId + "/5MinutesClient");
        return result.toString();
    }

    @ApiOperation(value = "Daily Client")
    @PostMapping("it4u/{id}/dailyClient")
    public String getDailyClient(@RequestBody String postData,@PathVariable(value = "id") String userId,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> result = new ArrayList<>();
        List<Integer> listClient = new ArrayList<Integer>();
        List<Double> listTraffic = new ArrayList<Double>();
        List<Double> listIncreaseTraffic = new ArrayList<Double>();
        Double sumTraffic = 0.0;
        JSONObject listClientJson = new JSONObject();
        JSONObject listTrafficJson = new JSONObject();
        JSONObject listIncreaseTrafficJson = new JSONObject();
        JSONObject client = new JSONObject();
        JSONObject traffic = new JSONObject();
        JSONObject getPostData = new JSONObject(postData);
        client.put("valueSuffix", " Client");
        traffic.put("valueSuffix", " GB");
        ApiRequest apiRequest = new ApiRequest();
        // String getMinute = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/daily.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPosZero = (JSONObject) dataHourly.get(0);
        Integer countClient0 = getPosZero.getInt("wlan-num_sta");
        Long countTraffic0 = getPosZero.getLong("wlan_bytes");
        Calculator getCalculator = new Calculator();
        double convertTraffic0 = getCalculator.convertBytesToGb(countTraffic0);
        listClient.add(countClient0);
        listTraffic.add(convertTraffic0);
        sumTraffic = convertTraffic0;
        listIncreaseTraffic.add(sumTraffic);
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Integer countClient = getPosEnd.getInt("wlan-num_sta");
                Long countTraffic = getPosEnd.getLong("wlan_bytes");
                double convertTraffic = getCalculator.convertBytesToGb(countTraffic);
                sumTraffic = sumTraffic + convertTraffic;
                double resultSumTraffic = Math.round(sumTraffic * 100.0) / 100.0;
                listClient.add(countClient);
                listTraffic.add(convertTraffic);
                listIncreaseTraffic.add(resultSumTraffic);
            }
        }
        Long pointStart = getPosZero.getLong("time");
        listClientJson.put("pointStart", pointStart + 7 * 60 * 60 * 1000);
        listClientJson.put("pointInterval", 24 * 60 * 60 * 1000);
        listClientJson.put("name", getPostData.getString("client"));
        listClientJson.put("data", listClient);
        listClientJson.put("yAxis",1);
        listClientJson.put("tooltip",client);

        listTrafficJson.put("pointStart", pointStart + 7 * 60 * 60 * 1000);
        listTrafficJson.put("pointInterval",24 * 60 * 60 * 1000);
        listTrafficJson.put("name",getPostData.getString("traffic"));
        listTrafficJson.put("data", listTraffic);
        listTrafficJson.put("tooltip",traffic);
        listTrafficJson.put("yAxis",0);

        listIncreaseTrafficJson.put("pointStart", pointStart + 7 * 60 * 60 * 1000);
        listIncreaseTrafficJson.put("pointInterval",24 * 60 * 60 * 1000);
        listIncreaseTrafficJson.put("name", getPostData.getString("total_traffic"));
        listIncreaseTrafficJson.put("data", listIncreaseTraffic);
        listIncreaseTrafficJson.put("tooltip", traffic);
        listIncreaseTrafficJson.put("yAxis", 0);

        result.add(listClientJson.toString());
        result.add(listTrafficJson.toString());
        result.add(listIncreaseTrafficJson.toString());
        log.info(currentUser.getUsername() + " - it4u/" + userId + "/dailyClient");
        return result.toString();
    }

    @ApiOperation(value = "Daily AP")
    @PostMapping("it4u/{id}/dailyAP")
    public String getDailyAP(@RequestBody String postData,@PathVariable(value = "id") String userId,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> result = new ArrayList<>();
        List<Integer> listClient = new ArrayList<Integer>();
        List<Long> listTraffic = new ArrayList<Long>();
        JSONObject listClientJson = new JSONObject();
        JSONObject listTrafficJson = new JSONObject();

        ApiRequest apiRequest = new ApiRequest();
        // String getMinute = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/daily.ap/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPosZero = (JSONObject) dataHourly.get(0);
        Integer countClient0 = getPosZero.getInt("num_sta");
        Long countTraffic0 = getPosZero.getLong("bytes");
        listClient.add(countClient0);
        listTraffic.add(countTraffic0);
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Integer countClient = getPosEnd.getInt("num_sta");
                Long countTraffic = getPosEnd.getLong("bytes");
                listClient.add(countClient);
                listTraffic.add(countTraffic);
            }
        }
        listClientJson.put("name","Client");
        listClientJson.put("data", listClient);
        listClientJson.put("yAxis",1);
        listTrafficJson.put("name","Traffic");
        listTrafficJson.put("data", listTraffic);
        listTrafficJson.put("yAxis",0);
        result.add(listClientJson.toString());
        result.add(listTrafficJson.toString());
        return result.toString();
    }

    @ApiOperation(value = "Hourly AP")
    @PostMapping("it4u/{id}/hourlyAP")
    public String getHourlyAP(@RequestBody String postData,@PathVariable(value = "id") String userId,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> result = new ArrayList<>();
        List<Integer> listClient = new ArrayList<Integer>();
        List<Long> listTraffic = new ArrayList<Long>();
        JSONObject listClientJson = new JSONObject();
        JSONObject listTrafficJson = new JSONObject();

        ApiRequest apiRequest = new ApiRequest();
        // String getMinute = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/hourly.ap/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPosZero = (JSONObject) dataHourly.get(0);
        Integer countClient0 = getPosZero.getInt("num_sta");
        Long countTraffic0 = getPosZero.getLong("bytes");
        listClient.add(countClient0);
        listTraffic.add(countTraffic0);
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Integer countClient = getPosEnd.getInt("num_sta");
                Long countTraffic = getPosEnd.getLong("bytes");
                listClient.add(countClient);
                listTraffic.add(countTraffic);
            }
        }
        listClientJson.put("name","Client");
        listClientJson.put("data", listClient);
        listClientJson.put("yAxis",1);
        listTrafficJson.put("name","Traffic");
        listTrafficJson.put("data", listTraffic);
        listTrafficJson.put("yAxis",0);
        result.add(listClientJson.toString());
        result.add(listTrafficJson.toString());
        return result.toString();
    }

    @ApiOperation(value = "5 minutes AP")
    @PostMapping("it4u/{id}/5MinutesAP")
    public String get5MinutesAP(@RequestBody String postData,@PathVariable(value = "id") String userId,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> result = new ArrayList<>();
        List<Integer> listClient = new ArrayList<Integer>();
        List<Long> listTraffic = new ArrayList<Long>();
        JSONObject listClientJson = new JSONObject();
        JSONObject listTrafficJson = new JSONObject();

        ApiRequest apiRequest = new ApiRequest();
        // String getMinute = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.ap/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPosZero = (JSONObject) dataHourly.get(0);
        Integer countClient0 = getPosZero.getInt("num_sta");
        Long countTraffic0 = getPosZero.getLong("bytes");
        listClient.add(countClient0);
        listTraffic.add(countTraffic0);
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Integer countClient = getPosEnd.getInt("num_sta");
                Long countTraffic = getPosEnd.getLong("bytes");
                listClient.add(countClient);
                listTraffic.add(countTraffic);
            }
        }
        listClientJson.put("name","Client");
        listClientJson.put("data", listClient);
        listClientJson.put("yAxis",1);
        listTrafficJson.put("name","Traffic");
        listTrafficJson.put("data", listTraffic);
        listTrafficJson.put("yAxis",0);
        result.add(listClientJson.toString());
        result.add(listTrafficJson.toString());
        return result.toString();
    }
    
    @ApiOperation(value = "Get time hourly")
    @PostMapping("it4u/{id}/getTimeHourly")
    public String getTimeHourly(@RequestBody String postData,@PathVariable(value = "id") String userId, @CurrentUser CustomUserDetails currentUser) {
        List<String> listTime = new ArrayList<>();
        List<String> listResult = new ArrayList<>();
        JSONObject listResultTraffic = new JSONObject();
        JSONObject listResultClient = new JSONObject();
        long getTimeMax = 0;
        long getTimeMin = 0;
        long sumTraffic = 0;
        long avgTraffic = 0;
        int sum = 0;
        int avg = 0;
        int k = 0;
        ApiRequest apiRequest = new ApiRequest();
        // condition get data
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/hourly.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPos = (JSONObject) dataHourly.get(0);
        Integer maxClient = getPos.getInt("wlan-num_sta");
        Integer minClient = getPos.getInt("wlan-num_sta");
        Long maxTraffic = getPos.getLong("wlan_bytes");
        Long minTraffic = getPos.getLong("wlan_bytes");
        Calculator getCalculator = new Calculator();
        Long time0 = getPos.getLong("time");
        String uptime0 = getCalculator.ConvertSecondToDate(time0);
        listTime.add(uptime0.toString());
        
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Long time = getPosEnd.getLong("time");  
                String uptime = getCalculator.ConvertSecondToDate(time);
                if (maxClient <= getPosStart.getInt("wlan-num_sta")) {
                    maxClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMax = getPosStart.getLong("time");
                }
                if (minClient >= getPosStart.getInt("wlan-num_sta")) {
                    minClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMin = getPosStart.getLong("time");
                }
                if (maxTraffic <= getPosStart.getLong("wlan_bytes")) {
                    maxTraffic = getPosStart.getLong("wlan_bytes");
                }
                if (minTraffic >= getPosStart.getLong("wlan_bytes")) {
                    minTraffic = getPosStart.getLong("wlan_bytes");
                }
                sumTraffic = sumTraffic + getPosStart.getLong("wlan_bytes");
                sum = sum + getPosStart.getInt("wlan-num_sta");
                k = k + 1;
                listTime.add(uptime.toString());
				
            }

        }
        avgTraffic = Math.round(sumTraffic/k);
        avg = Math.round(sum/k);
        JSONObject getPostData = new JSONObject(postData);
        String timeMax = getCalculator.ConvertSecondToDate(getTimeMax);
        String timeMin = getCalculator.ConvertSecondToDate(getTimeMin);
        // listResult.put("time",listTime);
        // listResultClient.put("timeMax",timeMax);
        // listResult.put("timeMin",timeMin);
        listResultTraffic.put("name", getPostData.getString("traffic"));
        listResultTraffic.put("max", getCalculator.convertBytesToGb(maxTraffic) + " GB");
        listResultTraffic.put("min", getCalculator.convertBytesToGb(minTraffic) + " GB");
        listResultTraffic.put("average", getCalculator.convertBytesToGb(avgTraffic) + " GB");
        
        listResultClient.put("name", getPostData.getString("client"));
        listResultClient.put("max",maxClient + " " + getPostData.getString("client_init"));
        listResultClient.put("min",minClient + " " + getPostData.getString("client_init"));
        listResultClient.put("average",avg  + " " + getPostData.getString("client_init"));

        listResult.add(listResultClient.toString());
        listResult.add(listResultTraffic.toString());
        return listResult.toString();
    }

    @ApiOperation(value = "Get time 5 minutes")
    @PostMapping("it4u/{id}/getTimeMinute")
    public String getTimeMinute(@RequestBody String postData,@PathVariable(value = "id") String userId, @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> listTime = new ArrayList<>();
        List<String> listResult = new ArrayList<>();
        JSONObject listResultTraffic = new JSONObject();
        JSONObject listResultClient = new JSONObject();
        long getTimeMax = 0;
        long getTimeMin = 0;
        long sumTraffic = 0;
        long avgTraffic = 0;
        int sum = 0;
        int avg = 0;
        int k = 0;
        ApiRequest apiRequest = new ApiRequest();
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPos = (JSONObject) dataHourly.get(0);
        Integer maxClient = getPos.getInt("wlan-num_sta");
        Integer minClient = getPos.getInt("wlan-num_sta");
        Long maxTraffic = getPos.getLong("wlan_bytes");
        Long minTraffic = getPos.getLong("wlan_bytes");
        Calculator getCalculator = new Calculator();
        Long time0 = getPos.getLong("time");
        String uptime0 = getCalculator.ConvertSecondToDate(time0);
        listTime.add(uptime0.toString());
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Long time = getPosEnd.getLong("time");  
                String uptime = getCalculator.ConvertSecondToDate(time);
                if (maxClient <= getPosStart.getInt("wlan-num_sta")) {
                    maxClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMax = getPosStart.getLong("time");
                }
                if (minClient >= getPosStart.getInt("wlan-num_sta")) {
                    minClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMin = getPosStart.getLong("time");
                }
                if (maxTraffic <= getPosStart.getLong("wlan_bytes")) {
                    maxTraffic = getPosStart.getLong("wlan_bytes");
                }
                if (minTraffic >= getPosStart.getLong("wlan_bytes")) {
                    minTraffic = getPosStart.getLong("wlan_bytes");
                }
                sumTraffic = sumTraffic + getPosStart.getLong("wlan_bytes");
                sum = sum + getPosStart.getInt("wlan-num_sta");
                k = k + 1;
                listTime.add(uptime.toString());
				
            }

        }
        avgTraffic = Math.round(sumTraffic / k);
        avg = Math.round(sum/k);
        JSONObject getPostData = new JSONObject(postData);
        String timeMax = getCalculator.ConvertSecondToDate(getTimeMax);
        String timeMin = getCalculator.ConvertSecondToDate(getTimeMin);
        // listResult.put("time",listTime);
        // listResultClient.put("timeMax",timeMax);
        // listResult.put("timeMin",timeMin);
        listResultTraffic.put("name", getPostData.getString("traffic"));
        listResultTraffic.put("max", getCalculator.convertBytesToGb(maxTraffic) + " GB");
        listResultTraffic.put("min", getCalculator.convertBytesToGb(minTraffic) + " GB");
        listResultTraffic.put("average", getCalculator.convertBytesToGb(avgTraffic) + " GB");

        listResultClient.put("name", getPostData.getString("client"));
        listResultClient.put("max", maxClient + " " + getPostData.getString("client_init"));
        listResultClient.put("min", minClient + " " + getPostData.getString("client_init"));
        listResultClient.put("average", avg + " " + getPostData.getString("client_init"));

        listResult.add(listResultClient.toString());
        listResult.add(listResultTraffic.toString());
        return listResult.toString();
    }

    @ApiOperation(value = "Get time daily")
    @PostMapping("it4u/{id}/getTimeDaily")
    public String getTimeDaily(@RequestBody String postData,@PathVariable(value = "id") String userId, @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> listTime = new ArrayList<>();
        List<String> listResult = new ArrayList<>();
        JSONObject listResultTraffic = new JSONObject();
        JSONObject listResultClient = new JSONObject();
        long getTimeMax = 0;
        long getTimeMin = 0;
        long sumTraffic = 0;
        long avgTraffic = 0;
        int sum = 0;
        int avg = 0;
        int k = 0;
        ApiRequest apiRequest = new ApiRequest();
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/daily.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPos = (JSONObject) dataHourly.get(0);
        Integer maxClient = getPos.getInt("wlan-num_sta");
        Integer minClient = getPos.getInt("wlan-num_sta");
        Long maxTraffic = getPos.getLong("wlan_bytes");
        Long minTraffic = getPos.getLong("wlan_bytes");
        Calculator getCalculator = new Calculator();
        Long time0 = getPos.getLong("time");
        String uptime0 = getCalculator.ConvertSecondToDate(time0);
        listTime.add(uptime0.toString());
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Long time = getPosEnd.getLong("time");  
                String uptime = getCalculator.ConvertSecondToDate(time);
                if (maxClient <= getPosStart.getInt("wlan-num_sta")) {
                    maxClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMax = getPosStart.getLong("time");
                }
                if (minClient >= getPosStart.getInt("wlan-num_sta")) {
                    minClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMin = getPosStart.getLong("time");
                }
                if (maxTraffic <= getPosStart.getLong("wlan_bytes")) {
                    maxTraffic = getPosStart.getLong("wlan_bytes");
                }
                if (minTraffic >= getPosStart.getLong("wlan_bytes")) {
                    minTraffic = getPosStart.getLong("wlan_bytes");
                }
                sumTraffic = sumTraffic + getPosStart.getLong("wlan_bytes");
                sum = sum + getPosStart.getInt("wlan-num_sta");
                k = k + 1;
                listTime.add(uptime.toString());
				
            }

        }
        avgTraffic = Math.round(sumTraffic / k);
        avg = Math.round(sum/k);
        JSONObject getPostData = new JSONObject(postData);
        String timeMax = getCalculator.ConvertSecondToDate(getTimeMax);
        String timeMin = getCalculator.ConvertSecondToDate(getTimeMin);
        // listResult.put("time",listTime);
        // listResultClient.put("timeMax",timeMax);
        // listResult.put("timeMin",timeMin);
        listResultTraffic.put("name", getPostData.getString("traffic"));
        listResultTraffic.put("max", getCalculator.convertBytesToGb(maxTraffic) + " GB");
        listResultTraffic.put("min", getCalculator.convertBytesToGb(minTraffic) + " GB");
        listResultTraffic.put("average", getCalculator.convertBytesToGb(avgTraffic) + " GB");

        listResultClient.put("name", getPostData.getString("client"));
        listResultClient.put("max", maxClient + " " + getPostData.getString("client_init"));
        listResultClient.put("min", minClient + " " + getPostData.getString("client_init"));
        listResultClient.put("average", avg + " " + getPostData.getString("client_init"));

        listResult.add(listResultClient.toString());
        listResult.add(listResultTraffic.toString());
        return listResult.toString();
    }

    @ApiOperation(value = "Count AP Connect")
    @PostMapping("it4u/{id}/apCount")
    public String getAPConnect(@PathVariable(value = "id") String userId, @RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        Integer countConn = 0;
        Integer countDisConn = 0;
        List<String> result = new ArrayList<>();
        JSONObject getConn = new JSONObject();
        JSONObject getDisConn = new JSONObject();
        JSONObject getPostData = new JSONObject(postData);
        ApiRequest apiRequest = new ApiRequest();
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/device/",csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i=0; i<data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            if (getInfo.getInt("state") == 1) {
                countConn = countConn + 1;
            }
            else {
                countDisConn = countDisConn + 1;
            }
        }
        getConn.put("name",getPostData.getString("Connected"));
        getConn.put("y",countConn);
        result.add(getConn.toString());
        getDisConn.put("name",getPostData.getString("Disconnected"));
        getDisConn.put("y",countDisConn);
        result.add(getDisConn.toString());
        return result.toString();
    }

    @ApiOperation(value = "Hotspot")
    @PostMapping("it4u/{id}/hotspot/{start}/{end}")
    public String getHotspot(@PathVariable(value = "id") String userId,@PathVariable(value = "start") String start, @PathVariable(value = "end") String end, @RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        csrfToken = body.getString("csrfToken");
        unifises = body.getString("unifises");
        Integer newClient = 0;
        Integer returnClient = 0;
        List<String> result = new ArrayList<>();
        JSONObject getNewClient = new JSONObject();
        JSONObject getReturnClient = new JSONObject();
        JSONObject getPostData = new JSONObject(postData);
        ApiRequest apiRequest = new ApiRequest();
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/guest?start=" + start + "&end="+ end,csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i=0; i<data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            try {
                Boolean temp = getInfo.getBoolean("is_returning");
                if (!temp) {
                    newClient = newClient + 1;
                }
                else {
                    returnClient = returnClient + 1;
                }
            } catch (Exception e) {
            }
        }
        getNewClient.put("name", getPostData.getString("New"));
        getNewClient.put("y",newClient);
        getReturnClient.put("name",getPostData.getString("Returning"));
        getReturnClient.put("y", returnClient);
        result.add(getNewClient.toString());
        result.add(getReturnClient.toString());
        return result.toString();
    }

    @ApiOperation(value = "View detail hotspot")
    @PostMapping("it4u/{id}/detail/hotspot/{start}/{end}")
    public String getDetailHotspot(@PathVariable(value = "id") String userId, @PathVariable(value = "start") String start,
            @PathVariable(value = "end") String end, @RequestBody String postData, @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<Integer> resultCountClient = new ArrayList<Integer>(); 
        JSONObject listHotspotJson = new JSONObject();
        JSONObject getPostData = new JSONObject(postData);
        ApiRequest apiRequest = new ApiRequest();
        String getData = apiRequest.getRequestApi(urlIt4u,
                "/s/" + userId + "/stat/guest?start=" + start + "&end=" + end, csrfToken, unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        Calculator convert = new Calculator();
        JSONObject getData0 = (JSONObject) data.get(data.length() - 1);
        long getTimeStart = getData0.getLong("start");
        String convertTimeCond = convert.ConvertSecondToDate(getTimeStart*1000);
        String[] timeCond = convertTimeCond.split("-");
        int startTime = Integer.parseInt(timeCond[0]);
        JSONObject getDataEnd = (JSONObject) data.get(0);
        long getDateEnd = getDataEnd.getLong("start");
        String convertDateEnd = convert.ConvertSecondToDate(getDateEnd*1000);
        String[] timeDateEnd = convertDateEnd.split("-");
        int endTime = Integer.parseInt(timeDateEnd[0]);
        //end
        int countClient = 0;
        int k = 0;
        for(int j = startTime; j<=endTime; j++) {
            for (int i = data.length() - 1; i > 0; i--) {
                JSONObject getInfo = (JSONObject) data.get(i);
                long getTimeEnd = getInfo.getLong("start");
                String convertTimeEnd = convert.ConvertSecondToDate(getTimeEnd * 1000);
                String[] partTime = convertTimeEnd.split("-");
                int getDay = Integer.parseInt(partTime[0]);
                if (getDay == j) {
                    countClient = countClient + 1;
                }
            }
            resultCountClient.add(countClient);
            countClient = 0;
        }
        listHotspotJson.put("pointStart", getTimeStart * 1000 + 7*60*60*1000);
        listHotspotJson.put("pointInterval", 24 * 60 * 60 * 1000);
        listHotspotJson.put("name", getPostData.getString("total_connect"));
        listHotspotJson.put("data", resultCountClient);
        // listTrafficJson.put("pointStart", pointStart);
        // listTrafficJson.put("pointInterval", 60 * 60 * 1000);
        // listTrafficJson.put("name", getPostData.getString("network_monintor"));
        // listTrafficJson.put("data", listTraffic);
        log.info(currentUser.getUsername() + " - it4u/" + userId + "/detail/hotspot/");
        return listHotspotJson.toString();
    }

    //Dashboard 2
    @ApiOperation(value = "Devices info")
    @GetMapping("it4u/{id}/devicesInfo")
    public String getDevicesInfo(@PathVariable(value="id") String siteName) {
        ApiRequest apiRequest = new ApiRequest();
        List<String> result = new ArrayList<>();
        String getData = apiRequest.getRequestDev(urlDev, "/" + siteName + "/15cadd25-ee0c-40b2-bdff-0ce622298336");
        JSONObject convertDataToJson = new JSONObject(getData);
        for (int i=1; i<6; i++) {
            JSONObject itemData = new JSONObject();
            try {
                if (convertDataToJson.getString("wan" + i + "_provider").equals("") || convertDataToJson.getString("wan" + i + "_provider").equals("NONE")) {
                    continue;
                }
                else {
                    itemData.put("name", convertDataToJson.getString("wan" + i + "_provider"));
                    itemData.put("value", convertDataToJson.getString("wan" + i + "_ip"));
                    result.add(itemData.toString());
                }
            } catch (Exception e) {
            }
        }
        return result.toString();
    } 

    @ApiOperation(value = "Customer info")
    @PostMapping("it4u/{id}/customerInfo")
    public String getCustomerInfo(@PathVariable(value="id") String siteName, @RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser){
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetTraffic(siteName, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        String wan2Status = "DOWN";
        String wan1Status = "DOWN";
        String wan3Status = "DOWN";
        String wan4Status = "DOWN";
        TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setTimeZone(tz);
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));
        List<String> result = new ArrayList<>();
        JSONObject wan1 = new JSONObject();
        JSONObject wan2 = new JSONObject();
        JSONObject wan3 = new JSONObject();
        JSONObject wan4 = new JSONObject();
        ApiRequest apiRequest = new ApiRequest();
        Calculator convert = new Calculator();
        String getData = apiRequest.getRequestDev(urlDev, "/" + siteName + "/15cadd25-ee0c-40b2-bdff-0ce622298336");
        JSONObject data = new JSONObject(getData);
        String wan1Ip = data.getString("wan1_ip");
        
        // String wan1Provider = data.getString("wan1_provider");
        Integer getWan1Status = data.getInt("wan1_status");
        if ( getWan1Status == 1) {
            wan1Status = "UP";
        }
        Integer getWan1Uptime = data.getInt("wan1_uptime");
        JSONObject getPostData = new JSONObject(postData);
        String wan1Uptime = convert.ConvertSecondToHHMMString(getWan1Uptime);
        wan1.put("wanIp", wan1Ip);
        wan1.put("wanProvider", getPostData.getString("provider1"));
        wan1.put("wanStatus", wan1Status);
        wan1.put("wanUptime", wan1Uptime);
        result.add(wan1.toString());
        try {
            String wan2Ip = data.getString("wan2_ip");
            // String wan2Provider = data.getString("wan2_provider");
            Integer getWan2Status = data.getInt("wan2_status");

            if ( getWan2Status == 1) {
                wan2Status = "UP";
            }
            Integer getWan2Uptime = data.getInt("wan2_uptime");
            String wan2Uptime = convert.ConvertSecondToHHMMString(getWan2Uptime);
            wan2.put("wanIp", wan2Ip);
            wan2.put("wanProvider", getPostData.getString("provider2"));
            wan2.put("wanStatus", wan2Status);
            wan2.put("wanUptime", wan2Uptime);
            result.add(wan2.toString());
        } catch (Exception e) {
        }
        try {
            String wan3Ip = data.getString("wan3_ip");
            // String wan3Provider = data.getString("wan3_provider");
            Integer getWan3Status = data.getInt("wan3_status");
            if ( getWan3Status == 1) {
                wan3Status = "UP";
            }
            Integer getWan3Uptime = data.getInt("wan3_uptime");
            String wan3Uptime = convert.ConvertSecondToHHMMString(getWan3Uptime);
            wan3.put("wanIp", wan3Ip);
            wan3.put("wanProvider", getPostData.getString("provider3"));
            wan3.put("wanStatus", wan3Status);
            wan3.put("wanUptime", wan3Uptime);
            result.add(wan3.toString());
        } catch (Exception e) {
        }
        try {
            String wan4Ip = data.getString("wan4_ip");
            // String wan4Provider = data.getString("wan4_provider");
            Integer getWan4Status = data.getInt("wan4_status");
            if (getWan4Status == 1) {
                wan4Status = "UP";
            }
            Integer getWan4Uptime = data.getInt("wan4_uptime");
            String wan4Uptime = convert.ConvertSecondToHHMMString(getWan4Uptime);
            wan4.put("wanIp", wan4Ip);
            wan4.put("wanProvider", getPostData.getString("provider4"));
            wan4.put("wanStatus", wan4Status);
            wan4.put("wanUptime", wan4Uptime);
            result.add(wan4.toString());
        } catch (Exception e) {
        }
        return result.toString();
    }

    @ApiOperation(value = "Traffic AP")
    @PostMapping("it4u/{id}/trafficInfo")
    public String getTrafficInfo(@PathVariable(value="id") String idUser, @RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser){
        List<String> result = new ArrayList<>();
        JSONObject uploadJson = new JSONObject();
        JSONObject downloadJson = new JSONObject();
        ApiRequest apiRequest = new ApiRequest();
        long upload = 0;
        long download = 0;
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + idUser + "/stat/sta/",csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(idUser, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        for (int i=0; i<data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            upload = upload + getInfo.getInt("rx_rate");
            download = download + getInfo.getInt("tx_rate");
        }
        Calculator getCalculator = new Calculator();
        double convertUploadToMb = getCalculator.convertBytesToMb(upload);
        double convertDownloadToMb = getCalculator.convertBytesToMb(download);
        JSONObject getPostData = new JSONObject(postData);
        uploadJson.put("name",getPostData.getString("Upload"));
        uploadJson.put("y", convertUploadToMb);
        downloadJson.put("name",getPostData.getString("Download"));
        downloadJson.put("y", convertDownloadToMb);
        result.add(uploadJson.toString());
        result.add(downloadJson.toString());
        return result.toString();
    }

    // Get network rates
    @ApiOperation(value = "getNetworkRates")
    @PostMapping("it4u/{id}/getNetworkRates")
    public String getNetworkRates(@RequestBody String postData, @PathVariable(value = "id") String userId,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> result = new ArrayList<>();
        List<String> listTime = new ArrayList<>();
        List<Double> listTraffic = new ArrayList<Double>();
        JSONObject listTrafficJson = new JSONObject();
        JSONObject getPostData = new JSONObject(postData);
        ApiRequest apiRequest = new ApiRequest();

        String getMinute = apiRequest.postRequestApi(urlIt4u, "/s/" + userId + "/stat/report/hourly.site/", csrfToken,
                unifises, postData);
        JSONObject getData = new JSONObject(getMinute);
        JSONArray dataMinute = getData.getJSONArray("data");
        JSONObject getPosZero = (JSONObject) dataMinute.get(0);
        long countTraffic0 = getPosZero.getLong("wlan_bytes");
        Long changeTraffic0 = Math.round(countTraffic0/439.08);
        Calculator getCalculator = new Calculator();
        double convertTraffic0 = getCalculator.convertBytesToMb(changeTraffic0);
        Long time0 = getPosZero.getLong("time");
        String uptime0 = getCalculator.ConvertSecondToDate(time0);
        listTime.add(uptime0.toString());
        listTraffic.add(convertTraffic0);
        for (int i = 0; i < dataMinute.length() - 1; i++) {
            JSONObject getPosStart = (JSONObject) dataMinute.get(i);
            JSONObject getPosEnd = (JSONObject) dataMinute.get(i + 1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Long time = getPosEnd.getLong("time");
                String uptime = getCalculator.ConvertSecondToDate(time);
                Long getTraffic = getPosEnd.getLong("wlan_bytes");
                Long changeTraffic = Math.round(getTraffic/439.08);
                double convertTraffic = getCalculator.convertBytesToMb(changeTraffic);
                
                listTraffic.add(convertTraffic);
                listTime.add(uptime.toString());
            }
        }
        Long pointStart = time0 + 7*60*60*1000;
        listTrafficJson.put("pointStart", pointStart);
        listTrafficJson.put("pointInterval",60 * 60 * 1000);
        listTrafficJson.put("name", getPostData.getString("network_monintor"));
        listTrafficJson.put("data", listTraffic);
        log.info(currentUser.getUsername() + " - it4u/" + userId + "/getNetworkRates");
        return listTrafficJson.toString();
    }

    // Get traffic history
    @ApiOperation(value = "Get traffic history")
    @PostMapping("it4u/{id}/getTrafficHistory")
    public String getTrafficHistory(@RequestBody String postData, @PathVariable(value = "id") String userId,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<Long> listTraffic = new ArrayList<Long>();
        JSONObject listTrafficJson = new JSONObject();
        JSONObject getPostData = new JSONObject(postData);
        ApiRequest apiRequest = new ApiRequest();

        String getMinute = apiRequest.postRequestApi(urlIt4u, "/s/" + userId + "/stat/report/daily.site/", csrfToken,
                unifises, postData);
        JSONObject getData = new JSONObject(getMinute);
        JSONArray dataMinute = getData.getJSONArray("data");
        JSONObject getPosZero = (JSONObject) dataMinute.get(0);
        long countTraffic0 = getPosZero.getLong("wlan_bytes");
        Long time0 = getPosZero.getLong("time");
        listTraffic.add(countTraffic0 / (1024 * 1024 * 1024));
        for (int i = 0; i < dataMinute.length() - 1; i++) {
            JSONObject getPosStart = (JSONObject) dataMinute.get(i);
            JSONObject getPosEnd = (JSONObject) dataMinute.get(i + 1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Long getTraffic = getPosEnd.getLong("wlan_bytes");
                listTraffic.add(getTraffic/(1024*1024*1024));
            }
        }
        Long pointStart = time0 + 7 * 60 * 60 * 1000;
        listTrafficJson.put("pointStart", pointStart);
        listTrafficJson.put("pointInterval", 24 * 60 * 60 * 1000);
        listTrafficJson.put("name", getPostData.getString("network_monintor"));
        listTrafficJson.put("data", listTraffic);
        log.info(currentUser.getUsername() + " - it4u/" + userId + "/getTrafficHistory");
        return listTrafficJson.toString();
    }

    // get history zabbix
    @ApiOperation(value = "History Network")
    @PostMapping("it4u/{id}/history/network")
    public String getHistoryNetwork(@PathVariable(value="id") String idUser,@RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetTraffic(idUser, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> listTime = new ArrayList<>();
        List<Float> listTraffic = new ArrayList<Float>();
        JSONObject listResultJson = new JSONObject();
        String dataGetHostId = "{\"jsonrpc\": \"2.0\",\"method\": \"item.get\",\"params\": {\"output\": [\"hostid\"],\"selectGroups\": \"extend\",\"filter\": {\"host\": [\"" + idUser + "\"]}},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
        DashboardController getDashboard = new DashboardController();
        JSONArray getResultHost = getDashboard.getDataZabbix(urlZabbix,dataGetHostId);
        JSONObject getItemHost = (JSONObject) getResultHost.get(0);
        String hostId = getItemHost.getString("hostid");
        String dataGetItemId = "{\"jsonrpc\": \"2.0\",\"method\": \"item.get\",\"params\": {\"output\": \"extend\",\"hostids\": \"" + hostId + "\"},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
        String itemUploadId = "";
        String itemDownloadId = "";
        JSONArray getResultItem = getDashboard.getDataZabbix(urlZabbix,dataGetItemId);
        for (int i = 0; i< getResultItem.length(); i++) {
            JSONObject getItem = (JSONObject) getResultItem.get(i);
            String nameTest = getItem.getString("name");
            if (nameTest.equals("download")) {
                itemDownloadId = getItem.getString("itemid");
            }
            if (nameTest.equals("upload")) {
                itemUploadId = getItem.getString("itemid");
            }
        }
        JSONObject convertDataPost = new JSONObject(postData);
        Long timeFrom = convertDataPost.getLong("time_from")/1000;
        Long timeTill = convertDataPost.getLong("time_till")/1000;
        String dataPostUpload = "{\"jsonrpc\": \"2.0\",\"method\": \"history.get\",\"params\": {\"output\": \"extend\",\"history\": 0,\"itemids\": \"" + itemUploadId + "\",\"time_from\":" + timeFrom + ",\"time_till\":" + timeTill + "},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
        String dataPostDownload = "{\"jsonrpc\": \"2.0\",\"method\": \"history.get\",\"params\": {\"output\": \"extend\",\"history\": 0,\"itemids\": \"" + itemDownloadId + "\",\"time_from\":" + timeFrom + ",\"time_till\":" + timeTill + "},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
        JSONArray getResultUpload = getDashboard.getDataZabbix(urlZabbix,dataPostUpload);
        JSONArray getResultDownload = getDashboard.getDataZabbix(urlZabbix,dataPostDownload);
        Calculator getCalculator = new Calculator();
        for (int i=0; i<getResultUpload.length(); i++) {
            JSONObject getItemUpload = (JSONObject) getResultUpload.get(i);
            JSONObject getItemDownload = (JSONObject) getResultDownload.get(i);
            Long time = getItemDownload.getLong("clock")*1000;
            Float total = getItemUpload.getFloat("value") + getItemDownload.getFloat("value");
            String convertTime = getCalculator.ConvertSecondToDateNotZone(time);
            listTime.add(convertTime);
            listTraffic.add(total/1024);
        }
        listResultJson.put("name", "Network Monintor");
        listResultJson.put("time", listTime);
        listResultJson.put("data", listTraffic);
        return listResultJson.toString();
    }

    @ApiOperation(value = "History Status")
    @PostMapping("it4u/{id}/history/status")
    public String getHistoryStatus(@PathVariable(value = "id") String idUser, @RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetTraffic(idUser, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> listTime = new ArrayList<>();
        List<Float> listStatusWan1 = new ArrayList<>();
        List<Float> listStatusWan2 = new ArrayList<>();
        List<Float> listStatusWan3 = new ArrayList<>();
        List<Float> listStatusWan4 = new ArrayList<>();
        List<String> result = new ArrayList<>();
        JSONObject statusWan1Json = new JSONObject();
        JSONObject statusWan2Json = new JSONObject();
        JSONObject statusWan3Json = new JSONObject();
        JSONObject statusWan4Json = new JSONObject();
        JSONObject listResultJson = new JSONObject();
        JSONArray getResultStatusWan1 = new JSONArray();
        JSONArray getResultStatusWan2 = new JSONArray();
        JSONArray getResultStatusWan3 = new JSONArray();
        JSONArray getResultStatusWan4 = new JSONArray();
        String dataGetHostId = "{\"jsonrpc\": \"2.0\",\"method\": \"item.get\",\"params\": {\"output\": [\"hostid\"],\"selectGroups\": \"extend\",\"filter\": {\"host\": [\""
                + idUser + "\"]}},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
        DashboardController getDashboard = new DashboardController();
        JSONArray getResultHost = getDashboard.getDataZabbix(urlZabbix, dataGetHostId);
        JSONObject getItemHost = (JSONObject) getResultHost.get(0);
        String hostId = getItemHost.getString("hostid");
        String dataGetItemId = "{\"jsonrpc\": \"2.0\",\"method\": \"item.get\",\"params\": {\"output\": \"extend\",\"hostids\": \""
                + hostId + "\"},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
        String itemStatusWan1Id = "";
        String itemStatusWan2Id = "";
        String itemStatusWan3Id = "";
        String itemStatusWan4Id = "";
        JSONArray getResultItem = getDashboard.getDataZabbix(urlZabbix, dataGetItemId);
        for (int i = 0; i < getResultItem.length(); i++) {
            JSONObject getItem = (JSONObject) getResultItem.get(i);
            String nameTest = getItem.getString("name");
            if (nameTest.equals("wan1_status")) {
                itemStatusWan1Id = getItem.getString("itemid");
            }
            if (nameTest.equals("wan2_status")) {
                itemStatusWan2Id = getItem.getString("itemid");
            }
            if (nameTest.equals("wan3_status")) {
                itemStatusWan3Id = getItem.getString("itemid");
            }
            if (nameTest.equals("wan4_status")) {
                itemStatusWan4Id = getItem.getString("itemid");
            }
        }
        JSONObject convertDataPost = new JSONObject(postData);
        Long timeFrom = convertDataPost.getLong("time_from") / 1000;
        Long timeTill = convertDataPost.getLong("time_till") / 1000;
        if (itemStatusWan1Id != "") {
            String dataPostStatusWan1 = "{\"jsonrpc\": \"2.0\",\"method\": \"history.get\",\"params\": {\"output\": \"extend\",\"history\": 3,\"itemids\": \""
                    + itemStatusWan1Id + "\",\"time_from\":" + timeFrom + ",\"time_till\":" + timeTill + "},\"auth\": \""
                    + tokenZabbix + "\",\"id\": 1}";
            getResultStatusWan1 = getDashboard.getDataZabbix(urlZabbix, dataPostStatusWan1);
        }
        if (itemStatusWan2Id != "") {
            String dataPostStatusWan2 = "{\"jsonrpc\": \"2.0\",\"method\": \"history.get\",\"params\": {\"output\": \"extend\",\"history\": 3,\"itemids\": \""
                    + itemStatusWan2Id + "\",\"time_from\":" + timeFrom + ",\"time_till\":" + timeTill
                    + "},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
            getResultStatusWan2 = getDashboard.getDataZabbix(urlZabbix, dataPostStatusWan2);
        }
        if (itemStatusWan3Id != "") {
            String dataPostStatusWan3 = "{\"jsonrpc\": \"2.0\",\"method\": \"history.get\",\"params\": {\"output\": \"extend\",\"history\": 3,\"itemids\": \""
                    + itemStatusWan3Id + "\",\"time_from\":" + timeFrom + ",\"time_till\":" + timeTill
                    + "},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
            getResultStatusWan2 = getDashboard.getDataZabbix(urlZabbix, dataPostStatusWan3);
        }
        if (itemStatusWan4Id != "") {
            String dataPostStatusWan4 = "{\"jsonrpc\": \"2.0\",\"method\": \"history.get\",\"params\": {\"output\": \"extend\",\"history\": 3,\"itemids\": \""
                    + itemStatusWan4Id + "\",\"time_from\":" + timeFrom + ",\"time_till\":" + timeTill
                    + "},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
            getResultStatusWan2 = getDashboard.getDataZabbix(urlZabbix, dataPostStatusWan4);
        }
        Calculator getCalculator = new Calculator();
        for (int i = 0; i < getResultStatusWan1.length(); i = i + 10) {
            JSONObject getItemStatusWan1 = (JSONObject) getResultStatusWan1.get(i);
            Float valueStatusWan1 = getItemStatusWan1.getFloat("value");
            listStatusWan1.add(valueStatusWan1);
            if (itemStatusWan2Id != "") {
                JSONObject getItemStatusWan2 = (JSONObject) getResultStatusWan2.get(i);
                Float valueStatusWan2 = getItemStatusWan2.getFloat("value");
                // if (valueStatusWan2)

                listStatusWan2.add(valueStatusWan2);
            }
            if (itemStatusWan3Id != "") {
                JSONObject getItemStatusWan3 = (JSONObject) getResultStatusWan3.get(i);
                Float valueStatusWan3 = getItemStatusWan3.getFloat("value");
                listStatusWan3.add(valueStatusWan3);
            }
            if (itemStatusWan4Id != "") {
                JSONObject getItemStatusWan4 = (JSONObject) getResultStatusWan4.get(i);
                Float valueStatusWan4 = getItemStatusWan4.getFloat("value");
                listStatusWan4.add(valueStatusWan4);
            }
            // listTime.add(convertTime);
        }
        
        statusWan1Json.put("name", "Wan1 Status");
        statusWan1Json.put("data", listStatusWan1);
        result.add(statusWan1Json.toString());
        if (itemStatusWan2Id != "") {
            statusWan2Json.put("name", "Wan2 Status");
            statusWan2Json.put("data", listStatusWan2);
            result.add(statusWan2Json.toString());
        }
        if (itemStatusWan3Id != "") {
            statusWan3Json.put("name", "Wan3 Status");
            statusWan3Json.put("data", listStatusWan3);
            result.add(statusWan3Json.toString());
        }

        if (itemStatusWan4Id != "") {
            statusWan4Json.put("name", "Wan4 Status");
            statusWan4Json.put("data", listStatusWan4);
            result.add(statusWan4Json.toString());
        }

        // listResultJson.put("time",listTime);
        // result.add(listResultJson.toString());
        return result.toString();
    }

    @ApiOperation(value = "History time status")
    @PostMapping("it4u/{id}/history/getTimeStatus")
    public String getTimeStatus(@PathVariable(value = "id") String idUser, @RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetTraffic(idUser, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<String> listTime = new ArrayList<>();
        List<String> result = new ArrayList<>();
        JSONObject listResultJson = new JSONObject();
        JSONArray getResultStatusWan1 = new JSONArray();
        String dataGetHostId = "{\"jsonrpc\": \"2.0\",\"method\": \"item.get\",\"params\": {\"output\": [\"hostid\"],\"selectGroups\": \"extend\",\"filter\": {\"host\": [\""
                + idUser + "\"]}},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
        DashboardController getDashboard = new DashboardController();
        JSONArray getResultHost = getDashboard.getDataZabbix(urlZabbix, dataGetHostId);
        JSONObject getItemHost = (JSONObject) getResultHost.get(0);
        String hostId = getItemHost.getString("hostid");
        String dataGetItemId = "{\"jsonrpc\": \"2.0\",\"method\": \"item.get\",\"params\": {\"output\": \"extend\",\"hostids\": \""
                + hostId + "\"},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
        String itemStatusWan1Id = "";
        JSONArray getResultItem = getDashboard.getDataZabbix(urlZabbix, dataGetItemId);
        for (int i = 0; i < getResultItem.length(); i++) {
            JSONObject getItem = (JSONObject) getResultItem.get(i);
            String nameTest = getItem.getString("name");
            if (nameTest.equals("wan1_status")) {
                itemStatusWan1Id = getItem.getString("itemid");
            }
        }
        JSONObject convertDataPost = new JSONObject(postData);
        Long timeFrom = convertDataPost.getLong("time_from") / 1000;
        Long timeTill = convertDataPost.getLong("time_till") / 1000;
        if (itemStatusWan1Id != "") {
            String dataPostStatusWan1 = "{\"jsonrpc\": \"2.0\",\"method\": \"history.get\",\"params\": {\"output\": \"extend\",\"history\": 3,\"itemids\": \""
                    + itemStatusWan1Id + "\",\"time_from\":" + timeFrom + ",\"time_till\":" + timeTill
                    + "},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
            getResultStatusWan1 = getDashboard.getDataZabbix(urlZabbix, dataPostStatusWan1);
        }
        Calculator getCalculator = new Calculator();
        for (int i = 0; i < getResultStatusWan1.length(); i = i + 10) {
            JSONObject getItemStatusWan1 = (JSONObject) getResultStatusWan1.get(i);
            Long time = getItemStatusWan1.getLong("clock") * 1000;
            String convertTime = getCalculator.ConvertSecondToDateNotZone(time);
            listTime.add(convertTime);
        }
        listResultJson.put("time", listTime);
        result.add(listResultJson.toString());
        return listResultJson.toString();
    }

    @ApiOperation(value = "Channel Status")
    @PostMapping("it4u/{id}/channel/status")
    public String getChannelStatus(@PathVariable(value = "id") String idUser, @RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser) {
        int k = 0, upWan1 = 0, upWan2 = 0, upWan3 = 0, upWan4 = 0;
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetTraffic(idUser, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        List<Integer> listStatusUp = new ArrayList<>();
        List<Integer> listStatusDown = new ArrayList<>();

        List<String> result = new ArrayList<>();
        JSONObject statusUpJson = new JSONObject();
        JSONObject statusDownJson = new JSONObject();

        JSONArray getResultStatusWan1 = new JSONArray();
        JSONArray getResultStatusWan2 = new JSONArray();
        JSONArray getResultStatusWan3 = new JSONArray();
        JSONArray getResultStatusWan4 = new JSONArray();
        JSONObject getPostData = new JSONObject(postData);
        String dataGetHostId = "{\"jsonrpc\": \"2.0\",\"method\": \"item.get\",\"params\": {\"output\": [\"hostid\"],\"selectGroups\": \"extend\",\"filter\": {\"host\": [\""
                + idUser + "\"]}},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
        DashboardController getDashboard = new DashboardController();
        JSONArray getResultHost = getDashboard.getDataZabbix(urlZabbix, dataGetHostId);
        JSONObject getItemHost = (JSONObject) getResultHost.get(0);
        String hostId = getItemHost.getString("hostid");
        String dataGetItemId = "{\"jsonrpc\": \"2.0\",\"method\": \"item.get\",\"params\": {\"output\": \"extend\",\"hostids\": \""
                + hostId + "\"},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
        String itemStatusWan1Id = "";
        String itemStatusWan2Id = "";
        String itemStatusWan3Id = "";
        String itemStatusWan4Id = "";
        JSONArray getResultItem = getDashboard.getDataZabbix(urlZabbix, dataGetItemId);
        for (int i = 0; i < getResultItem.length(); i++) {
            JSONObject getItem = (JSONObject) getResultItem.get(i);
            String nameTest = getItem.getString("name");
            if (nameTest.equals("wan1_status")) {
                itemStatusWan1Id = getItem.getString("itemid");
            }
            if (nameTest.equals("wan2_status")) {
                itemStatusWan2Id = getItem.getString("itemid");
            }
            if (nameTest.equals("wan3_status")) {
                itemStatusWan3Id = getItem.getString("itemid");
            }
            if (nameTest.equals("wan4_status")) {
                itemStatusWan4Id = getItem.getString("itemid");
            }
        }
        JSONObject convertDataPost = new JSONObject(postData);
        Long timeFrom = convertDataPost.getLong("time_from") / 1000;
        Long timeTill = convertDataPost.getLong("time_till") / 1000;
        if (itemStatusWan1Id != "") {
            String dataPostStatusWan1 = "{\"jsonrpc\": \"2.0\",\"method\": \"history.get\",\"params\": {\"output\": \"extend\",\"history\": 3,\"itemids\": \""
                    + itemStatusWan1Id + "\",\"time_from\":" + timeFrom + ",\"time_till\":" + timeTill
                    + "},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
            getResultStatusWan1 = getDashboard.getDataZabbix(urlZabbix, dataPostStatusWan1);
        }
        if (itemStatusWan2Id != "") {
            String dataPostStatusWan2 = "{\"jsonrpc\": \"2.0\",\"method\": \"history.get\",\"params\": {\"output\": \"extend\",\"history\": 3,\"itemids\": \""
                    + itemStatusWan2Id + "\",\"time_from\":" + timeFrom + ",\"time_till\":" + timeTill
                    + "},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
            getResultStatusWan2 = getDashboard.getDataZabbix(urlZabbix, dataPostStatusWan2);
        }
        if (itemStatusWan3Id != "") {
            String dataPostStatusWan3 = "{\"jsonrpc\": \"2.0\",\"method\": \"history.get\",\"params\": {\"output\": \"extend\",\"history\": 3,\"itemids\": \""
                    + itemStatusWan3Id + "\",\"time_from\":" + timeFrom + ",\"time_till\":" + timeTill
                    + "},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
            getResultStatusWan3 = getDashboard.getDataZabbix(urlZabbix, dataPostStatusWan3);
        }
        if (itemStatusWan4Id != "") {
            String dataPostStatusWan4 = "{\"jsonrpc\": \"2.0\",\"method\": \"history.get\",\"params\": {\"output\": \"extend\",\"history\": 3,\"itemids\": \""
                    + itemStatusWan4Id + "\",\"time_from\":" + timeFrom + ",\"time_till\":" + timeTill
                    + "},\"auth\": \"" + tokenZabbix + "\",\"id\": 1}";
            getResultStatusWan4 = getDashboard.getDataZabbix(urlZabbix, dataPostStatusWan4);
        }
        for (int i = 0; i < getResultStatusWan1.length(); i = i + 10) {
            k = k + 1;
            JSONObject getItemStatusWan1 = (JSONObject) getResultStatusWan1.get(i);
            Float valueStatusWan1 = getItemStatusWan1.getFloat("value");
            upWan1 = upWan1 + Math.round(valueStatusWan1);
            if (itemStatusWan2Id != "") {
                JSONObject getItemStatusWan2 = (JSONObject) getResultStatusWan2.get(i);
                Float valueStatusWan2 = getItemStatusWan2.getFloat("value");
                upWan2 = upWan2 + Math.round(valueStatusWan2);
            }
            if (itemStatusWan3Id != "") {
                JSONObject getItemStatusWan3 = (JSONObject) getResultStatusWan3.get(i);
                Float valueStatusWan3 = getItemStatusWan3.getFloat("value");
                upWan3 = upWan3 + Math.round(valueStatusWan3);
            }
            if (itemStatusWan4Id != "") {
                JSONObject getItemStatusWan4 = (JSONObject) getResultStatusWan4.get(i);
                Float valueStatusWan4 = getItemStatusWan4.getFloat("value");
                upWan4 = upWan4 + Math.round(valueStatusWan4);
            }
        }
        try {
            Integer valueUpWan1 = Math.round(upWan1*100/k);
            listStatusUp.add(valueUpWan1);
            listStatusDown.add(100 - valueUpWan1);
            if (itemStatusWan2Id != "") {
                Integer valueUpWan2 = Math.round(upWan2 * 100 / k);
                listStatusUp.add(valueUpWan2);
                listStatusDown.add(100 - valueUpWan2);
            }
            if (itemStatusWan3Id != "") {
                Integer valueUpWan3 = Math.round(upWan3 * 100 / k);
                listStatusUp.add(valueUpWan3);
                listStatusDown.add(100 - valueUpWan3);
            }
            if (itemStatusWan4Id != "") {
                Integer valueUpWan4 = Math.round(upWan4 * 100 / k);
                listStatusUp.add(valueUpWan4);
                listStatusDown.add(100 - valueUpWan4);
            }
        }catch(Exception e){
            
        }
        
        statusUpJson.put("name", getPostData.getString("up"));
        statusUpJson.put("data", listStatusUp);
        statusUpJson.put("valueSuffix", " %");
        statusDownJson.put("name", getPostData.getString("down"));
        statusDownJson.put("data", listStatusDown);
        statusDownJson.put("valueSuffix", " %");

        result.add(statusUpJson.toString());
        result.add(statusDownJson.toString());
        return result.toString();
    }

    @ApiOperation(value = "Provider info")
    @PostMapping("it4u/{id}/name/provider")
    public String getNameProvider(@PathVariable(value = "id") String siteName, @RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetTraffic(siteName, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setTimeZone(tz);
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));
        List<String> result = new ArrayList<>();
        JSONObject resultJson = new JSONObject();
        ApiRequest apiRequest = new ApiRequest();
        JSONObject getPostData = new JSONObject(postData);
        String getData = apiRequest.getRequestDev(urlDev, "/" + siteName + "/15cadd25-ee0c-40b2-bdff-0ce622298336");
        JSONObject data = new JSONObject(getData);
        
        result.add(getPostData.getString("provider1"));
        try {
            if (!data.getString("wan2_ip").equals("NONE")) {
                result.add(getPostData.getString("provider2"));
            }
        } catch (Exception e) {
        }
        try {
            if (!data.getString("wan3_ip").equals("NONE")) {
                result.add(getPostData.getString("provider3"));
            }     
        } catch (Exception e) {
        }
        try {
            if (!data.getString("wan4_ip").equals("NONE")) {
                result.add(getPostData.getString("provider4"));
            }
        } catch (Exception e) {
        }
        resultJson.put("name", result);
        return resultJson.toString();
    }
    
    //Dashboard - Overview
    @ApiOperation(value = "Dashboard overview")
    @GetMapping("/it4u/dashboard/overview")
    public String showDashboard() {
        JSONObject result = new JSONObject();
        Integer activeSites = 0;
        Integer warningSites = 0;
        Integer errorSites = 0;
        JSONObject activeSitesJson = new JSONObject();
        JSONObject warningSitesJson = new JSONObject();
        JSONObject errorSitesJson = new JSONObject();
        List<String> listErrorSites = new ArrayList<>();
        List<String> listWarningSites = new ArrayList<>();
        List<String> listErrorWeekly = new ArrayList<>();
        List<String> listSites = new ArrayList<>();
        JSONObject errorWeekly = new JSONObject();
        ApiRequest apiRequest = new ApiRequest();
        String getSites = apiRequest.getRequestApi(urlIt4u, "/stat/sites", csrfToken, unifises);
        JSONObject convertSites = new JSONObject(getSites);
        JSONArray getDataSites = convertSites.getJSONArray("data");
        for (int i = 0; i < getDataSites.length(); i++) {
            JSONObject errorSitesName = new JSONObject();
            JSONObject warningSitesName = new JSONObject();
            JSONObject getItemSite = (JSONObject) getDataSites.get(i);
            JSONArray getHealth = getItemSite.getJSONArray("health");
            JSONObject getItemHealth = (JSONObject) getHealth.get(0);
            String getStatus = getItemHealth.getString("status");
            try {
                switch (getStatus) {
                    case "error":
                        errorSitesName.put("name", getItemSite.getString("desc"));
                        errorSitesName.put("id", getItemSite.getString("name"));
                        errorSitesName.put("value", getItemHealth.getInt("num_ap") + "/" + getItemHealth.getInt("num_adopted"));
                        listErrorSites.add(errorSitesName.toString());
                        errorSites = errorSites + 1;
                        break;
                    case "warning":
                        warningSitesName.put("name", getItemSite.getString("desc"));
                        warningSitesName.put("id", getItemSite.getString("name"));
                        warningSitesName.put("value", getItemHealth.getInt("num_ap") + "/" + getItemHealth.getInt("num_adopted"));
                        listWarningSites.add(warningSitesName.toString());
                        warningSites = warningSites + 1;
                        break;
                    case "ok":
                        activeSites = activeSites + 1;
                        break;
                
                    default:
                        break;
                }
            } catch (Exception e) {
            }
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long endTime = timestamp.getTime();
        Long startTime = endTime - 7*24*60*60*100;
        String postData = "{\"attrs\":[\"time\"],\"start\":\"" + startTime + "\",\"end\":\""+ endTime +"\"}";
        for (int i = 0; i < listErrorSites.size(); i++) {
            JSONObject getErrorSite = new JSONObject(listErrorSites.get(i));
            String idSite = getErrorSite.getString("id");
            String getDaily = apiRequest.postRequestApi(urlIt4u, "/s/" + idSite + "/stat/report/daily.site/",
                    csrfToken, unifises, postData);
            JSONObject convertData = new JSONObject(getDaily);
            JSONArray getData = convertData.getJSONArray("data");
            try {
                JSONObject getEndData = (JSONObject) getData.get(getData.length() - 1);
                errorWeekly.put("name", getErrorSite.getString("name"));
                errorWeekly.put("time", getEndData.getLong("time"));
                listErrorWeekly.add(errorWeekly.toString());
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        result.put("errorWeekly", listErrorWeekly.toString());
        result.put("listErrorSites", listErrorSites.toString());
        result.put("listWarningSites", listWarningSites.toString());
        activeSitesJson.put("name", "Active");
        activeSitesJson.put("y", activeSites);
        errorSitesJson.put("name", "Error");
        errorSitesJson.put("y", errorSites);
        warningSitesJson.put("name", "Warning");
        warningSitesJson.put("y", warningSites);
        listSites.add(activeSitesJson.toString());
        listSites.add(errorSitesJson.toString());
        listSites.add(warningSitesJson.toString());
        result.put("overview", listSites.toString());
        return result.toString();
    }

    // VOUCHERS......................................................................................
    ///////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Create Voucher")
    @PostMapping("it4u/{id}/createVoucher")
    public String postVoucher(@RequestBody String postData, @PathVariable(value = "id") String userId, @CurrentUser CustomUserDetails currentUser) {
        
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        ApiRequest apiRequest = new ApiRequest();
        JSONObject getPostData = new JSONObject(postData);
        int n = getPostData.getInt("n");
        int quota = getPostData.getInt("quota");
        int expire = getPostData.getInt("expire");
        int expire_number = getPostData.getInt("expire_number");
        int expire_unit = getPostData.getInt("expire_unit");
        int down = getPostData.getInt("down");
        int up = getPostData.getInt("up");
        int bytes = getPostData.getInt("bytes");
        String dataPostVouchers= "{\"cmd\":\"create-voucher\",\"n\":"+ n +",\"quota\":" + quota
                + ",\"expire\":" + expire + ",\"expire_number\":" + expire_number + ",\"expire_unit\":" + expire_unit + ",\"down\":" + down + ",\"up\":" + up + ",\"bytes\":" + bytes + ",\"note\":\"Note Ok\"}";  
        String postVoucher = apiRequest.postRequestApi(urlIt4u, "/s/" + userId + "/cmd/hotspot", csrfToken, unifises,
                dataPostVouchers);
        return postVoucher;
    }

    @ApiOperation(value = "Get Voucher")
    @GetMapping("it4u/{id}/voucher")
    public String getVoucher(@PathVariable(value = "id") String userId, @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        ApiRequest apiRequest = new ApiRequest();
        JSONObject result = new JSONObject();
        Integer down = 0;
        Integer up = 0;
        Integer byteQuota = 0;
        Calculator getCalculator = new Calculator();
        List<String> dataList = new ArrayList<>();
        String getRequest = apiRequest.getRequestApi(urlIt4u, "/s/" + userId + "/stat/voucher", csrfToken, unifises);
        JSONObject jsonResult = new JSONObject(getRequest);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            JSONObject getData = (JSONObject) data.get(i);
            String id = getData.getString("_id");
            String code = getData.getString("code");
            long getTime = getData.getLong("create_time");
            String createTime = getCalculator.ConvertSecondToDate(getTime * 1000);
            // Integer down = getData.getInt("qos_rate_max_down");
            try {
                down = getData.getInt("qos_rate_max_down");
            } catch (Exception e) {
                down = 0;
            }
            result.put("down", down + " Kbps");
            try {
                up = getData.getInt("qos_rate_max_down");
            } catch (Exception e) {
                up = 0;
            }
            result.put("up", up + " Kbps");
            try {
                byteQuota = getData.getInt("qos_usage_quota");
            } catch (Exception e) {
                byteQuota = 0;
            }
            result.put("byteQuota", byteQuota + " MB");
            String note = getData.getString("note");
            Integer getDuration = getData.getInt("duration");
            String duration = getCalculator.ConvertSecondToHHMMString(getDuration);
            String status = getData.getString("status");
            result.put("id", id);
            result.put("code", code);
            result.put("createTime", createTime);
            result.put("notes", note);
            result.put("duration", duration);
            result.put("status", status);
            dataList.add(result.toString());
        }
        log.info(currentUser.getUsername() + " - it4u/" + userId + "/voucher");
        return dataList.toString();
    }

    @ApiOperation(value = "Get voucher app")
    @GetMapping("it4u/{id}/voucherApp")
    public String getVoucherApp(@PathVariable(value = "id") String userId, @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        ApiRequest apiRequest = new ApiRequest();
        JSONObject result = new JSONObject();
        Integer down = 0;
        Integer up = 0;
        Integer byteQuota = 0;
        Calculator getCalculator = new Calculator();
        List<String> dataList = new ArrayList<>();
        String getRequest = apiRequest.getRequestApi(urlIt4u, "/s/" + userId + "/stat/voucher", csrfToken, unifises);
        JSONObject jsonResult = new JSONObject(getRequest);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            List<String> lish = new ArrayList<>();
            JSONObject getData = (JSONObject) data.get(i);
            String id = getData.getString("_id");
            String code = getData.getString("code");
            long getTime = getData.getLong("create_time");
            String createTime = getCalculator.ConvertSecondToDate(getTime * 1000);
            // Integer down = getData.getInt("qos_rate_max_down");
            try {
                down = getData.getInt("qos_rate_max_down");
            } catch (Exception e) {
                down = 0;
            }
            lish.add(down + " Kbps");
            // result.put("down", down + " Kbps");
            try {
                up = getData.getInt("qos_rate_max_down");
            } catch (Exception e) {
                up = 0;
            }
            lish.add(up + " Kbps");
            //result.put("up", up + " Kbps");
            try {
                byteQuota = getData.getInt("qos_usage_quota");
            } catch (Exception e) {
                byteQuota = 0;
            }
            lish.add(byteQuota + " MB");
            //result.put("byteQuota", byteQuota + " MB");
            String note = getData.getString("note");
            Integer getDuration = getData.getInt("duration");
            String duration = getCalculator.ConvertSecondToHHMMString(getDuration);
            String status = getData.getString("status");
            lish.add(id);
            lish.add(code);
            lish.add(createTime);
            lish.add(note);
            lish.add(duration);
            lish.add(status);
            dataList.add(lish.toString());
        }
        log.info(currentUser.getUsername() + " - it4u/" + userId + "/voucherApp");
        return dataList.toString();
    }

    @ApiOperation(value = "Delete Voucher")
    @PostMapping("it4u/{id}/deleteVoucher")
    public String deleteVoucher(@PathVariable(value = "id") String userId, @RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        ApiRequest apiRequest = new ApiRequest();
        String getData = apiRequest.postRequestApi(urlIt4u, "/s/" + userId + "/cmd/hotspot", csrfToken, unifises,
                postData);
        log.info(currentUser.getUsername() + " - it4u/" + userId + "/deleteVoucher");
        return getData;
    }

    @ApiOperation(value = "Print Voucher")
    @GetMapping("it4u/{id}/printVoucher/{idVoucher}")
    public String printVoucher(@PathVariable(value = "id") String userId,
            @PathVariable(value = "idVoucher") String idVoucher, @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        ApiRequest apiRequest = new ApiRequest();
        String getData = apiRequest.getRequestApi(printUrlVoucher, "/" + userId + "?id=" + idVoucher, csrfToken,
                unifises);
        return getData;
    }
    ///
    ///

    public JSONArray getDataZabbix(String urlZabbix,String dataGetItemId){
        ApiRequest apiRequest = new ApiRequest();
        String getZabbixInfo = apiRequest.postLoginZabbix(urlZabbix, "/api_jsonrpc.php", dataGetItemId);
        JSONObject convertToObject = new JSONObject(getZabbixInfo);
        JSONArray getResultInfo = convertToObject.getJSONArray("result");
        return getResultInfo;
    }

    public String longestConn(String data, String postData) {
        JSONObject result = new JSONObject();
        JSONObject jsonResult = new JSONObject(data);
        JSONObject getPostData = new JSONObject(postData);
        Integer pos = 0;
        Integer posMax = 0;
        JSONArray getData = jsonResult.getJSONArray("data");
        for (int i=0; i<getData.length(); i++) {
            JSONObject getInfo = (JSONObject) getData.get(i);
            if (getInfo.getInt("uptime") != 0) {
                pos = i;
                break;
            }
        }
        JSONObject getInfo = (JSONObject) getData.get(pos);
        Integer max = getInfo.getInt("uptime");
        for (int i=0; i<getData.length(); i++) {
            JSONObject posUptime = (JSONObject) getData.get(i);
            if (max < posUptime.getInt("uptime")) {
                max = posUptime.getInt("uptime");
            }
        }
        for (int i=0; i<getData.length(); i++) {
            JSONObject posUptime = (JSONObject) getData.get(i);
            if (max == posUptime.getInt("uptime")) {
                posMax = i;
            }
        }
        JSONObject getBytes = (JSONObject) getData.get(posMax);
        Calculator getCalculator = new Calculator();
        long txBytes = getBytes.getLong("rx_bytes");
        long rxBytes = getBytes.getLong("tx_bytes");
        List<String> convertTx = getCalculator.ConvertBytes(txBytes);
        List<String> convertRx = getCalculator.ConvertBytes(rxBytes);
        String up = convertTx.get(0) + convertTx.get(1);
        String down = convertRx.get(0) + convertRx.get(1);
        Integer getUptime = getBytes.getInt("uptime");
        String hostname = "None";
        try {
            hostname = getBytes.getString("hostname");
        } catch (Exception e) {
            hostname = "None";
        }
         
        String uptime = getCalculator.ConvertSecondToHHMMString(getUptime);
        result.put("name",getPostData.getString("Longest_connect"));
        result.put("hostname",hostname);
        result.put("uptime",uptime);
        result.put("up",up);
        result.put("down",down);
        return result.toString();
    }

    public String mostActiveClient(String data, String postData) {
        JSONObject result = new JSONObject();
        JSONObject infoData = new JSONObject(data);
        JSONObject getPostData = new JSONObject(postData);
        JSONArray getData = infoData.getJSONArray("data");
        JSONObject getDataMax = (JSONObject) getData.get(0);
        long max = getDataMax.getLong("tx_bytes") + getDataMax.getLong("rx_bytes");
        Integer posMax = 0;
        for (int i=1; i<getData.length(); i++) {
            JSONObject getPosCompare = (JSONObject) getData.get(i);
            long dataCompare = getPosCompare.getLong("tx_bytes") + getPosCompare.getLong("rx_bytes");
            if (max < dataCompare) {
                max = dataCompare;
            }
        }
        for (int i=0; i<getData.length(); i++) {
            JSONObject getPosCompare = (JSONObject) getData.get(i);
            long dataCompare = getPosCompare.getLong("tx_bytes") + getPosCompare.getLong("rx_bytes");
            if (max <= dataCompare) {
                posMax = i;
            }
        }
        Calculator getCalculator = new Calculator();
        JSONObject getBytes = (JSONObject) getData.get(posMax);
        long txBytes = getBytes.getLong("rx_bytes");
        long rxBytes = getBytes.getLong("tx_bytes");
        List<String> convertTx = getCalculator.ConvertBytes(txBytes);
        List<String> convertRx = getCalculator.ConvertBytes(rxBytes);
        String up = convertTx.get(0) + convertTx.get(1);
        String down = convertRx.get(0) + convertRx.get(1);
        String hostname = "None";
        try {
            hostname = getBytes.getString("hostname");
        } catch (Exception e) {
        }
        result.put("name",getPostData.getString("Most_active_client"));
        result.put("hostname",hostname);
        result.put("up",up);
        result.put("down",down);
        return result.toString();   
    }

    public String mostActiveAp(String data, String postData) {
        JSONObject result = new JSONObject();
        JSONObject infoData = new JSONObject(data);
        JSONArray getData = infoData.getJSONArray("data");
        JSONObject getDataMax = (JSONObject) getData.get(0);
        JSONObject getPostData = new JSONObject(postData);
        long max = 0;
        try {
            max = getDataMax.getLong("bytes");
        } catch (Exception e) {
            //TODO: handle exception
        }
        Integer posMax = 0;
        for (int i=0; i<getData.length(); i++) {
            JSONObject getPosCompare = (JSONObject) getData.get(i);
            long dataCompare = 0;
            try {
                dataCompare = getPosCompare.getLong("bytes");
            } catch (Exception e) {
                //TODO: handle exception
            }
            if (max < dataCompare) {
                max = dataCompare;
            }
        }
        for (int i=0; i<getData.length(); i++) {
            JSONObject getPosCompare = (JSONObject) getData.get(i);
            long dataCompare = 0;
            try {
                dataCompare = getPosCompare.getLong("bytes");
            } catch (Exception e) {
                // TODO: handle exception
            }
            if (max <= dataCompare) {
                posMax = i;
            }
        }
        Calculator getCalculator = new Calculator();
        JSONObject getBytes = (JSONObject) getData.get(posMax);
        long txBytes = getBytes.getLong("rx_bytes");
        long rxBytes = getBytes.getLong("tx_bytes");
        List<String> convertTx = getCalculator.ConvertBytes(txBytes);
        List<String> convertRx = getCalculator.ConvertBytes(rxBytes);
        String up = convertTx.get(0) + convertTx.get(1);
        String down = convertRx.get(0) + convertRx.get(1);
        String hostname = getBytes.getString("name");
        result.put("name",getPostData.getString("Most_active_ap"));
        result.put("hostname",hostname);
        result.put("up",up);
        result.put("down",down);
        return result.toString();   
    }
    
    public String conditionGetData(String userId, @CurrentUser CustomUserDetails currentUser) {
        int dk = 0;
        JSONObject result = new JSONObject();
        Set<Role> strRoles = currentUser.getRoles();
        JSONObject strRolesJson = new JSONObject(strRoles);
        JSONArray getRoles = strRolesJson.getJSONArray("value");
        JSONObject roles = (JSONObject) getRoles.get(0);
        String convertString = roles.toString();
        JSONObject convertObject = new JSONObject(convertString);
        String getRole = convertObject.getString("name");
        String getUsername = currentUser.getName();
        if (getUsername.equals("admin")) {
            result.put("dk", 1);
            result.put("length", 1);
            return result.toString();
        }
        Set<SitesName> strSites = currentUser.getSitename();
        JSONObject strSitesJson = new JSONObject(strSites);
        JSONArray getSites = strSitesJson.getJSONArray("value");
        int length = getSites.length();
        for (int i=0; i<getSites.length(); i++) {
            JSONObject site = (JSONObject) getSites.get(i);
            String getIdname = site.getString("idname");
            if (getIdname.equals(userId) || !getRole.equals("ROLE_KH")) {
                break;
            }
            else {
                dk = dk + 1;
            }
        }
        result.put("dk", dk);
        result.put("length", length);
        return result.toString();
    }

    public String conditionGetTraffic(String userId, @CurrentUser CustomUserDetails currentUser) {
        int dk = 0;
        JSONObject result = new JSONObject();
        Set<Role> strRoles = currentUser.getRoles();
        JSONObject strRolesJson = new JSONObject(strRoles);
        JSONArray getRoles = strRolesJson.getJSONArray("value");
        JSONObject roles = (JSONObject) getRoles.get(0);
        String convertString = roles.toString();
        JSONObject convertObject = new JSONObject(convertString);
        String getRole = convertObject.getString("name");
        String getUsername = currentUser.getName();
        if (getUsername.equals("admin")) {
            result.put("dk", 1);
            result.put("length", 1);
            return result.toString();
        }
        Set<SitesName> strSites = currentUser.getSitename();
        JSONObject strSitesJson = new JSONObject(strSites);
        JSONArray getSites = strSitesJson.getJSONArray("value");
        int length = getSites.length();
        for (int i = 0; i < getSites.length(); i++) {
            JSONObject site = (JSONObject) getSites.get(i);
            String getIdname = site.getString("sitename");
            if (getIdname.equals(userId) || !getRole.equals("ROLE_KH")) {
                break;
            } else {
                dk = dk + 1;
            }
        }
        result.put("dk", dk);
        result.put("length", length);
        return result.toString();
    }

    //Update V2
    @ApiOperation(value = "Stat device")
    @PostMapping("it4u/{id}/stat/device/")
    public String getStatDevice(@PathVariable(value = "id") String userId, @RequestBody String postData,
            @CurrentUser CustomUserDetails currentUser) {
        DashboardController dashboard = new DashboardController();
        JSONObject conditionGetData = new JSONObject(dashboard.conditionGetData(userId, currentUser));
        if (conditionGetData.getInt("dk") == conditionGetData.getInt("length")) {
            return "Access denied!";
        }
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        csrfToken = body.getString("csrfToken");
        unifises = body.getString("unifises");
        JSONObject result = new JSONObject();
        ApiRequest apiRequest = new ApiRequest();
        String getClients = apiRequest.getRequestApi(urlIt4u, "/s/" + userId + "/stat/sta/", csrfToken, unifises);
        String getDevices = apiRequest.getRequestApi(urlIt4u, "/s/" + userId + "/stat/device/", csrfToken, unifises);
        try {
            String quickLook = dashboard.quickLook(getClients, getDevices, postData);
            result.put("quick_look", quickLook);
            String trafficInfo = dashboard.trafficInfo(getClients, postData);
            result.put("traffic_info", trafficInfo);
            String clientSSid = dashboard.clientSSid(getClients);
            result.put("client_ssid", clientSSid);
            String clienUsage = dashboard.clienUsage(getDevices);
            result.put("client_usage", clienUsage);
            String trafficUsage = dashboard.trafficUsage(getDevices);
            result.put("traffic_usage", trafficUsage);
            String radioType = dashboard.radioType(getDevices);
            result.put("radio_type", radioType);
            String apConnect = dashboard.apConnect(getDevices, postData);
            result.put("ap_conn", apConnect);
        } catch (Exception e) {
            //TODO: handle exception
        }
        log.info(currentUser.getUsername() + " - it4u/" + userId + "/stat/device/");
        return result.toString();
    }

    //function
    public String quickLook(String getClients, String getDevices, String postData) {
        DashboardController dashboard = new DashboardController();
        List<String> result = new ArrayList<>();
        try {
            String longestConn = dashboard.longestConn(getClients, postData);
            String mostActiveClient = dashboard.mostActiveClient(getClients, postData);
            String mostActiveAp = dashboard.mostActiveAp(getDevices, postData);
            result.add(longestConn);
            result.add(mostActiveClient);
            result.add(mostActiveAp);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result.toString();
    }
    public String trafficInfo(String getData, String postData) {
        List<String> result = new ArrayList<>();
        JSONObject uploadJson = new JSONObject();
        JSONObject downloadJson = new JSONObject();
        long upload = 0;
        long download = 0;
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            upload = upload + getInfo.getInt("rx_rate");
            download = download + getInfo.getInt("tx_rate");
        }
        Calculator getCalculator = new Calculator();
        double convertUploadToMb = getCalculator.convertBytesToMb(upload);
        double convertDownloadToMb = getCalculator.convertBytesToMb(download);
        JSONObject getPostData = new JSONObject(postData);
        uploadJson.put("name", getPostData.getString("Upload"));
        uploadJson.put("y", convertUploadToMb);
        downloadJson.put("name", getPostData.getString("Download"));
        downloadJson.put("y", convertDownloadToMb);
        result.add(uploadJson.toString());
        result.add(downloadJson.toString());
        return result.toString();
    }
    //
    public String clienUsage(String getData) {
        JSONObject getResult = new JSONObject();
        List<String> result = new ArrayList<>();
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i=0; i<data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            getResult.put("name",getInfo.getString("name"));
            getResult.put("y",getInfo.getInt("num_sta"));
            result.add(getResult.toString());
        }
        return result.toString();
    }

    public String trafficUsage(String getData) {
        JSONObject getResult = new JSONObject();
        List<String> result = new ArrayList<>();
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        Calculator getCalculator = new Calculator();
        for (int i = 0; i < data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            double convertTraffic = 0;
            try {
                long traffic = getInfo.getLong("bytes");
                convertTraffic = getCalculator.convertBytesToGb(traffic);
            } catch (Exception e) {
                // TODO: handle exception
            }
            getResult.put("name", getInfo.getString("name"));
            getResult.put("y", convertTraffic);
            result.add(getResult.toString());
        }
        return result.toString();
    }

    public String radioType(String getData) {
        Integer lowRadio = 0;
        Integer highRadio = 0;
        List<String> result = new ArrayList<>();
        JSONObject getLowRadio = new JSONObject();
        JSONObject getHighRadio = new JSONObject();
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            JSONArray getRadioTable = getInfo.getJSONArray("radio_table_stats");
            for (int j = 0; j < getRadioTable.length(); j++) {
                JSONObject getInfoRadio = (JSONObject) getRadioTable.get(j);
                if (getInfoRadio.getString("radio").equals("ng")) {
                    lowRadio = lowRadio + getInfoRadio.getInt("num_sta");
                }
                if (getInfoRadio.getString("radio").equals("na")) {
                    highRadio = highRadio + getInfoRadio.getInt("num_sta");
                }
            }
        }
        getLowRadio.put("name", "2.4 GHz");
        getLowRadio.put("y", lowRadio);
        result.add(getLowRadio.toString());
        getHighRadio.put("name", "5 GHz");
        getHighRadio.put("y", highRadio);
        result.add(getHighRadio.toString());
        return result.toString();
    }

    public String apConnect(String getData, String postData) {
        Integer countConn = 0;
        Integer countDisConn = 0;
        List<String> result = new ArrayList<>();
        JSONObject getConn = new JSONObject();
        JSONObject getDisConn = new JSONObject();
        JSONObject getPostData = new JSONObject(postData);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            if (getInfo.getInt("state") == 1) {
                countConn = countConn + 1;
            } else {
                countDisConn = countDisConn + 1;
            }
        }
        getConn.put("name", getPostData.getString("Connected"));
        getConn.put("y", countConn);
        result.add(getConn.toString());
        getDisConn.put("name", getPostData.getString("Disconnected"));
        getDisConn.put("y", countDisConn);
        result.add(getDisConn.toString());
        return result.toString();
    }

    public String clientSSid(String getData) {
        List<String> listSsid = new ArrayList<>();
        List<String> result = new ArrayList<>();
        JSONObject getResult = new JSONObject();
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        try {
            JSONObject itemDevice0 = (JSONObject) data.get(0);
            String devices0 = itemDevice0.getString("essid");
            listSsid.add(devices0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            for (int i = 0; i < data.length(); i++) {
                int k = 0;
                JSONObject listDevice = (JSONObject) data.get(i);
                for (int j = 0; j < listSsid.size(); j++) {
                    if (listDevice.getString("essid").equals(listSsid.get(j))) {
                        k = k + 1;
                    }
                }
                if (k == 0) {
                    listSsid.add(listDevice.getString("essid"));
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        for (int j = 0; j < listSsid.size(); j++) {
            int k = 0;
            try {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject listDevice = (JSONObject) data.get(i);
                    if (listDevice.getString("essid").equals(listSsid.get(j))) {
                        k = k + 1;
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            getResult.put("name", listSsid.get(j));
            getResult.put("y", k);
            result.add(getResult.toString());
        }
        return result.toString();
    }

    
}
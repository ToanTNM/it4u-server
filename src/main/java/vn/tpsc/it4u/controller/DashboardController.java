package vn.tpsc.it4u.controller;

import vn.tpsc.it4u.util.ApiResponseUtils;
import vn.tpsc.it4u.common.ApiRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import vn.tpsc.it4u.common.Calculator;
@RestController
@RequestMapping("${app.api.version}")
public class DashboardController {
    @Value("${app.ubnt.url}")
    private String urlIt4u;

    @Value("${app.ubnt.csrf_token}")
    private String csrfToken;

    @Value("${app.ubnt.unifises}")
    private String unifises;

    @Value("${app.dev.url}")
    private String urlDev;

    @Value("${app.dev.token}")
    private String tokenDev;
    
    String sitesid="/stat/sites";
    @Autowired 
    ApiResponseUtils apiResponse;
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
    public String getClientEssid(@PathVariable(value = "id") String userId) {
        int pos = 0;
        ApiRequest apiRequest = new  ApiRequest();
        List<String> nameSsid = new ArrayList<>();
        List<String> result = new ArrayList<>();
        List<Integer> countClient = new ArrayList<>();
        JSONObject getResult = new JSONObject();
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/device/",csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i=0; i<data.length(); i++) {
            JSONObject listDevice = (JSONObject) data.get(i);
            JSONArray device = listDevice.getJSONArray("vap_table");
            if(device.toString() != "") {
                pos = i;
                break;
            }  
        }
        JSONObject device = (JSONObject) data.get(pos);
        JSONArray vapTable = device.getJSONArray("vap_table");
        Integer len = vapTable.length();
        if (len%2 != 0){
            len = len + 1;
        }
        Integer varPr = Math.round(len/2);
        for ( int i=0; i<varPr; i++)
        {
            JSONObject getName = (JSONObject) vapTable.get(i);
            String name = getName.getString("essid");
            nameSsid.add(name);
        }
        for (int i=0; i<nameSsid.size(); i++) {
            int countName = 0;
            for (int j=pos; j<data.length(); j++) {
                JSONObject deviceI = (JSONObject) data.get(j);
                JSONArray varTableI = deviceI.getJSONArray("vap_table");
                for (int k=0; k<varTableI.length(); k++) {
                    JSONObject getVarTable = (JSONObject) varTableI.get(k);
                    String getSessid = getVarTable.getString("essid");
                    String getNameSsid = nameSsid.get(i).toString();
                    if (getSessid.equals(getNameSsid)) {
                        Integer getNumSta = getVarTable.getInt("num_sta");
                        countName = countName + getNumSta;
                    }
                }
            }
            countClient.add(countName);
        }

        for (int i=0; i<Math.round(len/2); i++) {
            
            if ( countClient.get(i) != 0) {
                JSONObject getName = (JSONObject) vapTable.get(i);
                String name = getName.getString("essid");
                getResult.put("name",name);
                getResult.put("y",countClient.get(i));
                result.add(getResult.toString());
            }
             
        }
        return result.toString();
    }

    @ApiOperation(value = "Current client usage")
    @GetMapping("it4u/{id}/clientUsage")
    public String getClientUsage(@PathVariable(value = "id") String userId) {
        JSONObject getResult = new JSONObject();
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
    public String getMacAp(@PathVariable(value = "id") String userId) {
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
    public String getTrafficUsage(@PathVariable(value = "id") String userId) {
        JSONObject getResult = new JSONObject();
        List<String> result = new ArrayList<>();
        ApiRequest apiRequest = new  ApiRequest();
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/device/",csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i=0; i<data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            long traffic = getInfo.getLong("bytes");
            getResult.put("name",getInfo.getString("name"));
            getResult.put("y",traffic);
            result.add(getResult.toString());

        }
        return result.toString();
    }

    @ApiOperation(value = "Per radio type")
    @GetMapping("it4u/{id}/radioType")
    public String getClientRadio(@PathVariable(value = "id") String userId) {
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
            lowRadio = lowRadio + getInfo.getInt("ng-num_sta");
            highRadio = highRadio + getInfo.getInt("na-num_sta");
        }
        getLowRadio.put("name","2.4 GH");
        getLowRadio.put("y",lowRadio);
        result.add(getLowRadio.toString());
        getHighRadio.put("name","5 GH");
        getHighRadio.put("y",highRadio);
        result.add(getHighRadio.toString());
        return result.toString();
    }

    @ApiOperation(value = "Quick look")
    @GetMapping("it4u/{id}/quickLook")
    public String getQuickLook(@PathVariable(value = "id") String userId) {
        ApiRequest apiRequest = new ApiRequest();
        List<String> result = new ArrayList<>();
        String getClients = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/sta/",csrfToken,unifises);
        String getDivices = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/device/",csrfToken,unifises);
        DashboardController dashboard = new DashboardController();
        String longestConn = dashboard.longestConn(getClients);
        String mostActiveClient = dashboard.mostActiveClient(getClients);
        String mostActiveAp = dashboard.mostActiveAp(getDivices);
        result.add(longestConn);
        result.add(mostActiveClient);
        result.add(mostActiveAp);
        return result.toString();
    }

    @ApiOperation(value = "Hourly Client")
    @PostMapping("it4u/{id}/hourlyClient")
    public String getHourlyClient(@RequestBody String postData,@PathVariable(value = "id") String userId) {
        List<String> result = new ArrayList<>();
        List<Integer> listClient = new ArrayList<Integer>();
        List<Long> listTraffic = new ArrayList<Long>();
        JSONObject listClientJson = new JSONObject();
        JSONObject listTrafficJson = new JSONObject();

        ApiRequest apiRequest = new ApiRequest();
        // String getMinute = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/hourly.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPosZero = (JSONObject) dataHourly.get(0);
        Integer countClient0 = getPosZero.getInt("wlan-num_sta");
        Long countTraffic0 = getPosZero.getLong("wlan_bytes");
        listClient.add(countClient0);
        listTraffic.add(countTraffic0);
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Integer countClient = getPosEnd.getInt("wlan-num_sta");
                Long countTraffic = getPosEnd.getLong("wlan_bytes");
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

    @ApiOperation(value = "5 minutes Client")
    @PostMapping("it4u/{id}/5MinutesClient")
    public String getMinutesClient(@RequestBody String postData,@PathVariable(value = "id") String userId) {
        List<String> result = new ArrayList<>();
        List<Integer> listClient = new ArrayList<Integer>();
        List<Long> listTraffic = new ArrayList<Long>();
        JSONObject listClientJson = new JSONObject();
        JSONObject listTrafficJson = new JSONObject();

        ApiRequest apiRequest = new ApiRequest();
        // String getMinute = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPosZero = (JSONObject) dataHourly.get(0);
        Integer countClient0 = getPosZero.getInt("wlan-num_sta");
        Long countTraffic0 = getPosZero.getLong("wlan_bytes");
        listClient.add(countClient0);
        listTraffic.add(countTraffic0);
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Integer countClient = getPosEnd.getInt("wlan-num_sta");
                Long countTraffic = getPosEnd.getLong("wlan_bytes");
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

    @ApiOperation(value = "Daily Client")
    @PostMapping("it4u/{id}/dailyClient")
    public String getDailyClient(@RequestBody String postData,@PathVariable(value = "id") String userId) {
        List<String> result = new ArrayList<>();
        List<Integer> listClient = new ArrayList<Integer>();
        List<Long> listTraffic = new ArrayList<Long>();
        JSONObject listClientJson = new JSONObject();
        JSONObject listTrafficJson = new JSONObject();

        ApiRequest apiRequest = new ApiRequest();
        // String getMinute = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/daily.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPosZero = (JSONObject) dataHourly.get(0);
        Integer countClient0 = getPosZero.getInt("wlan-num_sta");
        Long countTraffic0 = getPosZero.getLong("wlan_bytes");
        listClient.add(countClient0);
        listTraffic.add(countTraffic0);
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Integer countClient = getPosEnd.getInt("wlan-num_sta");
                Long countTraffic = getPosEnd.getLong("wlan_bytes");
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

    @ApiOperation(value = "Daily AP")
    @PostMapping("it4u/{id}/dailyAP")
    public String getDailyAP(@RequestBody String postData,@PathVariable(value = "id") String userId) {
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
    public String getHourlyAP(@RequestBody String postData,@PathVariable(value = "id") String userId) {
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
    public String get5MinutesAP(@RequestBody String postData,@PathVariable(value = "id") String userId) {
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
    public String getTimeHourly(@RequestBody String postData,@PathVariable(value = "id") String userId) {
        List<String> listTime = new ArrayList<>();
        JSONObject listResult = new JSONObject();
        long getTimeMax = 0;
        long getTimeMin = 0;
        int sum = 0;
        int avg = 0;
        int k = 0;
        ApiRequest apiRequest = new ApiRequest();
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/hourly.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        JSONObject getPos = (JSONObject) dataHourly.get(0);
        Integer maxClient = getPos.getInt("wlan-num_sta");
        Integer minClient = getPos.getInt("wlan-num_sta");
        Calculator getCalculator = new Calculator();
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Long time = getPosStart.getLong("time");  
                String uptime = getCalculator.ConvertSecondToDate(time);
                if (maxClient <= getPosStart.getInt("wlan-num_sta")) {
                    maxClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMax = getPosStart.getLong("time");
                }
                if (minClient >= getPosStart.getInt("wlan-num_sta")) {
                    minClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMin = getPosStart.getLong("time");
                }
                sum = sum + getPosStart.getInt("wlan-num_sta");
                k = k + 1;
                listTime.add(uptime.toString());
				
            }

        }
        avg = Math.round(sum/k);
        String timeMax = getCalculator.ConvertSecondToDate(getTimeMax);
        String timeMin = getCalculator.ConvertSecondToDate(getTimeMin);
        listResult.put("time",listTime);
        listResult.put("timeMax",timeMax);
        listResult.put("clientMax",maxClient);
        listResult.put("timeMin",timeMin);
        listResult.put("clientMin",minClient);
        listResult.put("clientAverage",avg);
        return listResult.toString();
    }

    @ApiOperation(value = "Get time 5 minutes")
    @PostMapping("it4u/{id}/getTimeMinute")
    public String getTimeMinute(@RequestBody String postData,@PathVariable(value = "id") String userId) {
        List<String> listTime = new ArrayList<>();
        JSONObject listResult = new JSONObject();
        long getTimeMax = 0;
        long getTimeMin = 0;
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
        Calculator getCalculator = new Calculator();
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Long time = getPosStart.getLong("time");  
                String uptime = getCalculator.ConvertSecondToDate(time);
                if (maxClient <= getPosStart.getInt("wlan-num_sta")) {
                    maxClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMax = getPosStart.getLong("time");
                }
                if (minClient >= getPosStart.getInt("wlan-num_sta")) {
                    minClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMin = getPosStart.getLong("time");
                }
                sum = sum + getPosStart.getInt("wlan-num_sta");
                k = k + 1;
                listTime.add(uptime.toString());
				
            }

        }
        avg = Math.round(sum/k);
        String timeMax = getCalculator.ConvertSecondToDate(getTimeMax);
        String timeMin = getCalculator.ConvertSecondToDate(getTimeMin);
        listResult.put("time",listTime);
        listResult.put("timeMax",timeMax);
        listResult.put("clientMax",maxClient);
        listResult.put("timeMin",timeMin);
        listResult.put("clientMin",minClient);
        listResult.put("clientAverage",avg);
        return listResult.toString();
    }

    @ApiOperation(value = "Get time daily")
    @PostMapping("it4u/{id}/getTimeDaily")
    public String getTimeDaily(@RequestBody String postData,@PathVariable(value = "id") String userId) {
        List<String> listTime = new ArrayList<>();
        JSONObject listResult = new JSONObject();
        long getTimeMax = 0;
        long getTimeMin = 0;
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
        Calculator getCalculator = new Calculator();
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Long time = getPosStart.getLong("time");  
                String uptime = getCalculator.ConvertSecondToDate(time);
                if (maxClient <= getPosStart.getInt("wlan-num_sta")) {
                    maxClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMax = getPosStart.getLong("time");
                }
                if (minClient >= getPosStart.getInt("wlan-num_sta")) {
                    minClient = getPosStart.getInt("wlan-num_sta");
                    getTimeMin = getPosStart.getLong("time");
                }
                sum = sum + getPosStart.getInt("wlan-num_sta");
                k = k + 1;
                listTime.add(uptime.toString());
				
            }

        }
        avg = Math.round(sum/k);
        String timeMax = getCalculator.ConvertSecondToDate(getTimeMax);
        String timeMin = getCalculator.ConvertSecondToDate(getTimeMin);
        listResult.put("time",listTime);
        listResult.put("timeMax",timeMax);
        listResult.put("clientMax",maxClient);
        listResult.put("timeMin",timeMin);
        listResult.put("clientMin",minClient);
        listResult.put("clientAverage",avg);
        return listResult.toString();
    }

    @ApiOperation(value = "Count AP Connect")
    @GetMapping("it4u/{id}/apCount")
    public String getAPConnect(@PathVariable(value = "id") String userId) {
        Integer countConn = 0;
        Integer countDisConn = 0;
        List<String> result = new ArrayList<>();
        JSONObject getConn = new JSONObject();
        JSONObject getDisConn = new JSONObject();
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
        getConn.put("name","AP Connected");
        getConn.put("y",countConn);
        result.add(getConn.toString());
        getDisConn.put("name","AP Disconnected");
        getDisConn.put("y",countDisConn);
        result.add(getDisConn.toString());
        return result.toString();
    }

    @ApiOperation(value = "Hotspot")
    @GetMapping("it4u/{id}/hotspot/{start}/{end}")
    public String getHotspot(@PathVariable(value = "id") String userId,@PathVariable(value = "start") String start, @PathVariable(value = "end") String end) {
        Integer newClient = 0;
        Integer returnClient = 0;
        List<String> result = new ArrayList<>();
        JSONObject getNewClient = new JSONObject();
        JSONObject getReturnClient = new JSONObject();
        ApiRequest apiRequest = new ApiRequest();
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/guest?start=" + start + "&end="+ end,csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i=0; i<data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            Boolean temp = getInfo.getBoolean("is_returning");
            if (!temp) {
                newClient = newClient + 1;
            }
            else {
                returnClient = returnClient + 1;
            }
        }
        getNewClient.put("name","New");
        getNewClient.put("y",newClient);
        getReturnClient.put("name","Returning");
        getReturnClient.put("y", returnClient);
        result.add(getNewClient.toString());
        result.add(getReturnClient.toString());
        return result.toString();
    }

    //Dashboard 2
    @ApiOperation(value = "Customer info")
    @GetMapping("it4u/{id}/customerInfo")
    public String getCustomerInfo(@PathVariable(value="id") String siteName){
        String wan2Status = "DOWN";
        String wan1Status = "DOWN";
        String wan3Status = "DOWN";
        List<String> result = new ArrayList<>();
        JSONObject wan1 = new JSONObject();
        JSONObject wan2 = new JSONObject();
        JSONObject wan3 = new JSONObject();
        JSONObject lb = new JSONObject();
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
        String wan1Uptime = convert.ConvertSecondToHHMMString(getWan1Uptime);
        wan1.put("wanIp", wan1Ip);
        wan1.put("wanProvider", "Kênh Truyền 1");
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
            wan2.put("wanProvider", "Kênh Truyền 2");
            wan2.put("wanStatus", wan2Status);
            wan2.put("wanUptime", wan2Uptime);
            result.add(wan2.toString());
            Integer getLbUptime = data.getInt("uptime_lb");
            String lbUptime = convert.ConvertSecondToHHMMString(getLbUptime);
            lb.put("wanProvider", "Router LoadBalance");
            lb.put("wanUptime", lbUptime);
            result.add(lb.toString());
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
            wan3.put("wanProvider", "Kênh Truyền 3");
            wan3.put("wanStatus", wan3Status);
            wan3.put("wanUptime", wan3Uptime);
            result.add(wan3.toString());
        } catch (Exception e) {
        }
        return result.toString();
    }

    @ApiOperation(value = "Traffic AP")
    @GetMapping("it4u/{id}/trafficInfo")
    public String getTrafficInfo(@PathVariable(value="id") String idUser){
        List<String> result = new ArrayList<>();
        JSONObject rate = new JSONObject();
        JSONObject uploadJson = new JSONObject();
        JSONObject downloadJson = new JSONObject();
        ApiRequest apiRequest = new ApiRequest();
        long upload = 0;
        long download = 0;
        Calculator convert = new Calculator();
        String getData = apiRequest.getRequestApi(urlIt4u,"/s/" + idUser + "/stat/sta/",csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getData);
        JSONArray data = jsonResult.getJSONArray("data");
        
        for (int i=0; i<data.length(); i++) {
            JSONObject getInfo = (JSONObject) data.get(i);
            upload = upload + getInfo.getInt("tx_rate");
            download = download + getInfo.getInt("rx_rate");
        }
        uploadJson.put("name","Upload");
        uploadJson.put("y",upload*8);
        downloadJson.put("name","Download");
        downloadJson.put("y",download*8);
        result.add(uploadJson.toString());
        result.add(downloadJson.toString());
        return result.toString();
    }


    public String longestConn(String data) {
        JSONObject result = new JSONObject();
        JSONObject jsonResult = new JSONObject(data);
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
        long txBytes = getBytes.getLong("tx_bytes");
        long rxBytes = getBytes.getLong("rx_bytes");
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
        result.put("name","Longest Connect");
        result.put("hostname",hostname);
        result.put("uptime",uptime);
        result.put("up",up);
        result.put("down",down);
        return result.toString();
    }

    public String mostActiveClient(String data) {
        JSONObject result = new JSONObject();
        JSONObject infoData = new JSONObject(data);
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
        long txBytes = getBytes.getLong("tx_bytes");
        long rxBytes = getBytes.getLong("rx_bytes");
        List<String> convertTx = getCalculator.ConvertBytes(txBytes);
        List<String> convertRx = getCalculator.ConvertBytes(rxBytes);
        String up = convertTx.get(0) + convertTx.get(1);
        String down = convertRx.get(0) + convertRx.get(1);
        String hostname = getBytes.getString("hostname");
        result.put("name","Most active client");
        result.put("hostname",hostname);
        result.put("up",up);
        result.put("down",down);
        return result.toString();   
    }

    public String mostActiveAp(String data) {
        JSONObject result = new JSONObject();
        JSONObject infoData = new JSONObject(data);
        JSONArray getData = infoData.getJSONArray("data");
        JSONObject getDataMax = (JSONObject) getData.get(0);
        long max = getDataMax.getLong("bytes");
        Integer posMax = 0;
        for (int i=0; i<getData.length(); i++) {
            JSONObject getPosCompare = (JSONObject) getData.get(i);
            long dataCompare = getPosCompare.getLong("bytes");
            if (max < dataCompare) {
                max = dataCompare;
            }
        }
        for (int i=0; i<getData.length(); i++) {
            JSONObject getPosCompare = (JSONObject) getData.get(i);
            long dataCompare = getPosCompare.getLong("bytes");
            if (max <= dataCompare) {
                posMax = i;
            }
        }
        Calculator getCalculator = new Calculator();
        JSONObject getBytes = (JSONObject) getData.get(posMax);
        long txBytes = getBytes.getLong("tx_bytes");
        long rxBytes = getBytes.getLong("rx_bytes");
        List<String> convertTx = getCalculator.ConvertBytes(txBytes);
        List<String> convertRx = getCalculator.ConvertBytes(rxBytes);
        String up = convertTx.get(0) + convertTx.get(1);
        String down = convertRx.get(0) + convertRx.get(1);
        String hostname = getBytes.getString("name");
        result.put("name","Most active ap");
        result.put("hostname",hostname);
        result.put("up",up);
        result.put("down",down);
        return result.toString();   
    }

}
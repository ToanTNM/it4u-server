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
    
    String sitesid="/stat/sites";
    @Autowired 
    ApiResponseUtils apiResponse;
    @ApiOperation(value = "Sites id")
    @GetMapping("/it4u/sites")
    public String getSitesId() {
        ApiRequest apiRequest = new  ApiRequest();
        JSONObject result = new JSONObject();
        List<String> dataList = new ArrayList<>();
        String getSites = apiRequest.getRequestApi(urlIt4u,sitesid,csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getSites);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i=0;i<data.length();i++)
        {
            JSONObject getData = (JSONObject) data.get(i);
            String siteName = getData.getString("desc");
            String idName = getData.getString("name");
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
    public String getDailyClient(@RequestBody String postData,@PathVariable(value = "id") String userId) {
        int posMax = 0;
        int posMin = 0;
        int k = 0;
        List<String> result = new ArrayList<>();
        List<Integer> listMax = new ArrayList<Integer>();
        List<Integer> listMin = new ArrayList<Integer>();
        List<Integer> listClient = new ArrayList<Integer>();
        List<Long> listTraffic = new ArrayList<Long>();
        JSONObject listMaxJson = new JSONObject();
        JSONObject listMinJson = new JSONObject();
        JSONObject listClientJson = new JSONObject();
        JSONObject listTrafficJson = new JSONObject();

        ApiRequest apiRequest = new ApiRequest();
        String getMinute = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/5minutes.site/",csrfToken,unifises,postData);
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/hourly.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                JSONObject getDataMinute = new JSONObject(getMinute);
                JSONArray dataMinute = getDataMinute.getJSONArray("data");
                JSONObject getPos = (JSONObject) dataMinute.get(k);
                Integer maxClient = getPos.getInt("wlan-num_sta");
                Integer minClient = getPos.getInt("wlan-num_sta");
                for ( int j=k+1; j<dataMinute.length(); j++) {
                    JSONObject getPosMaxMin = (JSONObject) dataMinute.get(j);
                    if ( getPosMaxMin.getLong("time") >= startTime) {
                        if (maxClient < getPosMaxMin.getInt("wlan-num_sta")) {
                            maxClient = getPosMaxMin.getInt("wlan-num_sta");
                            posMax = j;
                        }
                        if (minClient > getPosMaxMin.getInt("wlan-num_sta")) {
                            minClient = getPosMaxMin.getInt("wlan-num_sta");
                            posMin = j;
                        }
                    }
                    if (getPosMaxMin.getLong("time") >= endTime) {
                        k = j;
                        break;
                    }
                }
            Integer countClient = getPosStart.getInt("wlan-num_sta");
            Long countTraffic = getPosStart.getLong("wlan_bytes");
            listMax.add(maxClient);
            listMin.add(minClient);
            listClient.add(countClient);
            listTraffic.add(countTraffic);
            }

        }
        listMaxJson.put("name","Client Max");
        listMaxJson.put("data", listMax);
        listMaxJson.put("yAxis",1);
        listMinJson.put("name","Client Min");
        listMinJson.put("data", listMin);
        listMinJson.put("yAxis",1);
        listClientJson.put("name","Client");
        listClientJson.put("data", listClient);
        listClientJson.put("yAxis",1);
        listTrafficJson.put("name","Traffic");
        listTrafficJson.put("data", listTraffic);
        listTrafficJson.put("yAxis",0);
        result.add(listMaxJson.toString());
        result.add(listMinJson.toString());
        result.add(listClientJson.toString());
        result.add(listTrafficJson.toString());
        return result.toString();
    }
    
    @ApiOperation(value = "Get Time")
    @PostMapping("it4u/{id}/getTimeChart")
    public String getTimeChart(@RequestBody String postData,@PathVariable(value = "id") String userId) {
        List<String> listTime = new ArrayList<>();
        JSONObject listResult = new JSONObject();

        ApiRequest apiRequest = new ApiRequest();
        String getHourly = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/stat/report/hourly.site/",csrfToken,unifises,postData);
        JSONObject getData = new JSONObject(getHourly);
        JSONArray dataHourly = getData.getJSONArray("data");
        for (int i=0; i<dataHourly.length()-1; i++) {
            JSONObject getPosStart = (JSONObject) dataHourly.get(i);
            JSONObject getPosEnd = (JSONObject) dataHourly.get(i+1);
            long startTime = getPosStart.getLong("time");
            long endTime = getPosEnd.getLong("time");
            if (startTime != endTime) {
                Long time = getPosStart.getLong("time");
                Calculator getCalculator = new Calculator();
                String uptime = getCalculator.ConvertSecondToDate(time);
				listTime.add(uptime.toString());
            }

        }
        listResult.put("time",listTime);
        
        return listResult.toString();
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
        String hostname = getBytes.getString("hostname");
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
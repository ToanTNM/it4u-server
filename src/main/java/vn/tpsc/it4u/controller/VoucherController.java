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
public class VoucherController {
    @Value("${app.ubnt.url}")
    private String urlIt4u;

    @Value("${app.ubnt.csrf_token}")
    private String csrfToken;

    @Value("${app.ubnt.unifises}")
    private String unifises;
    
    String printUrlVoucher="https://ubnt.cloud.tpsc.vn/print/hotspot/vouchers/s";
    @Autowired 
    ApiResponseUtils apiResponse;
    @ApiOperation(value = "Create Voucher")
    @PostMapping("it4u/{id}/createVoucher")
    public String postVoucher(@RequestBody String postData,@PathVariable(value = "id") String userId) {
        ApiRequest apiRequest = new ApiRequest();
        String postVoucher = apiRequest.postRequestApi(urlIt4u,"/s/" + userId + "/cmd/hotspot",csrfToken,unifises,postData);
        return postVoucher;
    }

    @ApiOperation(value = "Get Voucher")
    @GetMapping("it4u/{id}/voucher")
    public String getVoucher(@PathVariable(value = "id") String userId) {
        ApiRequest apiRequest = new ApiRequest();
        JSONObject result = new JSONObject();
        Integer down = 0;
        Integer up = 0;
        Integer byteQuota = 0;
        Calculator getCalculator = new Calculator();
        List<String> dataList = new ArrayList<>();
        String getRequest = apiRequest.getRequestApi(urlIt4u,"/s/" + userId + "/stat/voucher",csrfToken,unifises);
        JSONObject jsonResult = new JSONObject(getRequest);
        JSONArray data = jsonResult.getJSONArray("data");
        for (int i=0;i<data.length();i++)
        {
            JSONObject getData = (JSONObject) data.get(i);
            String id = getData.getString("_id");
            String code = getData.getString("code");
            long getTime = getData.getLong("create_time");
            String createTime = getCalculator.ConvertSecondToDate(getTime*1000);
            // Integer down = getData.getInt("qos_rate_max_down");
            try {  
                down = getData.getInt("qos_rate_max_down");
            }  
            catch(Exception e) {
                down = 0;
            } 
            result.put("down",down + " Kbps");
            try {  
                up = getData.getInt("qos_rate_max_down");
            }  
            catch(Exception e) {
                up = 0;
            }
            result.put("up",up + " Kbps");
            try {  
                byteQuota = getData.getInt("qos_usage_quota");
            }  
            catch(Exception e) {
                byteQuota = 0;
            }
            result.put("byteQuota",byteQuota + " MB");
            String note = getData.getString("note");
            Integer getDuration = getData.getInt("duration");
            String duration = getCalculator.ConvertSecondToHHMMString(getDuration);
            String status = getData.getString("status");
            result.put("id",id);
            result.put("code",code);
            result.put("createTime",createTime);
            result.put("notes",note);
            result.put("duration",duration);
            result.put("status",status);
            dataList.add(result.toString());
        }
        return dataList.toString();
    }

    @ApiOperation(value = "Delete Voucher")
    @PostMapping("it4u/{id}/deleteVoucher")
    public String deleteVoucher(@PathVariable(value = "id") String userId, @RequestBody String postData) {
        ApiRequest apiRequest = new ApiRequest();
        String getData = apiRequest.postRequestApi(urlIt4u, "/s/" + userId + "/cmd/hotspot",csrfToken,unifises,postData);
        return getData;
    }

    @ApiOperation(value = "Print Voucher")
    @GetMapping("it4u/{id}/printVoucher/{idVoucher}")
    public String printVoucher(@PathVariable(value = "id") String userId, @PathVariable(value = "idVoucher") String idVoucher) {
        ApiRequest apiRequest = new ApiRequest();
        String getData = apiRequest.getRequestApi(printUrlVoucher, "/" + userId + "?id=" + idVoucher,csrfToken,unifises);
        return getData;
    }
}
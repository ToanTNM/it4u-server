package vn.tpsc.it4u.controller;

import vn.tpsc.it4u.util.ApiResponseUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import vn.tpsc.it4u.model.GroupClient;
import vn.tpsc.it4u.model.Reporter;
import vn.tpsc.it4u.payload.ReporterSummary;
import vn.tpsc.it4u.util.ApiRequest;
import vn.tpsc.it4u.util.Calculator;
import vn.tpsc.it4u.repository.ReporterRepository;
import vn.tpsc.it4u.repository.GroupClientRepository;
import vn.tpsc.it4u.service.ConfigTokenService;
import vn.tpsc.it4u.service.GroupClientService;
import vn.tpsc.it4u.service.ReporterService;
import vn.tpsc.it4u.service.SitesNameService;

@RestController
@RequestMapping("${app.api.version}")
public class ReporterController {
    @Value("${app.ubnt.url}")
    public String urlIt4u;

    @Autowired
    ConfigTokenService configTokenService;

    @Autowired
    ReporterRepository reporterRepository;

    @Autowired
    GroupClientRepository groupClientRepository;

    @Autowired
    SitesNameService sitenameService;

    @Autowired
    GroupClientService groupClientService;

    @Autowired
    ReporterService reporterService;

    @Autowired
    private ModelMapper mapper;

    ApiResponseUtils apiResponse;

    @ApiOperation(value = "Get information from all clients")
    @GetMapping("it4u/customer_info")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(reporterService.findAll());
    }

    @ApiOperation(value = "Get client from all Clients ")
    @GetMapping("it4u/{id}/customer_info")
    public ResponseEntity<?> getGroupClient(@PathVariable(value = "id") List<Long> sitename) {
        return ResponseEntity.ok(reporterService.findById(sitename));
    }

    @ApiOperation(value = "Get client from service")
    @GetMapping("it4u/customer_info/{service}")
    public ResponseEntity<?> getGroupClientFromServices(@PathVariable(value = "service") String service) {
        if (service.equals("ALL")) {
            return ResponseEntity.ok(reporterService.findAll());
        }
        reporterService.findByService(service);
        return ResponseEntity.ok(reporterService.findByService(service));
    }

    @ApiOperation(value = "Get information from clients")
    @PostMapping("it4u/customer_info")
    public ResponseEntity<?> PostInfoClient(@RequestBody final Reporter bodyData) {
        Reporter reporter = mapper.map(bodyData, Reporter.class);
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        String csrfToken = body.getString("csrfToken");
        String unifises = body.getString("unifises");
        ApiRequest apiRequest = new ApiRequest();
        String Upload = "0";
        String Download = "0";
        String countAPConnected = "0";
        String countAPDisconnected = "0";
        long upload = 0;
        long download = 0;
        Integer countConn = 0;
        Integer countDisConn = 0;
        String siteNameTest = "";
        String idUser = "";
        JSONArray getSitename = new JSONArray(sitenameService.findAll());
        for (int i = 0; i < getSitename.length(); i++) {
            JSONObject getDataSitename = (JSONObject) getSitename.get(i);
            try {
                siteNameTest = getDataSitename.getString("sitename");
                if (siteNameTest.equals(reporter.getSitename())) {
                    idUser = getDataSitename.getString("idname");
                    break;
                }        
            }
            catch (Exception e) {
            }
        }
        try {
             String getData = apiRequest.getRequestApi(urlIt4u, "/s/" + idUser + "/stat/sta/", csrfToken, unifises);
            JSONObject jsonResult = new JSONObject(getData);
            JSONArray data = jsonResult.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject getInfo = (JSONObject) data.get(i);
                upload = upload + getInfo.getInt("rx_rate");
                download = download + getInfo.getInt("tx_rate");
            }
            Calculator getCalculator = new Calculator();
            double convertUploadToMb = getCalculator.convertBytesToMb(upload);
            Upload = Double.toString(convertUploadToMb);
            double convertDownloadToMb = getCalculator.convertBytesToMb(download);
            Download = Double.toString(convertDownloadToMb);
            // AP connected
            String getDataAP = apiRequest.getRequestApi(urlIt4u, "/s/" + idUser + "/stat/device/", csrfToken, unifises);
            JSONObject jsonResultAP = new JSONObject(getDataAP);
            JSONArray dataAP = jsonResultAP.getJSONArray("data");
            for (int i = 0; i < dataAP.length(); i++) {
                JSONObject getInfo = (JSONObject) dataAP.get(i);
                if (getInfo.getInt("state") == 1) {
                    countConn = countConn + 1;
                } else {
                    countDisConn = countDisConn + 1;
                }
            }
            countAPConnected = Integer.toString(countConn);
            countAPDisconnected = Integer.toString(countDisConn);
        } catch (Exception e) {
            //TODO: handle exception
        }
        if (!reporterRepository.existsBySitename(reporter.getSitename())) {
            final Reporter createSitename = new Reporter(
                reporter.getSitename(),
                reporter.getWan1Ip(), 
                reporter.getWan1Provider(), 
                reporter.getWan1Status(),
                reporter.getWan1Uptime(),
                reporter.getWan2Ip(), 
                reporter.getWan2Provider(), 
                reporter.getWan2Status(),
                reporter.getWan2Uptime(),
                reporter.getWan3Ip(), 
                reporter.getWan3Provider(), 
                reporter.getWan3Status(),
                reporter.getWan3Uptime(),
                reporter.getWan4Ip(), 
                reporter.getWan4Provider(), 
                reporter.getWan4Status(),
                reporter.getWan4Uptime(),
                reporter.getUptimeLb(),
                Upload,
                Download,
                countAPConnected,
                countAPDisconnected,
                reporter.getGroupClient()
            );
            reporterRepository.save(createSitename);
            return ResponseEntity.ok(reporterService.findAll());
        }
        return ResponseEntity.ok(reporterService.findAll());
    }

    @ApiOperation(value = "put information from all clients")
    @PutMapping("it4u/{sitename}/customer_info")
    public String PutInfoClient(@PathVariable(value = "sitename") String sitename, @RequestBody ReporterSummary updatingReporter, Locale locale) {
        JSONObject getCookies = new JSONObject(ResponseEntity.ok(configTokenService.findAll()));
        JSONArray getBody = getCookies.getJSONArray("body");
        JSONObject body = (JSONObject) getBody.get(0);
        String csrfToken = body.getString("csrfToken");
        String unifises = body.getString("unifises");
        ApiRequest apiRequest = new ApiRequest();
        long upload = 0;
        long download = 0;
        Integer countConn = 0;
        Integer countDisConn = 0;
        String siteNameTest = "";
        String idUser = "";
        JSONArray getSitename = new JSONArray(sitenameService.findAll());
        for (int i = 0; i < getSitename.length(); i++) {
            JSONObject getDataSitename = (JSONObject) getSitename.get(i);
            try {
                siteNameTest = getDataSitename.getString("sitename");
                if (siteNameTest.equals(sitename)) {
                    idUser = getDataSitename.getString("idname");
                    break;
                }
            } catch (Exception e) {
            }
        }
        try {
            String getData = apiRequest.getRequestApi(urlIt4u, "/s/" + idUser + "/stat/sta/", csrfToken, unifises);
            JSONObject jsonResult = new JSONObject(getData);
            JSONArray data = jsonResult.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject getInfo = (JSONObject) data.get(i);
                upload = upload + getInfo.getInt("rx_rate");
                download = download + getInfo.getInt("tx_rate");
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        Calculator getCalculator = new Calculator();
        double convertUploadToMb = getCalculator.convertBytesToMb(upload);
        String Upload = Double.toString(convertUploadToMb);
        double convertDownloadToMb = getCalculator.convertBytesToMb(download);
        String Download = Double.toString(convertDownloadToMb);
        // AP connected
        try {
            String getDataAP = apiRequest.getRequestApi(urlIt4u, "/s/" + idUser + "/stat/device/", csrfToken, unifises);
            JSONObject jsonResultAP = new JSONObject(getDataAP);
            JSONArray dataAP = jsonResultAP.getJSONArray("data");
            for (int i = 0; i < dataAP.length(); i++) {
                JSONObject getInfo = (JSONObject) dataAP.get(i);
                if (getInfo.getInt("state") == 1) {
                    countConn = countConn + 1;
                } else {
                    countDisConn = countDisConn + 1;
                }
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        String countAPConnected = Integer.toString(countConn);
        String countAPDisconnected = Integer.toString(countDisConn);
        JSONObject getDataPut = new JSONObject();
        getDataPut.put("upload", Upload);
        getDataPut.put("download", Download);
        getDataPut.put("APConnected", countAPConnected);
        getDataPut.put("APDisconnected", countAPDisconnected);
        reporterService.updateReporter(sitename, updatingReporter, getDataPut.toString());
        return "Updated Successfully";
    }
    
    @ApiOperation(value = "Get information from all clients")
    @DeleteMapping("it4u/{id}/customer_info")
    public ResponseEntity<?> deleteInfoClient(@PathVariable(value = "id") Long id, Locale locale) {
        reporterService.deleteReporter(id);
        return ResponseEntity.ok(reporterService.findAll()); 
    }

    @ApiOperation(value = "Post group client")
    @PostMapping("it4u/group")
    public ResponseEntity<?> postGroupsClient(@RequestBody final GroupClient bodyData) {
        groupClientRepository.save(bodyData);
        return ResponseEntity.ok(groupClientService.findAll());
    }

    @ApiOperation(value = "Get all group")
    @GetMapping("it4u/group")
    public ResponseEntity<?> getGroupsClient() {
        return ResponseEntity.ok(groupClientService.findAll());
    }

    @ApiOperation(value = "Get a group client")
    @DeleteMapping("it4u/{id}/group")
    public ResponseEntity<?> getGroupClient(@PathVariable(value = "id") Long id) {
        groupClientService.deleteGroupClient(id);
        return ResponseEntity.ok(groupClientService.findAll());
    }

    
}
package vn.tpsc.it4u.controller;

import vn.tpsc.it4u.util.ApiResponseUtils;
import vn.tpsc.it4u.util.*;
import vn.tpsc.it4u.controller.DashboardController;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import vn.tpsc.it4u.model.ConfigMail;
import vn.tpsc.it4u.payload.ConfigMailSummary;
import vn.tpsc.it4u.service.ConfigMailService;
import vn.tpsc.it4u.repository.ConfigMailRepository;

@RestController
@RequestMapping("${app.api.version}")
public class MailController {
    @Value("${app.ubnt.url}")
    private String urlIt4u;

    @Value("${app.ubnt.csrf_token}")
    private String csrfToken;

    @Value("${app.ubnt.unifises}")
    private String unifises;

    @Autowired
    ConfigMailRepository configMailRepository;

    @Autowired
    ConfigMailService configMailService;

    String printUrlVoucher="https://ubnt.cloud.tpsc.vn/print/hotspot/vouchers/s";
    @Autowired
    ApiResponseUtils apiResponse;

    @ApiOperation(value = "Create config mail")
    @PostMapping("it4u/configMail")
    public String postConfigMail(@RequestBody final ConfigMail configMail) {
        String result = "Created Succesfully";
        try {
            final ConfigMail createCookie = new ConfigMail(
                    configMail.getServiceMail(),
                    configMail.getHostMail(),
                    configMail.getPortMail(),
                    configMail.getMaxMessages(),
                    configMail.getRateDelta(),
                    configMail.getRateLimit(),
                    configMail.getUsernameMail(),
                    configMail.getPasswordMail(),
                    configMail.getCcMail(),
                    configMail.getSubjectMail(),
                    configMail.getTextMail(),
                    configMail.getCronjobMail()
            );
            configMailRepository.save(createCookie);
            return result;
        } catch (Exception e) {
            return e.toString();
        }
    }

    @ApiOperation(value = "Get config mail")
    @GetMapping("it4u/configMail")
    public String getConfigMail() {
        JSONArray getConfigMail = new JSONArray(configMailService.findAll());
        JSONObject getData = (JSONObject) getConfigMail.get(0);
        return getData.toString();
        
    }
    
    @ApiOperation(value = "Put config mail")
    @PutMapping("it4u/configMail")
    public ResponseEntity<?> putConfigMail(@RequestBody ConfigMailSummary updatingMail, Locale locale) {
        configMailService.updateMail(updatingMail);
        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }
}
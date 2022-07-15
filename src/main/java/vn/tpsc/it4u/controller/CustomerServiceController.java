package vn.tpsc.it4u.controller;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.sql.Timestamp;

import javax.naming.spi.DirStateFactory.Result;

import com.google.api.client.util.DateTime;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.tpsc.it4u.model.Contract;
import vn.tpsc.it4u.model.CustomerBreakdowns;
import vn.tpsc.it4u.model.CustomerSupport;
import vn.tpsc.it4u.model.HandleService;
import vn.tpsc.it4u.model.ServiceDeployment;
import vn.tpsc.it4u.payload.CustomerBreakdownsSummary;
import vn.tpsc.it4u.repository.ContractRepository;
import vn.tpsc.it4u.repository.CustomerBreakdownsRepository;
import vn.tpsc.it4u.repository.CustomerSupportRepository;
import vn.tpsc.it4u.repository.HandleServiceRepository;
import vn.tpsc.it4u.repository.ServiceDeploymentRepository;
import vn.tpsc.it4u.service.CustomerService;
import vn.tpsc.it4u.util.Calculator;

@RestController
@Slf4j
@RequestMapping("${app.api.version}")
public class CustomerServiceController {

    @Autowired
    CustomerBreakdownsRepository customerBreakdownsRepository;

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    ServiceDeploymentRepository serviceDeploymentRepository;

    @Autowired
    CustomerSupportRepository customerSupportRepository;

    @Autowired
    HandleServiceRepository handleServiceRepository;

    @Autowired
    CustomerService customerService;

    @Operation(description = "Create a customer breakdowns")
    @PostMapping("/it4u/customer/breakdowns")
    public Boolean createBreakdowns(@RequestBody String data) {
        JSONObject getData = new JSONObject(data);
        CustomerBreakdowns createBreakdowns = new CustomerBreakdowns(
                getData.getString("error"),
                getData.getString("receiver"),
                getData.getLong("firstTime"),
                getData.getLong("secondTime"),
                getData.getLong("duration"),
                getData.getString("influenceLevel"),
                getData.getString("reason"),
                getData.getString("result"),
                getData.getString("verify"),
                getData.getString("status"),
                getData.getString("classify"));
        if (contractRepository.existsByCustomId(getData.getString("customId"))) {
            Contract getContract = contractRepository.findByCustomId(getData.getString("customId"));
            createBreakdowns.setContract(getContract);
            customerBreakdownsRepository.save(createBreakdowns);
            return true;
        } else {
            Contract createContract = new Contract(
                    getData.getString("customId"),
                    getData.getString("numContract"),
                    getData.getString("clientName"),
                    getData.getString("servicePlans"),
                    getData.getString("street"),
                    null);
            contractRepository.save(createContract);
            createBreakdowns.setContract(createContract);
            customerBreakdownsRepository.save(createBreakdowns);
            return true;
        }
    }

    @Operation(description = "List all customer breakdowns")
    @GetMapping("/it4u/customer/breakdowns.all")
    public ResponseEntity<?> listAllBreakdowns() {
        return ResponseEntity.ok(customerService.findAllBreakdowns());
    }

    @Operation(description = "List all customer breakdowns from time to time")
    @PostMapping("/it4u/customer/breakdowns.time")
    public String listAllBreakdownsToTime(@RequestBody String data) {
        List<String> dataList = new ArrayList<>();
        JSONObject getData = new JSONObject(data);
        JSONArray getAllBreakdowns = new JSONArray(customerService.findAllBreakdowns());
        for (int i = 0; i < getAllBreakdowns.length(); i++) {
            JSONObject getItem = (JSONObject) getAllBreakdowns.get(i);
            if (getData.getLong("firstTime") <= getItem.getLong("firstTime"))
                if (getData.getLong("secondTime") >= getItem.getLong("firstTime"))
                    dataList.add(getItem.toString());
        }
        return dataList.toString();
    }

    @Operation(description = "Create a service deployment")
    @PostMapping("/it4u/service/deployment")
    public boolean createServiceDeployment(@RequestBody String data) {
        JSONObject getData = new JSONObject(data);
        final ServiceDeployment createServiceDeployment = new ServiceDeployment(
                getData.getString("receiver"),
                getData.getLong("firstTime"),
                getData.getLong("secondTime"),
                getData.getLong("duration"),
                getData.getString("result"),
                getData.getString("completionRate"),
                getData.getString("verify"));
        if (contractRepository.existsByCustomId(getData.getString("customId"))) {
            Contract getContract = contractRepository.findByCustomId(getData.getString("customId"));
            createServiceDeployment.setContract(getContract);
            serviceDeploymentRepository.save(createServiceDeployment);
            return true;
        } else {
            Contract createContract = new Contract(
                    getData.getString("customId"),
                    getData.getString("numContract"),
                    getData.getString("clientName"),
                    getData.getString("servicePlans"),
                    getData.getString("street"),
                    null);
            contractRepository.save(createContract);
            createServiceDeployment.setContract(createContract);
            serviceDeploymentRepository.save(createServiceDeployment);
            return true;
        }
    }

    @Operation(description = "List all service deployment")
    @GetMapping("/it4u/service/deployment.all")
    public ResponseEntity<?> listAllServiceDeployment() {
        return ResponseEntity.ok(customerService.findAllServiceDeployment());
    }

    @Operation(description = "List all service deployment from time to time")
    @PostMapping("/it4u/service/deployment.time")
    public String listAllServiceDeploymentToTime(@RequestBody String data) {
        List<String> dataList = new ArrayList<>();
        JSONObject getData = new JSONObject(data);
        JSONArray getAllServiceDeployment = new JSONArray(customerService.findAllServiceDeployment());
        for (int i = 0; i < getAllServiceDeployment.length(); i++) {
            JSONObject getItem = (JSONObject) getAllServiceDeployment.get(i);
            if (getData.getLong("firstTime") <= getItem.getLong("firstTime"))
                if (getData.getLong("secondTime") >= getItem.getLong("firstTime"))
                    dataList.add(getItem.toString());
        }
        return dataList.toString();
    }

    @Operation(description = "Create a customer support")
    @PostMapping("/it4u/customer/support")
    public boolean createCustomerSupport(@RequestBody String data) {
        JSONObject getData = new JSONObject(data);
        final CustomerSupport createCustomerSupport = new CustomerSupport(
                getData.getString("request"),
                getData.getString("receiver"),
                getData.getLong("firstTime"),
                getData.getLong("secondTime"),
                getData.getLong("duration"),
                getData.getString("result"),
                getData.getString("status"));
        if (contractRepository.existsByCustomId(getData.getString("customId"))) {
            Contract getContract = contractRepository.findByCustomId(getData.getString("customId"));
            createCustomerSupport.setContract(getContract);
            customerSupportRepository.save(createCustomerSupport);
            return true;
        } else {
            Contract createContract = new Contract(
                    getData.getString("customId"),
                    getData.getString("numContract"),
                    getData.getString("clientName"),
                    getData.getString("servicePlans"),
                    getData.getString("street"),
                    null);
            contractRepository.save(createContract);
            createCustomerSupport.setContract(createContract);
            customerSupportRepository.save(createCustomerSupport);
            return true;
        }
    }

    @Operation(description = "List all customer support")
    @GetMapping("/it4u/customer/support.all")
    public ResponseEntity<?> listAllCustomerSupport() {
        return ResponseEntity.ok(customerService.findAllCustomerSupport());
    }

    @Operation(description = "List all customer support from time to time")
    @PostMapping("/it4u/customer/support.time")
    public String listAllCustomerSupportToTime(@RequestBody String data) {
        List<String> dataList = new ArrayList<>();
        JSONObject getData = new JSONObject(data);
        JSONArray getAllCustomerSupport = new JSONArray(customerService.findAllCustomerSupport());
        for (int i = 0; i < getAllCustomerSupport.length(); i++) {
            JSONObject getItem = (JSONObject) getAllCustomerSupport.get(i);
            if (getData.getLong("firstTime") <= getItem.getLong("firstTime"))
                if (getData.getLong("secondTime") >= getItem.getLong("firstTime"))
                    dataList.add(getItem.toString());
        }
        return dataList.toString();
    }

    @Operation(description = "Create a handle service")
    @PostMapping("/it4u/handle/service")
    public boolean createHandleService(@RequestBody String data) {
        JSONObject getData = new JSONObject(data);
        final HandleService createHandleService = new HandleService(
                getData.getString("request"),
                getData.getString("receiver"),
                getData.getLong("firstTime"),
                getData.getLong("secondTime"),
                getData.getLong("duration"),
                getData.getString("result"),
                getData.getString("completionRate"),
                getData.getString("happyCall"));
        if (contractRepository.existsByCustomId(getData.getString("customId"))) {
            Contract getContract = contractRepository.findByCustomId(getData.getString("customId"));
            createHandleService.setContract(getContract);
            handleServiceRepository.save(createHandleService);
            return true;
        } else {
            Contract createContract = new Contract(
                    getData.getString("customId"),
                    getData.getString("numContract"),
                    getData.getString("clientName"),
                    getData.getString("servicePlans"),
                    getData.getString("street"),
                    null);
            contractRepository.save(createContract);
            createHandleService.setContract(createContract);
            handleServiceRepository.save(createHandleService);
            return true;
        }
    }

    @Operation(description = "List all handle services")
    @GetMapping("/it4u/handle/services.all")
    public ResponseEntity<?> listAllHandleServices() {
        return ResponseEntity.ok(customerService.findAllCustomerSupport());
    }

    @Operation(description = "List all handle services from time to time")
    @PostMapping("/it4u/handle/services.time")
    public String listAllHandleServicesToTime(@RequestBody String data) {
        List<String> dataList = new ArrayList<>();
        JSONObject getData = new JSONObject(data);
        JSONArray getAllHandleServices = new JSONArray(customerService.findAllHandleService());
        for (int i = 0; i < getAllHandleServices.length(); i++) {
            JSONObject getItem = (JSONObject) getAllHandleServices.get(i);
            if (getData.getLong("firstTime") <= getItem.getLong("firstTime"))
                if (getData.getLong("secondTime") >= getItem.getLong("firstTime"))
                    dataList.add(getItem.toString());
        }
        return dataList.toString();
    }

    @Operation(description = "Service statistics by month")
    @GetMapping("/it4u/service/sta/month")
    public String staByMonth() {
        String result = "";
        int year = Year.now().getValue();
        LocalDate now = LocalDate.now();
        Calculator getCalculator = new Calculator();
        for (int i = 1; i <= 12; i++) {
            int dayOfMonth = getCalculator.dayOfMonth(i);
            YearMonth yearMonth = YearMonth.of(year, i);
            LocalDate firstOfMonth = yearMonth.atDay(1);
            Timestamp timestamp = Timestamp.valueOf(firstOfMonth.toString() + " 00:00:00");
            Long startTime = timestamp.getTime();
            Long endTime = startTime + dayOfMonth * 24 * 60 * 60 * 1000;
            JSONArray getAllServiceDeployment = new JSONArray(customerService.findAllServiceDeployment());
            // System.out.println(timestamp.getTime());
        }
        return result;
    }
}

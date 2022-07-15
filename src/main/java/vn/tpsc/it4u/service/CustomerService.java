package vn.tpsc.it4u.service;

import vn.tpsc.it4u.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.model.Contract;
import vn.tpsc.it4u.model.CustomerBreakdowns;
import vn.tpsc.it4u.model.CustomerSupport;
import vn.tpsc.it4u.model.HandleService;
import vn.tpsc.it4u.model.ServiceDeployment;
import vn.tpsc.it4u.payload.CustomerBreakdownsSummary;
import vn.tpsc.it4u.payload.CustomerSupportSummary;
import vn.tpsc.it4u.payload.HandleServiceSummary;
import vn.tpsc.it4u.payload.ServiceDeploymentSummary;
import vn.tpsc.it4u.repository.ContractRepository;
import vn.tpsc.it4u.repository.CustomerBreakdownsRepository;
import vn.tpsc.it4u.repository.ServiceDeploymentRepository;
import vn.tpsc.it4u.repository.CustomerSupportRepository;
import vn.tpsc.it4u.repository.HandleServiceRepository;

@Service
@ExtensionMethod({StringUtils.class})
public class CustomerService {
    
    @Autowired
    private ModelMapper mapper;

    @Autowired
    CustomerBreakdownsRepository customerBreakdownsRepository;

    @Autowired
    ServiceDeploymentRepository serviceDeploymentRepository;

    @Autowired
    CustomerSupportRepository customerSupportRepository;

    @Autowired
    HandleServiceRepository handleServiceRepository;

    @Autowired
    ContractRepository contractRepository;

    public List<CustomerBreakdownsSummary> findAllBreakdowns() {
        List<CustomerBreakdowns> getBreakdowns = customerBreakdownsRepository.findAll();
        List<CustomerBreakdownsSummary> listBreakdowns = getBreakdowns.stream()
            .map(breakdowns -> new CustomerBreakdownsSummary(
                breakdowns.getId(),
                breakdowns.getContract(),
                breakdowns.getReceiver(),
                breakdowns.getFirstTime(),
                breakdowns.getSecondTime(),
                breakdowns.getInfluenceLevel(),
                breakdowns.getReason(),
                breakdowns.getResult(),
                breakdowns.getVerify(),
                breakdowns.getStatus(),
                breakdowns.getClassify()
            )).collect(Collectors.toList());
        return listBreakdowns;
    }

    public List<ServiceDeploymentSummary> findAllServiceDeployment() {
        List<ServiceDeployment> getServiceDeployment = serviceDeploymentRepository.findAll();
        List<ServiceDeploymentSummary> listServiceDeployment = getServiceDeployment.stream()
            .map(serviceDeployment -> new ServiceDeploymentSummary(
                serviceDeployment.getId(),
                serviceDeployment.getContract(),
                serviceDeployment.getReceiver(),
                serviceDeployment.getFirstTime(),
                serviceDeployment.getSecondTime(),
                serviceDeployment.getDuration(),
                serviceDeployment.getResult(),
                serviceDeployment.getCompletionRate(),
                serviceDeployment.getVerify()
            )).collect(Collectors.toList());
        return listServiceDeployment;
    }

    public List<CustomerSupportSummary> findAllCustomerSupport() {
        List<CustomerSupport> getCustomerSupport = customerSupportRepository.findAll();
        List<CustomerSupportSummary> listCustomerSupport = getCustomerSupport.stream()
            .map(customerSupport -> new CustomerSupportSummary(
                customerSupport.getId(),
                customerSupport.getContract(),
                customerSupport.getRequest(),
                customerSupport.getReceiver(),
                customerSupport.getFirstTime(),
                customerSupport.getSecondTime(),
                customerSupport.getDuration(),
                customerSupport.getResult(),
                customerSupport.getStatus()
            )).collect(Collectors.toList());
        return listCustomerSupport;
    }

    public List<HandleServiceSummary> findAllHandleService() {
        List<HandleService> getHandleService = handleServiceRepository.findAll();
        List<HandleServiceSummary> listHandleService = getHandleService.stream()
            .map(handleService -> new HandleServiceSummary(
                handleService.getId(),
                handleService.getContract(),
                handleService.getRequest(),
                handleService.getReceiver(),
                handleService.getFirstTime(),
                handleService.getSecondTime(),
                handleService.getDuration(),
                handleService.getResult(),
                handleService.getCompletionRate(),
                handleService.getHappyCall()
            )).collect(Collectors.toList());
        return listHandleService;
    }
}

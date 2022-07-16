package vn.tpsc.it4u.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.models.CustomerBreakdowns;
import vn.tpsc.it4u.models.CustomerSupport;
import vn.tpsc.it4u.models.HandleService;
import vn.tpsc.it4u.models.ServiceDeployment;
import vn.tpsc.it4u.payloads.CustomerBreakdownsSummary;
import vn.tpsc.it4u.payloads.CustomerSupportSummary;
import vn.tpsc.it4u.payloads.HandleServiceSummary;
import vn.tpsc.it4u.payloads.ServiceDeploymentSummary;
import vn.tpsc.it4u.repository.ContractRepository;
import vn.tpsc.it4u.repository.CustomerBreakdownsRepository;
import vn.tpsc.it4u.repository.CustomerSupportRepository;
import vn.tpsc.it4u.repository.HandleServiceRepository;
import vn.tpsc.it4u.repository.ServiceDeploymentRepository;
import vn.tpsc.it4u.utils.StringUtils;

@Service
@ExtensionMethod({ StringUtils.class })
public class CustomerService {

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
						breakdowns.getClassify()))
				.collect(Collectors.toList());
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
						serviceDeployment.getVerify()))
				.collect(Collectors.toList());
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
						customerSupport.getStatus()))
				.collect(Collectors.toList());
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
						handleService.getHappyCall()))
				.collect(Collectors.toList());
		return listHandleService;
	}
}

package vn.tpsc.it4u.service;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.repository.ChannelAttributeRepository;
import vn.tpsc.it4u.repository.ChannelDetailRepository;
import vn.tpsc.it4u.model.ChannelAttribute;
import vn.tpsc.it4u.model.ChannelDetail;
import vn.tpsc.it4u.payload.ChannelAttributeSummary;
import vn.tpsc.it4u.payload.ChannelDetailSummary;
import vn.tpsc.it4u.util.StringUtils;
import vn.tpsc.it4u.repository.ChannelValueRepository;
import vn.tpsc.it4u.repository.HistoryChannelRepository;
import vn.tpsc.it4u.repository.ContractRepository;
import vn.tpsc.it4u.model.ChannelValue;
import vn.tpsc.it4u.model.Contract;
import vn.tpsc.it4u.model.HistoryChannel;
/**
 * ChannelAttribute
 */
@Service
@ExtensionMethod({StringUtils.class})
public class ChannelInfoService {

    @Autowired
    private ChannelAttributeRepository channelAttributeRepository;

    @Autowired
    private ChannelDetailRepository channelDetailRepository;

    @Autowired
    private ChannelValueRepository channelValueRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private HistoryChannelRepository historyChannelRepository;

    @Autowired
    private ModelMapper mapper;

    public List<ChannelAttributeSummary> findAll() {
        List<ChannelAttribute> channelAttributes = channelAttributeRepository.findAll();
        List<ChannelAttributeSummary> listChannelAttributes = channelAttributes.stream()
            .map(channel -> new ChannelAttributeSummary(
                channel.getId(),
                channel.getCustomer(),
                channel.getChannelValue(),
                channel.getStatus(),
                channel.getVirtualNum(),
                channel.getUsernamePPPoE()
                ))
            .collect(Collectors.toList());
        return listChannelAttributes;
    }

     public List<ChannelAttributeSummary> findChannelAttributeByStatus(String status) {
        List<ChannelAttribute> channelAttributes = channelAttributeRepository.findByStatus(status);
        List<ChannelAttributeSummary> listChannelAttributes = channelAttributes.stream()
            .map(channel -> new ChannelAttributeSummary(
                channel.getId(),
                channel.getCustomer(),
                channel.getChannelValue(),
                channel.getStatus(),
                channel.getVirtualNum(),
                channel.getUsernamePPPoE()
                ))
            .collect(Collectors.toList());
        return listChannelAttributes;
    }

    public List<ChannelAttributeSummary> findChannelValue(String servicePack) {
        ChannelValue channelValue = channelValueRepository.findByServicePack(servicePack);
        Set<ChannelValue> channelValues = new HashSet<>();
        channelValues.add(channelValue);
        List<ChannelAttribute> channelAttribute = channelAttributeRepository.findByChannelValue(channelValue);
        List<ChannelAttributeSummary> listChannelAttributes = channelAttribute.stream()
            .map(channel -> new ChannelAttributeSummary(
                channel.getId(),
                channel.getCustomer(),
                channel.getChannelValue(), 
                channel.getStatus(),
                channel.getVirtualNum(),
                channel.getUsernamePPPoE()
                ))
            .collect(Collectors.toList());
        return listChannelAttributes;
    }

    public List<ChannelDetailSummary> findAllChannelDetail() {
        List<ChannelDetail> channelDetails = channelDetailRepository.findAll();
        List<ChannelDetailSummary> listChannelDetails = channelDetails.stream()
            .map(channelDetail -> new ChannelDetailSummary(
               channelDetail.getId(),
               channelDetail.getContract(),
               channelDetail.getChannelAttribute(),
               channelDetail.getRouterType(),
               channelDetail.getVotesRequire(),
               channelDetail.getIpType(),
               channelDetail.getDeviceStatus(),
               channelDetail.getIpAddress(),
               channelDetail.getRegionalEngineer(),
               channelDetail.getDeployRequestDate(),
               channelDetail.getDateAcceptance(),
               channelDetail.getFees()
            )).collect(Collectors.toList());
        return listChannelDetails;
    }

    public List<ChannelAttribute> findChannelAttributeByDate(Timestamp fromDate, Timestamp toDate) {
        List<ChannelAttribute> listChannelAttribute = channelAttributeRepository.findChannelAttributes(fromDate, toDate);
        return listChannelAttribute;
    }

    public Contract findContractByCustomId(String customId) {
        Contract getContract = contractRepository.findByCustomId(customId);
        return getContract;
    }

    public Contract findContractByClientName(String clientName) {
        Contract getContract = contractRepository.findByClientName(clientName);
        return getContract;
    }

    public boolean deleteChannelDetail(long id) {
        channelDetailRepository.deleteById(id);
        return true;
    }

    public boolean updateInfoChannelDetail(long channelDetailId, JSONObject updatingChannelDetail) {
        ChannelDetail channelDetail = channelDetailRepository.findById(channelDetailId);
        channelDetail.setRouterType(updatingChannelDetail.getString("routerType").isNullorEmpty() ? channelDetail.getRouterType() : updatingChannelDetail.getString("routerType"));
        channelDetail.setDeviceStatus(updatingChannelDetail.getString("deviceStatus").isNullorEmpty() ? channelDetail.getDeviceStatus() : updatingChannelDetail.getString("deviceStatus"));
        channelDetail.setVotesRequire(updatingChannelDetail.getString("votesRequire"));
        channelDetail.setIpType(updatingChannelDetail.getString("ipType"));
        channelDetail.setRegionalEngineer(updatingChannelDetail.getString("regionalEngineer"));
        channelDetail.setIpAddress(updatingChannelDetail.getString("ipAddress"));
        channelDetail.setFees(updatingChannelDetail.getString("fees"));
        channelDetail.setDeployRequestDate(updatingChannelDetail.getLong("deployRequestDate"));
        channelDetail.setDateAcceptance(updatingChannelDetail.getLong("dateAcceptance"));
        JSONObject getChannelAttribute = new JSONObject(channelDetail.getChannelAttribute());
        ChannelAttribute channelAttribute = channelAttributeRepository.findById(getChannelAttribute.getLong("id"));
        channelAttribute.setCustomer(updatingChannelDetail.getString("customer"));
        channelAttribute.setStatus(updatingChannelDetail.getString("status"));
        channelAttribute.setVirtualNum(updatingChannelDetail.getString("virtualNum"));
        channelAttribute.setUsernamePPPoE(updatingChannelDetail.getString("usernamePPPoE"));
        ChannelValue channelValue = channelValueRepository.findByServicePack(updatingChannelDetail.getString("servicePack"));
        channelAttribute.setChannelValue(channelValue);
        channelAttributeRepository.save(channelAttribute);
        if (contractRepository.existsByCustomId(updatingChannelDetail.getString("customId"))) {
            Contract getContract = contractRepository.findByCustomId(updatingChannelDetail.getString("customId"));
            channelDetail.setContract(getContract);
            channelDetailRepository.save(channelDetail);
            return true;
        } else {
            Contract createContract = new Contract(
                updatingChannelDetail.getString("customId"), 
                updatingChannelDetail.getString("numContract"),
                updatingChannelDetail.getString("clientName"), 
                updatingChannelDetail.getString("servicePlans"),
                updatingChannelDetail.getString("street")
            );
            contractRepository.save(createContract);
            channelDetail.setContract(createContract);
            channelDetailRepository.save(channelDetail);
            return true;
        }
    }

    public Boolean updateChannelAttribute(long channelId, JSONObject data) {
        ChannelAttribute channelAttribute = channelAttributeRepository.findById(channelId);
        channelAttribute.setCustomer(data.getString("customer"));
        channelAttribute.setStatus(data.getString("status"));
        channelAttribute.setUsernamePPPoE(data.getString("usernamePPPoE"));
        channelAttribute.setVirtualNum(data.getString("virtualNum"));
        ChannelValue channelValue = channelValueRepository.findByServicePack(data.getString("channelValue"));
        channelAttribute.setChannelValue(channelValue);
        channelAttributeRepository.save(channelAttribute);
        return true;
    }

    public Boolean deleteChannelAttribute(Long channelId) {
        channelAttributeRepository.deleteById(channelId);
        return true;
    }
}

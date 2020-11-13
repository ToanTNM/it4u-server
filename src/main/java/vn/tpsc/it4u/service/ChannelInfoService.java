package vn.tpsc.it4u.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import vn.tpsc.it4u.model.ChannelValue;
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

    public List<ChannelAttributeSummary> findAll() {
        List<ChannelAttribute> channelAttributes = channelAttributeRepository.findAll();
        List<ChannelAttributeSummary> listChannelAttributes = channelAttributes.stream()
            .map(channel -> new ChannelAttributeSummary(
                channel.getId(),
                channel.getCustomer(),
                channel.getChannelValue(),
                channel.getStatus()))
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
                channel.getStatus()))
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
               channelDetail.getCustomerMove(),
               channelDetail.getVotesRequire(),
               channelDetail.getIpType(),
               channelDetail.getVirtualNum(),
               channelDetail.getDeviceStatus(),
               channelDetail.getIpAddress(),
               channelDetail.getRegionalEngineer(),
               channelDetail.getDeployRequestDate(),
               channelDetail.getDateAcceptance(),
               channelDetail.getDateRequestStop(),
               channelDetail.getDateStop(),
               channelDetail.getDateOnlineRequest(),
               channelDetail.getDateOnline(),
               channelDetail.getFees()
            )).collect(Collectors.toList());
        return listChannelDetails;
    }
}

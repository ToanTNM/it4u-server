package vn.tpsc.it4u.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.channels.Channel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.repository.ChannelAttributeRepository;
import vn.tpsc.it4u.model.ChannelAttribute;
import vn.tpsc.it4u.payload.ChannelAttributeSummary;
import vn.tpsc.it4u.util.StringUtils;
import vn.tpsc.it4u.repository.ChannelNameRespository;
import vn.tpsc.it4u.model.ChannelName;
/**
 * ChannelAttribute
 */
@Service
@ExtensionMethod({StringUtils.class})
public class ChannelAttributeService {

    @Autowired
    private ChannelAttributeRepository channelAttributeRepository;

    @Autowired
    private ChannelNameRespository channelNameRespository;

    public List<ChannelAttributeSummary> findAll() {
        List<ChannelAttribute> channelAttributes = channelAttributeRepository.findAll();
        List<ChannelAttributeSummary> listChannelAttributes = channelAttributes.stream()
            .map(channel -> new ChannelAttributeSummary(
                channel.getId(), 
                channel.getChannelName(),
                channel.getChannelValue(),
                channel.getStatus()))
            .collect(Collectors.toList());
        return listChannelAttributes;
    }

    public List<ChannelAttributeSummary> findChannelName(String name) {
        ChannelName channelName = channelNameRespository.findByName(name);
        Set<ChannelName> channelNames = new HashSet<>();
        channelNames.add(channelName);
        List<ChannelAttribute> channelAttribute = channelAttributeRepository.findByChannelName(channelName);
        List<ChannelAttributeSummary> listChannelAttributes = channelAttribute.stream()
            .map(channel -> new ChannelAttributeSummary(
                channel.getId(), 
                channel.getChannelName(), 
                channel.getChannelValue(), 
                channel.getStatus()))
            .collect(Collectors.toList());
        return listChannelAttributes;
    }
}

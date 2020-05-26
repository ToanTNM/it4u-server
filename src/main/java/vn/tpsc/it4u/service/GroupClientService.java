package vn.tpsc.it4u.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.model.GroupClient;
import vn.tpsc.it4u.payload.GroupClientSummary;
import vn.tpsc.it4u.util.StringUtils;
import vn.tpsc.it4u.repository.GroupClientRepository;

/**
 * GroupClientService
 */
@Service
@ExtensionMethod({ StringUtils.class })
public class GroupClientService {

    @Autowired
    private GroupClientRepository groupClientRepository;

    public List<GroupClientSummary> findAll() {
        List<GroupClient> groupName = groupClientRepository.findAll();

        List<GroupClientSummary> listGroups = groupName.stream()
                .map(user -> new GroupClientSummary(
                    user.getId(), 
                    user.getGroupName()))
                .collect(Collectors.toList());

        return listGroups;
    }

    public List<GroupClientSummary> findById(List<Long> id) {
        List<GroupClient> groupClient = groupClientRepository.findByIdIn(id);
        List<GroupClientSummary> listGroups = groupClient.stream()
                .map(user -> new GroupClientSummary(
                    user.getId(), 
                    user.getGroupName()))
                .collect(Collectors.toList());
        return listGroups;
    }

    public Boolean deleteGroupClient(Long userId) {
        groupClientRepository.deleteById(userId);
        return true;
    }
}
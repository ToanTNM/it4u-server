package vn.tpsc.it4u.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.model.SitesName;
import vn.tpsc.it4u.payload.SitesNameSummary;
import vn.tpsc.it4u.util.StringUtils;
import vn.tpsc.it4u.repository.SitesNameRepository;

/**
 * SitesNameService
 */
@Service
@ExtensionMethod({ StringUtils.class })
public class SitesNameService {

    @Autowired
    private SitesNameRepository sitesNameRepository;

    public List<SitesNameSummary> findAll() {
        List<SitesName> sitesNames = sitesNameRepository.findAll();

        List<SitesNameSummary> listSitesName = sitesNames.stream()
                .map(user -> new SitesNameSummary(user.getId(), user.getSitename(), user.getIdname()))
                .collect(Collectors.toList());

        return listSitesName;
    }

    // public addSiteForUser() {
    // }

    public Boolean deleteAll() {
        sitesNameRepository.deleteAll();
        return true;
    }
}
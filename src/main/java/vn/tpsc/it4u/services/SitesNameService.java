package vn.tpsc.it4u.services;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.models.SitesName;
import vn.tpsc.it4u.payloads.SitesNameSummary;
import vn.tpsc.it4u.repository.SitesNameRepository;
import vn.tpsc.it4u.utils.StringUtils;

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

    public SitesName findById(long id) {
        SitesName sitesName = sitesNameRepository.findById(id);
        return sitesName;
    }

    public Boolean deleteAll() {
        sitesNameRepository.deleteAll();
        return true;
    }

    public Boolean deleteById(long id) {
        sitesNameRepository.deleteById(id);
        return true;
    }

    public Boolean createSitename(JSONObject data) {
        SitesName sitesName = new SitesName(
                data.getString("sitename"),
                data.getString("idname"));
        sitesNameRepository.save(sitesName);
        return true;
    }

    public Boolean updateSitename(long id, JSONObject data) {
        SitesName sitename = sitesNameRepository.findById(id);
        sitename.setIdname(data.getString("idname"));
        sitename.setSitename(data.getString("sitename"));
        sitesNameRepository.save(sitename);
        return true;
    }
}
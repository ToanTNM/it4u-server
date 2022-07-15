package vn.tpsc.it4u.service;

import java.util.List;

import com.google.auth.oauth2.IdTokenProvider.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antlr.StringUtils;
import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.model.SystemConfig;
import vn.tpsc.it4u.payload.SystemConfigSummary;
import vn.tpsc.it4u.repository.SystemConfigRepository;

@Service
@ExtensionMethod({StringUtils.class})
public class SystemConfigService {
    @Autowired
    private SystemConfigRepository systemConfigRepository;

    public boolean updateSystemConfig(SystemConfigSummary data) {
        List<SystemConfig> listSystemConfig = systemConfigRepository.findAll();
        SystemConfig systemConfig = listSystemConfig.get(0);
        systemConfig.setUsernameUbnt(data.getUsernameUbnt());
        systemConfig.setPwUbnt(data.getPwUbnt());
        systemConfig.setTokenDev(data.getTokenDev());
        systemConfig.setTokenUcrm(data.getTokenUcrm());
        systemConfigRepository.save(systemConfig);
        return true;
    }

    public boolean createSystemConfig(SystemConfigSummary data) {
        SystemConfig systemConfig = new SystemConfig(
            data.getUsernameUbnt(),
            data.getPwUbnt(),
            data.getTokenDev(),
            data.getTokenUcrm()
        );
        systemConfigRepository.save(systemConfig);
        return true;
    }

    public SystemConfig findSystemConfig() {
        List<SystemConfig> listSystemConfig = systemConfigRepository.findAll();
        SystemConfig systemConfig = listSystemConfig.get(0);
        return systemConfig;
    }


}

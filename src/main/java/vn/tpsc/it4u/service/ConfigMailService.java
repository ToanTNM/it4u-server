package vn.tpsc.it4u.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.model.ConfigMail;
import vn.tpsc.it4u.payload.ConfigMailSummary;
import vn.tpsc.it4u.util.StringUtils;
import vn.tpsc.it4u.repository.ConfigMailRepository;

/**
 * ConfigMailService
 */
@Service
@ExtensionMethod({ StringUtils.class })
public class ConfigMailService {

    @Autowired
    private ConfigMailRepository configMailRepository;

    @Autowired
    private ModelMapper mapper;
    
    public List<ConfigMailSummary> findAll() {
        List<ConfigMail> configMail = configMailRepository.findAll();

        List<ConfigMailSummary> listConfigMail = configMail.stream()
                .map(mail -> new ConfigMailSummary(
                    mail.getId(),
                    mail.getServiceMail(),
                    mail.getHostMail(), 
                    mail.getPortMail(), 
                    mail.getMaxMessages(), 
                    mail.getRateDelta(), 
                    mail.getRateLimit(), 
                    mail.getUsernameMail(), 
                    mail.getPasswordMail(), 
                    mail.getCcMail(),
                    mail.getSubjectMail(), 
                    mail.getTextMail(),
                    mail.getCronjobMail()))
                .collect(Collectors.toList());

        return listConfigMail;
    }

    public boolean updateMail(ConfigMailSummary updateMail) {
        List<ConfigMail> configMails = configMailRepository.findAll();
        ConfigMail mail = mapper.map(configMails.get(0),ConfigMail.class);
        mail.setServiceMail(updateMail.getServiceMail().isNullorEmpty() ? updateMail.getServiceMail() : mail.getServiceMail());
        mail.setHostMail(updateMail.getHostMail().isNullorEmpty() ? updateMail.getHostMail() : mail.getHostMail());
        mail.setPortMail(updateMail.getPortMail().isNullorEmpty() ? updateMail.getPortMail() : mail.getPortMail());
        mail.setMaxMessages(updateMail.getMaxMessages().isNullorEmpty() ? updateMail.getMaxMessages() : mail.getMaxMessages());
        mail.setRateDelta(updateMail.getRateDelta().isNullorEmpty() ? updateMail.getRateDelta() : mail.getRateDelta());
        mail.setRateLimit(updateMail.getRateLimit().isNullorEmpty() ? updateMail.getRateLimit() : mail.getRateLimit());
        mail.setUsernameMail(updateMail.getUsernameMail().isNullorEmpty() ? updateMail.getUsernameMail() : mail.getUsernameMail());
        mail.setPasswordMail(updateMail.getPasswordMail().isNullorEmpty() ? updateMail.getPasswordMail() : mail.getPasswordMail());
        mail.setCcMail(updateMail.getCcMail().isNullorEmpty()  ? updateMail.getCcMail() : mail.getCcMail());
        mail.setSubjectMail(updateMail.getSubjectMail().isNullorEmpty() ? updateMail.getSubjectMail() : mail.getSubjectMail());
        mail.setTextMail(updateMail.getTextMail() != "" ? updateMail.getTextMail() : updateMail.getTextMail());
        mail.setCronjobMail(updateMail.getCronjobMail().isNullorEmpty() ? updateMail.getCronjobMail() : mail.getCronjobMail());
        configMailRepository.save(mail);
        return true;
    }
    
}
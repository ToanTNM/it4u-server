package vn.tpsc.it4u.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.models.ConfigMail;
import vn.tpsc.it4u.payloads.ConfigMailSummary;
import vn.tpsc.it4u.repository.ConfigMailRepository;
import vn.tpsc.it4u.utils.StringUtils;

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
		ConfigMail mail = mapper.map(configMails.get(0), ConfigMail.class);
		mail.setServiceMail(
				updateMail.getServiceMail().isNullorEmpty() ? mail.getServiceMail()
						: updateMail.getServiceMail());
		mail.setHostMail(updateMail.getHostMail().isNullorEmpty() ? mail.getHostMail()
				: updateMail.getHostMail());
		mail.setPortMail(updateMail.getPortMail().isNullorEmpty() ? mail.getPortMail()
				: updateMail.getPortMail());
		mail.setMaxMessages(
				updateMail.getMaxMessages().isNullorEmpty() ? mail.getMaxMessages()
						: updateMail.getMaxMessages());
		mail.setRateDelta(updateMail.getRateDelta().isNullorEmpty() ? mail.getRateDelta()
				: updateMail.getRateDelta());
		mail.setRateLimit(updateMail.getRateLimit().isNullorEmpty() ? mail.getRateLimit()
				: updateMail.getRateLimit());
		mail.setUsernameMail(
				updateMail.getUsernameMail().isNullorEmpty() ? mail.getUsernameMail()
						: updateMail.getUsernameMail());
		mail.setPasswordMail(
				updateMail.getPasswordMail().isNullorEmpty() ? mail.getPasswordMail()
						: updateMail.getPasswordMail());
		mail.setCcMail(updateMail.getCcMail().isNullorEmpty() ? mail.getCcMail() : updateMail.getCcMail());
		mail.setSubjectMail(
				updateMail.getSubjectMail().isNullorEmpty() ? mail.getSubjectMail()
						: updateMail.getSubjectMail());
		mail.setTextMail(updateMail.getTextMail().isNullorEmpty() ? mail.getTextMail()
				: updateMail.getTextMail());
		mail.setCronjobMail(
				updateMail.getCronjobMail().isNullorEmpty() ? mail.getCronjobMail()
						: updateMail.getCronjobMail());
		configMailRepository.save(mail);
		return true;
	}

}
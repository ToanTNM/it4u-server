package vn.tpsc.it4u.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.models.ConfigToken;
import vn.tpsc.it4u.payloads.ConfigTokenSummary;
import vn.tpsc.it4u.repository.ConfigTokenRepository;
import vn.tpsc.it4u.utils.StringUtils;

/**
 * ConfigTokenService
 */
@Service
@ExtensionMethod({ StringUtils.class })
public class ConfigTokenService {

	@Autowired
	private ConfigTokenRepository configTokenRepository;

	@Autowired
	private ModelMapper mapper;

	public List<ConfigTokenSummary> findAll() {
		List<ConfigToken> configToken = configTokenRepository.findAll();

		List<ConfigTokenSummary> listConfigToken = configToken.stream()
				.map(token -> new ConfigTokenSummary(
						token.getId(),
						token.getCsrfToken(),
						token.getUnifises()))
				.collect(Collectors.toList());
		return listConfigToken;
	}

	public boolean updateCookies(String csrfToken, String unifises) {
		List<ConfigToken> configTokens = configTokenRepository.findAll();
		ConfigToken configToken = mapper.map(configTokens.get(0), ConfigToken.class);
		configToken.setCsrfToken(csrfToken);
		configToken.setUnifises(unifises);
		configTokenRepository.save(configToken);
		return true;
	}
}
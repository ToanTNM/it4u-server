package vn.tpsc.it4u.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.modelmapper.ModelMapper;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.models.User;
import vn.tpsc.it4u.models.token.RefreshToken;
import vn.tpsc.it4u.repository.UserRepository;
import vn.tpsc.it4u.security.CustomUserDetails;
import vn.tpsc.it4u.services.RefreshTokenService;
import vn.tpsc.it4u.utils.StringUtils;

@Service
@ExtensionMethod({ StringUtils.class })
public class AuthService {
	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper mapper;

	public Optional<User> createAndPersistRefreshTokenForDevice(Authentication authentication) {
		CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
		User user = mapper.map(currentUser, User.class);
		String refreshToken = refreshTokenService.createRefreshToken();
		user.setRefreshToken(refreshToken);
		user = userRepository.save(user);
		return Optional.ofNullable(user);
	}
}
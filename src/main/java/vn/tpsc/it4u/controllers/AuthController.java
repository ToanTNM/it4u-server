package vn.tpsc.it4u.controllers;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import vn.tpsc.it4u.enums.RoleName;
import vn.tpsc.it4u.exceptions.AppException;
import vn.tpsc.it4u.exceptions.TokenRefreshException;
import vn.tpsc.it4u.models.SitesName;
import vn.tpsc.it4u.models.auth.RefreshToken;
import vn.tpsc.it4u.models.auth.Role;
import vn.tpsc.it4u.models.auth.User;
import vn.tpsc.it4u.payloads.auth.JwtAuthenticationResponse;
import vn.tpsc.it4u.payloads.auth.LoginRequest;
import vn.tpsc.it4u.payloads.auth.SignUpRequest;
import vn.tpsc.it4u.payloads.auth.TokenRefreshRequest;
import vn.tpsc.it4u.repository.RoleRepository;
import vn.tpsc.it4u.repository.SitesNameRepository;
import vn.tpsc.it4u.repository.UserRepository;
import vn.tpsc.it4u.security.CustomUserDetails;
import vn.tpsc.it4u.security.JwtTokenProvider;
import vn.tpsc.it4u.services.RefreshTokenService;
import vn.tpsc.it4u.services.UserService;
import vn.tpsc.it4u.utils.ApiResponseUtils;

/**
 * AuthController
 */
@RestController
@RequestMapping("${app.api.version}/auth")
public class AuthController {

	@Autowired
	ModelMapper mapper;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	SitesNameRepository sitenamesRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	ApiResponseUtils apiResponse;

	@Autowired
	UserService userService;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Value("${app.api.version}")
	String apiVersion;

	@PostMapping("/signin")
	@Operation(description = "Sign In App")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "1000", description = "Successfully retrieved list"),
			@ApiResponse(responseCode = "401", description = "Bad credentials")
	})
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody final LoginRequest loginRequest, Locale locale) {
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(
						loginRequest.getUsernameOrEmail().toLowerCase(),
						loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

		final String accessToken = tokenProvider.generateTokenFromUserId(customUserDetails.getId());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(customUserDetails.getId());

		return ResponseEntity.ok(apiResponse.success(new JwtAuthenticationResponse(
				accessToken, refreshToken.getToken()), locale));
	}

	@PostMapping("/refresh")
	@Operation(description = "Refesh the expired jwt authentication")
	public ResponseEntity<?> refeshJwtToken(
			@Parameter(description = "The TokenRefreshRequest payload") @Valid @RequestBody TokenRefreshRequest request,
			Locale locale) {
		// Optional<User> user =
		// userService.getRefreshToken(tokenRefreshRequest.getRefreshToken());
		// Long userId = user.get().getId();
		// final String updatedToken = tokenProvider.generateTokenFromUserId(userId);
		// return ResponseEntity.ok(new JwtAuthenticationResponse(updatedToken,
		// tokenRefreshRequest.getRefreshToken()));

		String requestRefreshToken = request.getRefreshToken();

		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser)
				.map(user -> {
					final String accessToken = tokenProvider.generateTokenFromUserId(user.getId());
					return ResponseEntity.ok(apiResponse.success(new JwtAuthenticationResponse(
							accessToken, requestRefreshToken), locale));
				})
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
						"Refresh token is not exist!"));
	}

	@PostMapping("/signup")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody final SignUpRequest signUpRequest, Locale locale) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<>(apiResponse.error(1021, locale),
					HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<>(apiResponse.error(1002, locale), HttpStatus.BAD_REQUEST);
		}

		User user = mapper.map(signUpRequest, User.class);

		final String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		try {
			switch (signUpRequest.getRoles()) {
				case "ROLE_ADMIN":
					final Role roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN)
							.orElseThrow(() -> new AppException("User Role not set."));
					user.setRoles(Collections.singleton(roleAdmin));
					break;
				case "ROLE_USER":
					final Role roleUser = roleRepository.findByName(RoleName.ROLE_USER)
							.orElseThrow(() -> new AppException("User Role not set."));
					user.setRoles(Collections.singleton(roleUser));
					break;
				case "ROLE_MANAGER":
					final Role roleMG = roleRepository.findByName(RoleName.ROLE_MANAGER)
							.orElseThrow(() -> new AppException("User Role not set."));
					user.setRoles(Collections.singleton(roleMG));
					break;
				case "ROLE_CSKH":
					final Role userRoleCSKH = roleRepository.findByName(RoleName.ROLE_CSKH)
							.orElseThrow(() -> new AppException("User Role not set."));
					user.setRoles(Collections.singleton(userRoleCSKH));
					break;
				case "ROLE_LEAD_KT":
					final Role roleLeadKT = roleRepository.findByName(RoleName.ROLE_LEAD_KT)
							.orElseThrow(() -> new AppException("User Role not set."));
					user.setRoles(Collections.singleton(roleLeadKT));
					break;
				case "ROLE_KT":
					final Role roleKT = roleRepository.findByName(RoleName.ROLE_KT)
							.orElseThrow(() -> new AppException("User Role not set."));
					user.setRoles(Collections.singleton(roleKT));
					break;
				case "ROLE_LEAD_KD":
					final Role roleLeadKD = roleRepository.findByName(RoleName.ROLE_LEAD_KD)
							.orElseThrow(() -> new AppException("User Role not set."));
					user.setRoles(Collections.singleton(roleLeadKD));
					break;
				case "ROLE_KD":
					final Role roleKD = roleRepository.findByName(RoleName.ROLE_KD)
							.orElseThrow(() -> new AppException("User Role not set."));
					user.setRoles(Collections.singleton(roleKD));
					break;
				default:
					final Role roleKH = roleRepository.findByName(RoleName.ROLE_KH)
							.orElseThrow(() -> new AppException("User Role not set."));
					user.setRoles(Collections.singleton(roleKH));
					break;
			}
		} catch (Exception e) {
			final Role roleKH = roleRepository.findByName(RoleName.ROLE_KH)
					.orElseThrow(() -> new AppException("User Role not set."));
			user.setRoles(Collections.singleton(roleKH));
		}
		Set<String> strSites = signUpRequest.getSitename();
		Set<SitesName> sitenames = new HashSet<>();
		strSites.forEach(site -> {
			SitesName sitename = sitenamesRepository.findByIdname(site);
			sitenames.add(sitename);
		});
		user.setSitename(sitenames);
		final User result = userRepository.save(user);

		final URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path(apiVersion + "/users/{username}")
				.buildAndExpand(result.getUsername()).toUri();

		return ResponseEntity.created(location).body(apiResponse.success("User registered successfully"));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(HttpServletRequest request) {
		String jwt = tokenProvider.getJwtFromRequest(request);
		Long userId = tokenProvider.getUserIdFromJWT(jwt);
		refreshTokenService.deleteByUserId(userId);
		return ResponseEntity.ok(apiResponse.success("Log out successfully"));
	}
}
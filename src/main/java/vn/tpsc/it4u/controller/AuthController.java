package vn.tpsc.it4u.controller;

import java.net.URI;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
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
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import vn.tpsc.it4u.exception.AppException;
import vn.tpsc.it4u.model.Role;
import vn.tpsc.it4u.model.SitesName;
import vn.tpsc.it4u.model.enums.RoleName;
import vn.tpsc.it4u.model.enums.UserStatus;
import vn.tpsc.it4u.model.User;
import vn.tpsc.it4u.model.token.RefreshToken;
import vn.tpsc.it4u.payload.JwtAuthenticationResponse;
import vn.tpsc.it4u.payload.LoginRequest;
import vn.tpsc.it4u.payload.SignUpRequest;
import vn.tpsc.it4u.payload.TokenRefreshRequest;
import vn.tpsc.it4u.repository.RoleRepository;
import vn.tpsc.it4u.repository.SitesNameRepository;
import vn.tpsc.it4u.repository.UserRepository;
import vn.tpsc.it4u.security.JwtTokenProvider;
import vn.tpsc.it4u.service.AuthService;
import vn.tpsc.it4u.service.UserService;
import vn.tpsc.it4u.util.ApiResponseUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import vn.tpsc.it4u.exception.UserLoginException;

/**
 * AuthController
 */
@RestController
@Slf4j
@RequestMapping("${app.api.version}/auth")
public class AuthController {

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
	AuthService authService;

	@Autowired
	UserService userService;

	@PostMapping("/signin")
	@Operation(description = "Sign In App")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "1000", description = "Successfully retrieved list")
	})
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody final LoginRequest loginRequest, Locale locale) {

		final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				loginRequest.getUsernameOrEmail().toLowerCase(),
				loginRequest.getPassword());
		final Authentication authentication = authenticationManager.authenticate(token);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		log.info(loginRequest.getUsernameOrEmail() + " - Login");
		final String jwt = tokenProvider.generateToken(authentication);
		return authService.createAndPersistRefreshTokenForDevice(authentication)
				.map(User::getRefreshToken).map(refreshToken -> {
					return ResponseEntity.ok(
							new JwtAuthenticationResponse(jwt, refreshToken));
				}).orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + loginRequest + "]"));
		// return ResponseEntity.ok(apiResponse.success(new
		// JwtAuthenticationResponse(jwt, refreshToken), locale));
	}

	@PostMapping("/refresh")
	@Operation(description = "Refesh the expired jwt authentication")
	public ResponseEntity<?> refeshJwtToken(
			@Parameter(description = "The TokenRefreshRequest payload") @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest,
			Locale locale) {
		Optional<User> user = userService.getRefreshToken(tokenRefreshRequest.getRefreshToken());
		Long userId = user.get().getId();
		final String updatedToken = tokenProvider.generateTokenFromUserId(userId);
		return ResponseEntity.ok(new JwtAuthenticationResponse(updatedToken, tokenRefreshRequest.getRefreshToken()));
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

		// Creating user's account
		final User user = new User(
				signUpRequest.getName(),
				signUpRequest.getUsername(),
				signUpRequest.getEmail(),
				signUpRequest.getPassword(),
				signUpRequest.getGender(),
				signUpRequest.getType(),
				UserStatus.Active,
				signUpRequest.getLanguage(),
				null,
				null,
				null);

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
				.fromCurrentContextPath().path("/api/users/{username}")
				.buildAndExpand(result.getUsername()).toUri();

		return ResponseEntity.created(location).body(apiResponse.success("User registered successfully"));
	}
}
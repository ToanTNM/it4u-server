package vn.tpsc.it4u.controller;

import java.util.List;
import vn.tpsc.it4u.payload.*;
import vn.tpsc.it4u.repository.UserRepository;
import vn.tpsc.it4u.security.CustomUserDetails;
import vn.tpsc.it4u.service.UserService;
import vn.tpsc.it4u.util.ApiResponseUtils;
import vn.tpsc.it4u.security.CurrentUser;

import java.util.Locale;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("${app.api.version}")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	UserService userService;

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserController.class);

	@Autowired
	ApiResponseUtils apiResponse;

	@Operation(description = "Get current user", security = { @SecurityRequirement(name = "bearerAuth") })
	@GetMapping("/user/me")
	// @PreAuthorize("hasRole('USER')")
	public UserSummary getCurrentUser(@CurrentUser CustomUserDetails currentUser) {
		UserSummary userSummary = new UserSummary(
				currentUser.getId(),
				currentUser.getUsername(),
				currentUser.getName(),
				currentUser.getEmail(),
				currentUser.getAvatar(),
				currentUser.getGender(),
				currentUser.getType(),
				currentUser.getStatus(),
				currentUser.getSitename(),
				currentUser.getRoles());

		return userSummary;
	}

	@Operation(description = "Check is username available", security = {
			@SecurityRequirement(name = "bearerAuth") })
	@GetMapping("/user/checkUsernameAvailability")
	public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
		Boolean isAvailable = !userRepository.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}

	@Operation(description = "Check is email available", security = {
			@SecurityRequirement(name = "bearerAuth") })
	@GetMapping("/user/checkEmailAvailability")
	public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
		Boolean isAvailable = !userRepository.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}

	@Operation(description = "Update user infomation, except: role, password, avatar", security = {
			@SecurityRequirement(name = "bearerAuth") })
	@PutMapping("/user/update")
	public ResponseEntity<?> updateInfo(@CurrentUser CustomUserDetails currentUser,
			@RequestBody UserSummary updatingUser, Locale locale) {
		userService.updateInfo(currentUser, updatingUser);

		return ResponseEntity.ok(apiResponse.success(1001, locale));
	}

	@Operation(security = { @SecurityRequirement(name = "bearerAuth") })
	@PutMapping("/user/changePassword")
	public ResponseEntity<?> changePassword(@CurrentUser CustomUserDetails currentUser,
			@RequestBody ChangePasswordViewModel updatingPassword, Locale locale) {

		userService.changePassword(currentUser, updatingPassword);

		return ResponseEntity.ok(apiResponse.success(1001, locale));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(security = { @SecurityRequirement(name = "bearerAuth") })
	@GetMapping("/user")
	public ResponseEntity<?> getAllUser() {
		// return ResponseEntity.ok(apiResponse.success(userRepository.findAll(),
		// locale));
		return ResponseEntity.ok(userService.findAll());
		// return ResponseEntity.ok(apiResponse.success(userService.findAll(), locale));
	}

	@Operation(security = { @SecurityRequirement(name = "bearerAuth") })
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId, Locale locale) {
		userService.deleteUser(userId);
		return ResponseEntity.ok(apiResponse.success(200, locale));
	}

	@Operation(security = { @SecurityRequirement(name = "bearerAuth") })
	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUser(@PathVariable(value = "id") List<Long> userId, Locale locale) {
		return ResponseEntity.ok(apiResponse.success(userService.findUser(userId), locale));
	}

	@Operation(description = "Update user infomation, except: role, password, avatar", security = {
			@SecurityRequirement(name = "bearerAuth") })
	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@PathVariable(value = "id") List<Long> userId,
			@RequestBody UserSummary updatingUser, Locale locale) {
		userService.updateUser(userId, updatingUser);

		return ResponseEntity.ok(apiResponse.success(1001, locale));
	}

}

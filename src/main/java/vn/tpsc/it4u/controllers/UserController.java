package vn.tpsc.it4u.controllers;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import vn.tpsc.it4u.models.SitesName;
import vn.tpsc.it4u.models.auth.Role;
import vn.tpsc.it4u.models.auth.User;
import vn.tpsc.it4u.payloads.ChangePasswordViewModel;
import vn.tpsc.it4u.payloads.auth.SignUpRequest;
import vn.tpsc.it4u.payloads.auth.UserIdentityAvailability;
import vn.tpsc.it4u.payloads.auth.UserSummary;
import vn.tpsc.it4u.repository.RoleRepository;
import vn.tpsc.it4u.repository.SitesNameRepository;
import vn.tpsc.it4u.repository.UserRepository;
import vn.tpsc.it4u.security.CurrentUser;
import vn.tpsc.it4u.security.CustomUserDetails;
import vn.tpsc.it4u.services.UserService;
import vn.tpsc.it4u.utils.ApiResponseUtils;

@RestController
@RequestMapping("${app.api.version}")
public class UserController {

	@Value("${app.ubnt.passwordDefault}")
	public String passwordDefault;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SitesNameRepository sitenamesRepository;

	@Autowired
	UserService userService;

	@Autowired
	private ModelMapper mapper;

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserController.class);

	@Autowired
	ApiResponseUtils apiResponse;

	@Operation(description = "Get current user")
	@GetMapping("/user/me")
	// @PreAuthorize("hasRole('USER')")
	public String getCurrentUser(@CurrentUser CustomUserDetails currentUser) {
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
				currentUser.getLastTimeLogin(),
				currentUser.getNumLogin(),
				currentUser.getLanguage(),
				currentUser.getRoles(),
				currentUser.getRegistrationId());
		Long loginNum = currentUser.getNumLogin();
		JSONObject convertUserSummary = new JSONObject(userSummary);
		try {
			if (loginNum < 1)
				convertUserSummary.put("changePw", true);
			else
				convertUserSummary.put("changePw", false);
		} catch (Exception e) {
			long countLogin = 1;
			User user = mapper.map(currentUser, User.class);
			user.setNumLogin(countLogin);
			userRepository.save(user);
			convertUserSummary.put("changePw", true);
		}

		return convertUserSummary.toString();
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

	@Operation(description = "Update registration id")
	@PutMapping("/user/update/registrationId")
	public ResponseEntity<?> updateRegistrationId(@CurrentUser CustomUserDetails currentUser,
			@RequestBody UserSummary updateUser, Locale locale) {
		userService.updateRegisterId(currentUser, updateUser);

		return ResponseEntity.ok(apiResponse.success(1001, locale));
	}

	@Operation(description = "Update number of login")
	@GetMapping("/user/updateNumLogin")
	public ResponseEntity<?> updateNumLogin(@CurrentUser CustomUserDetails currentUser, Locale locale) {
		userService.updateNumLogin(currentUser);

		return ResponseEntity.ok(apiResponse.success(1001, locale));
	}

	@Operation(description = "Get user online")
	@GetMapping("/user/online")
	public String getUserOnline() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int count = 0;
		JSONObject result = new JSONObject();
		long currentTime = timestamp.getTime();
		JSONArray getAllUser = new JSONArray(userService.findAll());
		for (int i = 0; i < getAllUser.length(); i++) {
			JSONObject getUser = (JSONObject) getAllUser.get(i);
			try {
				long getLastTimeLogin = getUser.getLong("lastTimeLogin");
				if ((currentTime - getLastTimeLogin) < 86400000) {
					count = count + 1;
				}
			} catch (Exception e) {
				throw e;
			}
		}
		result.put("online", count);
		return result.toString();
	}

	@PutMapping("/user/changePassword")
	public ResponseEntity<?> changePassword(@CurrentUser CustomUserDetails currentUser,
			@RequestBody ChangePasswordViewModel updatingPassword, Locale locale) {
		userService.changePassword(currentUser, updatingPassword);

		return ResponseEntity.ok(apiResponse.success(1001, locale));
	}

	@PutMapping("/changePassword/{id}")
	public ResponseEntity<?> changePasswordById(@PathVariable(value = "id") Long id,
			@RequestBody ChangePasswordViewModel updatingPassword, Locale locale) {
		userService.changePasswordById(id, updatingPassword);

		return ResponseEntity.ok(apiResponse.success(1001, locale));
	}

	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/user")
	public ResponseEntity<?> getAllUser() {
		return ResponseEntity.ok(userService.findAll());
	}

	@PostMapping("/searchUser")
	public ResponseEntity<?> searchAllUser(@RequestBody String data) {
		JSONObject convertDataToJson = new JSONObject(data);
		String param = convertDataToJson.getString("content");
		return ResponseEntity.ok(userService.findAllByParam(param));
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId, Locale locale) {
		userService.deleteUser(userId);
		return ResponseEntity.ok(apiResponse.success(200, locale));
	}

	@GetMapping("/users/{id}")
	public String getUser(@PathVariable(value = "id") List<Long> userId) {
		JSONArray getUser = new JSONArray(userService.findUser(userId));
		// JSONObject result = (JSONObject) getUser.get(0);
		return getUser.toString();
	}

	@Operation(description = "Update user infomation, except: role, password, avatar")
	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@PathVariable(value = "id") List<Long> userId,
			@RequestBody SignUpRequest updatingUser, Locale locale) {
		Set<String> strSites = updatingUser.getSitename();
		Set<SitesName> sitenames = new HashSet<>();
		strSites.forEach(site -> {
			SitesName sitename = sitenamesRepository.findByIdname(site);
			sitenames.add(sitename);
		});
		UserSummary user = new UserSummary(
				null,
				updatingUser.getUsername().toLowerCase(),
				updatingUser.getName(),
				updatingUser.getEmail(),
				null,
				updatingUser.getGender(),
				updatingUser.getType(),
				updatingUser.getStatus(),
				sitenames,
				null,
				null,
				updatingUser.getLanguage(),
				null,
				null);
		Role role = roleRepository.findRoleByName(updatingUser.getRoles());
		userService.updateUser(userId, user, role);

		return ResponseEntity.ok(apiResponse.success(1001, locale));
	}

}

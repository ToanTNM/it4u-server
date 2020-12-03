package vn.tpsc.it4u.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import vn.tpsc.it4u.model.SitesName;
import vn.tpsc.it4u.payload.*;
import vn.tpsc.it4u.repository.SitesNameRepository;
import vn.tpsc.it4u.repository.UserRepository;
import vn.tpsc.it4u.security.CustomUserDetails;
import vn.tpsc.it4u.service.UserService;
import vn.tpsc.it4u.util.ApiResponseUtils;
import vn.tpsc.it4u.security.CurrentUser;

import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.google.api.services.compute.Compute.Autoscalers.Update;

import org.json.JSONArray;
import org.json.JSONObject;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${app.api.version}")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SitesNameRepository sitenamesRepository;

    @Autowired 
    UserService userService;

    //private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired 
    ApiResponseUtils apiResponse;

    @ApiOperation(value = "Get current user")
    @GetMapping("/user/me")
    //@PreAuthorize("hasRole('USER')")
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
            currentUser.getLastTimeLogin(),
            currentUser.getNumLogin(),
            currentUser.getLanguage(),
            currentUser.getRoles(),
            currentUser.getRegistrationId()
            );
        return userSummary;
    }

    @ApiOperation(value = "Check is username available")
    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @ApiOperation(value = "Check is email available")
    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @ApiOperation(value = "Update user infomation, except: role, password, avatar")
    @PutMapping("/user/update")
    public ResponseEntity<?> updateInfo(@CurrentUser CustomUserDetails currentUser, @RequestBody UserSummary updatingUser, Locale locale) {        
        userService.updateInfo(currentUser, updatingUser);

        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

    @ApiOperation(value = "Update registration id")
    @PutMapping("/user/update/registrationId")
    public ResponseEntity<?> updateRegistrationId(@CurrentUser CustomUserDetails currentUser, @RequestBody UserSummary updateUser, Locale locale) {
        userService.updateRegisterId(currentUser, updateUser);
        
        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

    @ApiOperation(value = "Update number of login")
    @GetMapping("/user/updateNumLogin")
    public ResponseEntity<?> updateNumLogin(@CurrentUser CustomUserDetails currentUser, Locale locale) {
        userService.updateNumLogin(currentUser);
        
        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

    @ApiOperation(value = "Get user online")
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
                //TODO: handle exception
            }
        }
        result.put("online", count);
        return result.toString();
    }



    @PutMapping("/user/changePassword")
    public ResponseEntity<?> changePassword(@CurrentUser CustomUserDetails currentUser, @RequestBody ChangePasswordViewModel updatingPassword, Locale locale) {
        userService.changePassword(currentUser, updatingPassword);

        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<?> changePasswordById(@PathVariable(value="id") Long id ,
            @RequestBody ChangePasswordViewModel updatingPassword, Locale locale) {
        userService.changePasswordById(id, updatingPassword);

        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<?> getAllUser() {
        // return ResponseEntity.ok(apiResponse.success(userRepository.findAll(), locale));
        return ResponseEntity.ok(userService.findAll());
        // return ResponseEntity.ok(apiResponse.success(userService.findAll(), locale));
    }
    @DeleteMapping("/users/{id}")
        public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId, Locale locale) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(apiResponse.success(200, locale));
    }

    @GetMapping("/users/{id}")
        public String getUser(@PathVariable(value = "id") List<Long> userId) {
            JSONArray getUser = new JSONArray(userService.findUser(userId));
            JSONObject result = (JSONObject) getUser.get(0);
            return getUser.toString();
        }

    @ApiOperation(value = "Update user infomation, except: role, password, avatar")
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") List<Long> userId, @RequestBody SignUpRequest updatingUser, Locale locale) {         
        Set<String> strSites = updatingUser.getSitename();
        Set<SitesName> sitenames = new HashSet<>();
        strSites.forEach(site -> {
            SitesName sitename = sitenamesRepository.findByIdname(site);
            sitenames.add(sitename);
        });
        UserSummary user = new UserSummary(
                    null, 
                    updatingUser.getUsername(), 
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
                    null
            );
        userService.updateUser(userId, user);

        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

}

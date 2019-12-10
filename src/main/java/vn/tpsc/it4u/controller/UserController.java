package vn.tpsc.it4u.controller;

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

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${app.api.version}")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    UserService userService;

    //private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired 
    ApiResponseUtils apiResponse;

    @ApiOperation(value = "Get current user")
    @GetMapping("/user/me")
    //@PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser CustomUserDetails currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), 
        currentUser.getUsername(), 
        currentUser.getName(), 
        currentUser.getEmail(),
        currentUser.getAvatar(),
        currentUser.getGender(), 
        currentUser.getType(), 
        currentUser.getStatus());
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

    @PutMapping("/user/changePassword")
    public ResponseEntity<?> changePassword(@CurrentUser CustomUserDetails currentUser, @RequestBody ChangePasswordViewModel updatingPassword, Locale locale) {

        userService.changePassword(currentUser, updatingPassword);

        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<?> getAllUser(Locale locale) {
        // return ResponseEntity.ok(apiResponse.success(userRepository.findAll(), locale));
        return ResponseEntity.ok(apiResponse.success(userService.findAll(), locale));
    }
}

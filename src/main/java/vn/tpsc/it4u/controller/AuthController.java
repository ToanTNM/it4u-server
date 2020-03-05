package vn.tpsc.it4u.controller;

import java.net.URI;
import java.util.Collections;
import java.util.Locale;

import javax.validation.Valid;

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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import vn.tpsc.it4u.exception.AppException;
import vn.tpsc.it4u.model.Role;
import vn.tpsc.it4u.model.enums.RoleName;
import vn.tpsc.it4u.model.enums.UserStatus;
import vn.tpsc.it4u.model.User;
import vn.tpsc.it4u.payload.JwtAuthenticationResponse;
import vn.tpsc.it4u.payload.LoginRequest;
import vn.tpsc.it4u.payload.SignUpRequest;
import vn.tpsc.it4u.repository.RoleRepository;
import vn.tpsc.it4u.repository.UserRepository;
import vn.tpsc.it4u.security.JwtTokenProvider;
import vn.tpsc.it4u.util.ApiResponseUtils;

/**
 * AuthController
 */
@RestController
@RequestMapping("${app.api.version}/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired 
    ApiResponseUtils apiResponse;

    @PostMapping("/signin")
    @ApiOperation(value = "Sign In App")
    @ApiResponses(value = {
        @ApiResponse(code = 1000, message = "Successfully retrieved list")
    })
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody final LoginRequest loginRequest, Locale locale) {

        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                );
        final Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(apiResponse.success(new JwtAuthenticationResponse(jwt), locale));
    }

    @PostMapping("/signup")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody final SignUpRequest signUpRequest, Locale locale) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(apiResponse.error(1021, locale),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
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
            signUpRequest.getSitename(),
            signUpRequest.getLanguage()
            );

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
                case "ROLE_KT":
                    final Role roleKT = roleRepository.findByName(RoleName.ROLE_KT)
                            .orElseThrow(() -> new AppException("User Role not set."));
                    user.setRoles(Collections.singleton(roleKT));
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

        final User result = userRepository.save(user);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(apiResponse.success("User registered successfully"));
    }    
}
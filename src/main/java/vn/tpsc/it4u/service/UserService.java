package vn.tpsc.it4u.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.exception.AppException;
import vn.tpsc.it4u.model.Role;
import vn.tpsc.it4u.model.User;
import vn.tpsc.it4u.payload.ChangePasswordViewModel;
import vn.tpsc.it4u.payload.UserSummary;
import vn.tpsc.it4u.payload.UserUpdateSummary;
import vn.tpsc.it4u.repository.UserRepository;
import vn.tpsc.it4u.security.CustomUserDetails;
import vn.tpsc.it4u.util.StringUtils;

/**
 * UserService
 */
@Service
@ExtensionMethod({StringUtils.class})
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private ModelMapper mapper;

    public Boolean isMatchPassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    public User getUser(long userId) {
        return userRepository.findById(userId);
    }

    public Boolean updateInfo(CustomUserDetails currentUser, UserSummary updatingUser) {
        User user = mapper.map(currentUser, User.class);
        //name
        user.setName(updatingUser.getName().isNullorEmpty() ? currentUser.getName() : updatingUser.getName());
        //username
        user.setUsername(updatingUser.getUsername().isNullorEmpty() ? currentUser.getUsername() : updatingUser.getUsername()); 
        //email
        user.setEmail((updatingUser.getEmail() == null || updatingUser.getEmail().isEmpty()) ? currentUser.getEmail() : updatingUser.getEmail()); 
        //Gender gender
        user.setGender(updatingUser.getGender() != null ? updatingUser.getGender() : user.getGender());
        //UserType type
        user.setType(updatingUser.getType() != null ? updatingUser.getType() : user.getType());
        //Language
        user.setLanguage(updatingUser.getLanguage() != null ? updatingUser.getLanguage() : user.getLanguage());
       
        userRepository.save(user);

        return true;
    }

    public Boolean updateRegisterId(CustomUserDetails currentUser, UserSummary updatingUser) {
        User user = mapper.map(currentUser, User.class);
        user.setRegistrationId(updatingUser.getRegistrationId() != null ? updatingUser.getRegistrationId() : user.getRegistrationId());
        userRepository.save(user);
        return true;
    }

    public Boolean updateNumLogin(CustomUserDetails currentUser) {
        User user = mapper.map(currentUser, User.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        user.setLastTimeLogin(timestamp.getTime());
        long numLogin = 0;
        try {
            numLogin = currentUser.getNumLogin() + 1;
        } catch (Exception e) {
            numLogin = 1;
        }
        user.setNumLogin(numLogin);
        userRepository.save(user);
        return true;
    }

    public Boolean changePassword(CustomUserDetails currentUser, ChangePasswordViewModel model) {
        // User user = new User();
        // mapper.map(currentUser, user);

        User user = mapper.map(currentUser, User.class);
        if(isMatchPassword(model.getNewPassword(), currentUser.getPassword())) {
            throw new AppException("New password is the same with the old one");
        }

        if(!model.getNewPassword().equals(model.getConfirmPassword())){
            throw new AppException("Password is not match");
        }

        user.setPassword(encoder.encode(model.getConfirmPassword()));

        userRepository.save(user);

        return true;
    }

    public Boolean changePasswordById(long id, ChangePasswordViewModel model) {
        // User user = new User();
        // mapper.map(currentUser, user);
        User user = userRepository.findById(id);

        if (!model.getNewPassword().equals(model.getConfirmPassword())) {
            throw new AppException("Password is not match");
        }

        user.setPassword(encoder.encode(model.getConfirmPassword()));

        userRepository.save(user);

        return true;
    }

    public List<UserSummary> findAll() {
        List<User> users = userRepository.findAll();
        
        List<UserSummary> listUsers = users.stream()
            .map(user -> 
                new UserSummary(
                    user.getId(), 
                    user.getUsername(), 
                    user.getName(), 
                    user.getEmail(), 
                    user.getAvatar(), 
                    user.getGender(), 
                    user.getType(), 
                    user.getStatus(),
                    user.getSitename(),
                    user.getLastTimeLogin(),
                    user.getNumLogin(),
                    user.getLanguage(),
                    user.getRoles(),
                    user.getRegistrationId()
                ))
            .collect(Collectors.toList());

        return listUsers;
    }

    public List<UserSummary> findAllByParam(String param) {
        List<User> users = userRepository.findAllUserByParam(param);
        List<UserSummary> listUsers = users.stream()
            .map(user -> 
                new UserSummary(
                    user.getId(), 
                    user.getUsername(), 
                    user.getName(), 
                    user.getEmail(), 
                    user.getAvatar(), 
                    user.getGender(), 
                    user.getType(), 
                    user.getStatus(),
                    user.getSitename(),
                    user.getLastTimeLogin(),
                    user.getNumLogin(),
                    user.getLanguage(),
                    user.getRoles(),
                    user.getRegistrationId()
                ))
            .collect(Collectors.toList());

        return listUsers;
    }

    public Boolean deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return true;
    }

    public Optional<User> getRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }

    public List<UserSummary> findUser(List<Long> userId) {
        List<User> users = userRepository.findByIdIn(userId);
        List<UserSummary> listUsers = users.stream()
            .map(user -> 
            new UserSummary(
                    user.getId(), 
                    user.getUsername(), 
                    user.getName(), 
                    user.getEmail(), 
                    user.getAvatar(), 
                    user.getGender(),
                    user.getType(), 
                    user.getStatus(),
                    user.getSitename(),
                    user.getLastTimeLogin(),
                    user.getNumLogin(),
                    user.getLanguage(),
                    user.getRoles(),
                    user.getRegistrationId()
                    ))
            .collect(Collectors.toList());
        return listUsers;
    }

    public boolean updateUser(List<Long> userId, UserSummary updatingUser, Role role) {
        // List<User> users = userRepository.findByIdIn(userId);
        List<User> users = userRepository.findByIdIn(userId);
        User user = mapper.map(users.get(0), User.class);
        user.setName(updatingUser.getName().isNullorEmpty() ? user.getName() : updatingUser.getName());
        //username
        user.setUsername(updatingUser.getUsername().isNullorEmpty() ? user.getUsername() : updatingUser.getUsername()); 
        //email
        user.setEmail((updatingUser.getEmail() == null || updatingUser.getEmail().isEmpty()) ? user.getEmail() : updatingUser.getEmail()); 
        //password
        //user.setPassword((updatingUser.() == null || updatingUser.getEmail().isEmpty()) ? user.getEmail() : updatingUser.getEmail()); 
        //Gender gender
        user.setGender(updatingUser.getGender() != null ? updatingUser.getGender() : user.getGender());
        //UserType type
        user.setType(updatingUser.getType() != null ? updatingUser.getType() : user.getType());

        user.setSitename(updatingUser.getSitename() != null ? updatingUser.getSitename() : user.getSitename());
        
        user.setLanguage(updatingUser.getLanguage() != null ? updatingUser.getLanguage() : user.getLanguage());
        // Set<Role> roles = updatingUser.getRoles();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        
        userRepository.save(user);

        return true;
    }
}
package vn.tpsc.it4u.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.exception.AppException;
import vn.tpsc.it4u.model.User;
import vn.tpsc.it4u.payload.ChangePasswordViewModel;
import vn.tpsc.it4u.payload.UserSummary;
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

        if(!model.getCurrentPassword().equals(model.getConfirmPassword())){
            throw new AppException("Password is not match");
        }

        user.setPassword(encoder.encode(model.getNewPassword()));

        userRepository.save(user);

        return true;
    }
}
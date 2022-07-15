package vn.tpsc.it4u.service;

import java.util.Optional;
import vn.tpsc.it4u.model.User;

public interface ForgotUserService {
    public Optional<User> findUserByEmail(String email);
    public Optional<User> findUserByResetToken(String resetToken);
    public void save(User user);
}
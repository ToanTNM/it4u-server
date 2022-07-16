package vn.tpsc.it4u.services;

import java.util.Optional;

import vn.tpsc.it4u.models.User;

public interface ForgotUserService {
	public Optional<User> findUserByEmail(String email);

	public Optional<User> findUserByResetToken(String resetToken);

	public void save(User user);
}
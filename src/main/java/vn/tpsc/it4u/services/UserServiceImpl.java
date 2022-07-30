package vn.tpsc.it4u.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import vn.tpsc.it4u.models.auth.User;
import vn.tpsc.it4u.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service("forgotUserService")
public class UserServiceImpl implements ForgotUserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<User> findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Optional<User> findUserByResetToken(String resetToken) {
		return userRepository.findByResetToken(resetToken);
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}
}
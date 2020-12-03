package vn.tpsc.it4u.repository;

import java.util.List;
import java.util.Optional;

// import javax.jws.soap.SOAPBinding.Use;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.User;

/**
 * UserRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByRefreshToken(String refreshToken);

    List<User> findByIdIn(List<Long> userIds);

    User findById(long id);

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    
}
package vn.tpsc.it4u.model.token;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tpsc.it4u.model.User;
/**
 * ConfigToken
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
    @SequenceGenerator(name = "refresh_token_seq", allocationSize = 1)
    private Long id;

    @NotEmpty
    @Size(max = 40)
    @NaturalId(mutable = true)
    private String token;

    @OneToOne(optional = false, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", unique = true)
    private User users;

    @NotEmpty
    @Size(max = 40)
    private Instant expiryDate;

    public RefreshToken(Long id, String token, User users, Instant expiryDate) {
        this.id = id;
        this.token = token;
        this.users = users;
        this.expiryDate = expiryDate;
    }
}
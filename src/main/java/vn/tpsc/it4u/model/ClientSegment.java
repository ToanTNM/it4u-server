package vn.tpsc.it4u.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Segment client
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client_segment")
public class ClientSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 40)
    private String name;

    public ClientSegment(String name) {
        this.name = name;
    }
}

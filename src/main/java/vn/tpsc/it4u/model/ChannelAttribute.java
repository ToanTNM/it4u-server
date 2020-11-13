package vn.tpsc.it4u.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.HashSet;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * ChannelAttribute
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChannelAttribute{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 40)
    private String customer;

    @OneToOne
    @JoinColumn(name = "channel_value_id")
    private ChannelValue channelValue;

    @NotEmpty
    @Size(max = 40)
    private String status;

    public ChannelAttribute(String customer,String status) {
        this.customer = customer;
        this.status = status;
    }
}

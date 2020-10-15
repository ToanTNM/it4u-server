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

    @ManyToMany
    @JoinTable(name = "channel_name_attribute", 
        joinColumns = @JoinColumn(name = "channel_attribute_id"), 
        inverseJoinColumns = @JoinColumn(name = "channel_name_id")
    )
    private Set<ChannelName> channelName = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "channel_value_attribute", 
        joinColumns = @JoinColumn(name = "channel_attribute_id"), 
        inverseJoinColumns = @JoinColumn(name = "channel_value_id")
    )
    private Set<ChannelValue> channelValue = new HashSet<>();

    @NotEmpty
    @Size(max = 40)
    private String status;

    public ChannelAttribute(String status) {
        this.status = status;
    }
}

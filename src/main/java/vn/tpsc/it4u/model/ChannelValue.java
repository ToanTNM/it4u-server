package vn.tpsc.it4u.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * ChannelName
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "channelValue")
public class ChannelValue {
    /**
    *
    */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 40)
    private String servicePack;

    @NotEmpty
    @Size(max = 40)
    private String value;

    @OneToOne
    @JoinColumn(name = "channelName_id")
    private ChannelName channelName;

    public ChannelValue(String servicePack, String value, ChannelName channelName) {
        this.servicePack = servicePack;
        this.value = value;
        this.channelName = channelName;
    }
}

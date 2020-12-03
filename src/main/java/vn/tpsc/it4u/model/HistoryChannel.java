package vn.tpsc.it4u.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tpsc.it4u.model.audit.UserDateAudit;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class HistoryChannel extends UserDateAudit {
    /**
    *
    */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "channel_attribute_id")
    private ChannelAttribute channelAttribute;

    @OneToOne
    @JoinColumn(name = "channel_detail_id")
    private Contract contract;

    public HistoryChannel(ChannelAttribute channelAttribute, Contract contract) {
        this.channelAttribute = channelAttribute;
        this.contract = contract;
    }
}

package vn.tpsc.it4u.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "channelDetail")
public class ChannelDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 40)
    private String customId;
    
    @NotEmpty
    @Size(max = 40)
    private String numContact;

    @NotEmpty
    @Size(max = 40)
    private String clientName;

    @NotEmpty
    @Size(max = 40)
    private String servicePlans;

    @Size(max = 40)
    private String routerType;

    @Size(max = 40)
    private String votesRequire;

    @Size(max = 40)
    private String ipType;

    @NotEmpty
    @Size(max = 40)
    private String virtualNum;

    @Size(max = 40)
    private String regionalEngineer;

    @Size(max = 40)
    private Date deployRequestDate;

    @Size(max = 40)
    private Long dayAcceptance;

    @Size(max = 40)
    private Date monthAcceptance;

    @Size(max = 40)
    private String addressIP;

    @Size(max = 40)
    private String dateRequestStop;

    @Size(max = 40)
    private Long dayStop;

    @Size(max = 40)
    private Date monthStop;

    @Size(max = 40)
    private Date dateOnline;

    @Size(max = 40)
    private Long dayOnline;

    @Size(max = 40)
    private Date monthOnline;

    @Size(max = 40)
    private String fees;

    @ManyToMany
    @JoinTable(name = "channel_attribute_detail", joinColumns = @JoinColumn(name = "channel_detail_id"), inverseJoinColumns = @JoinColumn(name = "channel_attribute_id"))
    private Set<ChannelAttribute> channelValue = new HashSet<>();

    public ChannelDetail(String customId, String numContact, String clientName, String servicePlans, String routerType, String votesRequire, String ipType, String virtualNum, String regionalEngineer, Date deployRequestDate,
        Long dayAcceptance, Date monthAcceptance, String addressIp, String dateRequestStop, Long dayStop, Date monthStop, Date dateOnline, Long dayOnline, Date monthOnline, String fees) {
        this.customId = customId;
        this.numContact = numContact;
        this.clientName = clientName;
        this.servicePlans = servicePlans;
        this.routerType = routerType;
        this.votesRequire = votesRequire;
        this.ipType = ipType;
        this.virtualNum = virtualNum;
        this.regionalEngineer = regionalEngineer;
        this.deployRequestDate = deployRequestDate;
        this.dayAcceptance = dayAcceptance;
        this.monthAcceptance = monthAcceptance;
        this.addressIP = addressIp;
        this.dateRequestStop = dateRequestStop;
        this.dayStop = dayStop;
        this.monthStop = monthStop;
        this.dateOnline = dateOnline;
        this.dayOnline = dayOnline;
        this.monthOnline = monthOnline;
        this.fees = fees;
    }

}

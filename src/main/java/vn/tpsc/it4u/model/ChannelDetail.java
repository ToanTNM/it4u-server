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

    @OneToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    private String routerType;

    private String customerMove;

    @Size(max = 40)
    private String votesRequire;

    @Size(max = 40)
    private String ipType;

    @Size(max = 40)
    private String deviceStatus;

    @Size(max = 40)
    private String ipAddress;

    @Size(max = 40)
    private String regionalEngineer;

    private Long deployRequestDate;

    private Long dateAcceptance;

    private Long dateRequestStop;

    private Long dateStop;

    private Long dateOnlineRequest;

    private Long dateOnline;

    private String fees;

    @OneToOne
    @JoinColumn(name = "channel_attribute_id")
    private ChannelAttribute channelAttribute;

    public ChannelDetail(String routerType, String customerMove, String deviceStatus, String votesRequire, String ipType, String regionalEngineer, Long deployRequestDate,
        Long dateAcceptance, String ipAddress, Long dateRequestStop, Long dateStop, Long dateOnlineRequest, Long dateOnline, String fees) {
        this.routerType = routerType;
        this.customerMove = customerMove;
        this.votesRequire = votesRequire;
        this.ipType = ipType;
        this.deviceStatus = deviceStatus;
        this.regionalEngineer = regionalEngineer;
        this.deployRequestDate = deployRequestDate;
        this.dateAcceptance = dateAcceptance;
        this.ipAddress = ipAddress;
        this.dateRequestStop = dateRequestStop;
        this.dateStop = dateStop;
        this.dateOnlineRequest = dateOnlineRequest;
        this.dateOnline = dateOnline;
        this.fees = fees;
    }

}

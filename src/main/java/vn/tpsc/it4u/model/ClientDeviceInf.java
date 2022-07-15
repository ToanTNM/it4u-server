package vn.tpsc.it4u.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tpsc.it4u.model.audit.UserDateAudit;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ClientDeviceInf extends UserDateAudit {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @OneToOne
    @JoinColumn(name = "sitename_id")
    private SitesName sitename;

    private String backup;

    private String maintenance;

    private String ispWan1;

    private String modelWan1;

    private String ipWan1;

    private String ipPublicWan1;

    private String passWan1;

    private String ispWan2;

    private String modelWan2;

    private String ipWan2;

    private String ipPublicWan2;

    private String passWan2;

    private String ispWan3;

    private String modelWan3;

    private String ipWan3;

    private String ipPublicWan3;

    private String passWan3;

    private String ispWan4;

    private String modelWan4;

    private String ipWan4;

    private String ipPublicWan4;
   
    private String passWan4;

    private String modelLb;

    private String ipLb;

    private String passLb;

    private String modelOther;

    private String ipOther;

    private String passOther;

    private String reboot;

    private String monitor;

    private String method;

    private String location;

    private String openPort;

    private String firmware;

    private String wanGroup;

    private String note;

    public ClientDeviceInf(String backup, String maintenance, String ispWan1, String modelWan1, String ipWan1, String passWan1,
        String ispWan2, String modelWan2, String ipWan2, String passWan2, String ispWan3, String modelWan3, String ipWan3, String passWan3,
        String ispWan4, String modelWan4, String ipWan4, String passWan4, String modelLb, String ipLb, String passLb, String modelOther, String ipOther, String passOther,
        String reboot, String monitor, String method, String location, String openPort, String firmware, String wanGroup, String note) {
            this.backup = backup;
            this.maintenance = maintenance;
            this.ispWan1 = ispWan1;
            this.modelWan1 = modelWan1;
            this.ipWan1 = ipWan1;
            this.passWan1 = passWan1;
            this.ispWan2 = ispWan2;
            this.modelWan2 = modelWan2;
            this.ipWan2 = ipWan2;
            this.passWan2 = passWan2;
            this.ispWan3 = ispWan3; 
            this.modelWan3 = modelWan3;
            this.ipWan3 = ipWan3;
            this.passWan3 = passWan3;
            this.ispWan4 = ispWan4;
            this.modelWan4 = modelWan4;
            this.ipWan4 = ipWan4;
            this.passWan4 = passWan4;
            this.modelLb = modelLb;
            this.ipLb = ipLb;
            this.passLb = passLb;
            this.modelOther = modelOther;
            this.ipOther = ipOther;
            this.passOther = passOther;
            this.reboot = reboot;
            this.monitor = monitor;
            this.method = method;
            this.location = location;
            this.openPort = openPort;
            this.firmware = firmware;
            this.wanGroup = wanGroup;
            this.note = note;
        }


}

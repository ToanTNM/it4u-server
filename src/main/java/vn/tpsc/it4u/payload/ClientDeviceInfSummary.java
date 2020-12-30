package vn.tpsc.it4u.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.tpsc.it4u.model.Contract;
import vn.tpsc.it4u.model.SitesName;

@AllArgsConstructor
@Getter
@Setter
public class ClientDeviceInfSummary {
   
    private Long id;

    private Contract contract;

    private SitesName sitename;

    private String backup;

    private String maintenance;

    private String ispWan1;

    private String modelWan1;

    private String ipWan1;

    private String passWan1;

    private String ispWan2;

    private String modelWan2;

    private String ipWan2;

    private String passWan2;

    private String ispWan3;

    private String modelWan3;

    private String ipWan3;

    private String passWan3;

    private String ispWan4;

    private String modelWan4;

    private String ipWan4;

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
}

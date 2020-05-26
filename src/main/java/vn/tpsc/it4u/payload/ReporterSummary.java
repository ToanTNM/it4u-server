package vn.tpsc.it4u.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReporterSummary {
    private Long id;
    private String sitename;
    private String wan1Provider;
    private String wan1Ip;
    private String wan1Status;
    private String wan1Uptime;
    private String wan2Provider;
    private String wan2Ip;
    private String wan2Status;
    private String wan2Uptime;
    private String wan3Provider;
    private String wan3Ip;
    private String wan3Status;
    private String wan3Uptime;
    private String wan4Provider;
    private String wan4Ip;
    private String wan4Status;
    private String wan4Uptime;
    private String uptimeLb;
    private String upload;
    private String download;
    private String APDisconnected;
    private String APConnected;
    private String groupClient;
}

package vn.tpsc.it4u.security;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.tpsc.it4u.model.Reporter;

/**
 * CustomReporterDetails Spring Security will use the information stored in the this
 * class' object to perform authentication and authorization
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class CustomReporterDetails {

    /**
     *
     */

    @Id
    private Long id;

    private String sitename;

    private String wan1Ip;

    private String wan1Provider;

    private String wan1Status;
    
    private String wan1Uptime;

    private String wan2Ip;

    private String wan2Provider;

    private String wan2Status;

    private String wan2Uptime;

    private String wan3Ip;

    private String wan3Provider;

    private String wan3Status;

    private String wan3Uptime;

    private String wan4Ip;

    private String wan4Provider;

    private String wan4Status;

    private String wan4Uptime;

    private String uptimeLb;

    private String upload;

    private String download;

    private String APDisconnected;

    private String APConnected;


    public static CustomReporterDetails create(Reporter reporter) {
        return new CustomReporterDetails(
            reporter.getId(), 
            reporter.getSitename(),
            reporter.getWan1Ip(), 
            reporter.getWan1Provider(), 
            reporter.getWan1Status(),
            reporter.getWan1Uptime(),
            reporter.getWan2Ip(), 
            reporter.getWan2Provider(), 
            reporter.getWan2Status(),
            reporter.getWan2Uptime(),
            reporter.getWan3Ip(), 
            reporter.getWan3Provider(), 
            reporter.getWan3Status(),
            reporter.getWan3Uptime(),
            reporter.getWan4Ip(), 
            reporter.getWan4Provider(), 
            reporter.getWan4Status(),
            reporter.getWan4Uptime(),
            reporter.getUptimeLb(),
            reporter.getUpload(),
            reporter.getDownload(),
            reporter.getAPConnected(),
            reporter.getAPDisconnected()
        );

    }
}
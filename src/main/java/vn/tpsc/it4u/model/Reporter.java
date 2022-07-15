package vn.tpsc.it4u.model;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tpsc.it4u.model.audit.UserDateAudit;

/**
 * Dev
 */
@Getter
@Setter
@NoArgsConstructor
// @Document("reporter")
@Entity
@Table(name = "reporter", uniqueConstraints = { @UniqueConstraint(columnNames = { "sitename" })})
public class Reporter extends UserDateAudit {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 40)
    private String sitename;

    @NotEmpty
    @Size(max = 40)
    private String wan1Provider;

    @NotEmpty
    @Size(max = 40)
    private String wan1Ip;

    @NotEmpty
    @Size(max = 40)
    private String wan1Status;

    @NotEmpty
    @Size(max = 40)
    private String wan1Uptime;

    @NotEmpty
    @Size(max = 40)
    private String wan2Provider;

    @NotEmpty
    @Size(max = 40)
    private String wan2Ip;

    @NotEmpty
    @Size(max = 40)
    private String wan2Status;

    @NotEmpty
    @Size(max = 40)
    private String wan2Uptime;

    @NotEmpty
    @Size(max = 40)
    private String wan3Provider;

    @NotEmpty
    @Size(max = 40)
    private String wan3Ip;

    @NotEmpty
    @Size(max = 40)
    private String wan3Status;

    @NotEmpty
    @Size(max = 40)
    private String wan3Uptime;

    @NotEmpty
    @Size(max = 40)
    private String wan4Provider;

    @NotEmpty
    @Size(max = 40)
    private String wan4Ip;

    @NotEmpty
    @Size(max = 40)
    private String wan4Status;

    @NotEmpty
    @Size(max = 40)
    private String wan4Uptime;

    @NotEmpty
    @Size(max = 40)
    private String uptimeLb;

    @NotEmpty
    @Size(max = 40)
    private String upload;

    @NotEmpty
    @Size(max = 40)
    private String download;

    @NotEmpty
    @Size(max = 40)
    private String APDisconnected;

    @NotEmpty
    @Size(max = 40)
    private String APConnected;

    @NotEmpty
    @Size(max = 40)
    private String groupClient;
    

    public Reporter(String sitename, String wan1Provider, String wan1Ip, String wan1Status, String wan1Uptime,String wan2Provider, String wan2Ip, String wan2Status, String wan2Uptime,
    String wan3Provider, String wan3Ip, String wan3Status, String wan3Uptime,String wan4Provider, String wan4Ip, String wan4Status, String wan4Uptime, String uptimeLb, String upload,
    String download, String APDisconnected, String APConnected, String groupClient) {
        this.sitename = sitename;
        this.wan1Ip = wan1Ip;
        this.wan1Provider = wan1Provider;
        this.wan1Status = wan1Status;
        this.wan1Uptime = wan1Uptime;
        this.wan2Ip = wan1Ip;
        this.wan2Provider = wan1Provider;
        this.wan2Status = wan1Status;
        this.wan2Uptime = wan1Uptime;
        this.wan3Ip = wan1Ip;
        this.wan3Provider = wan1Provider;
        this.wan3Status = wan1Status;
        this.wan3Uptime = wan1Uptime;
        this.wan4Ip = wan1Ip;
        this.wan4Provider = wan1Provider;
        this.wan4Status = wan1Status;
        this.wan4Uptime = wan1Uptime;
        this.uptimeLb = uptimeLb;
        this.upload = upload;
        this.download = download;
        this.APConnected = APConnected;
        this.APDisconnected = APDisconnected;
        this.groupClient = groupClient;
    }
}
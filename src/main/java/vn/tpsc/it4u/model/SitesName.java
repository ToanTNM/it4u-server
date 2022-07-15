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
 * SitesName
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sitesname", uniqueConstraints = { @UniqueConstraint(columnNames = { "sitename" })})

public class SitesName extends UserDateAudit {

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

    @Size(max = 40)
    private String idname;

    public SitesName(String sitename, String idname) {
        this.sitename = sitename;
        this.idname = idname;
    }
}
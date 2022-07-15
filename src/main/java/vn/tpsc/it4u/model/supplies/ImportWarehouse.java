package vn.tpsc.it4u.model.supplies;

import java.sql.Timestamp;

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
public class ImportWarehouse extends UserDateAudit{
    /**
    *
    */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licence;

    @OneToOne
    @JoinColumn(name = "list_supplies_id")
    private ListSupplies listSupplies;

    private Long number;

    private Long value;

    private Long totalAmount;

    private String supplier;

    private String serialNum;

    private String MAC;

    private Long warrantyPeriod;

    private Long storageTerm;

    private Long warrantyLandmark;

    private Long importDate;

    private String note;
    
    public ImportWarehouse(String licence, Long number, Long value, Long totalAmount, String supplier, String serialNum,
        String MAC, Long warrantyPeriod, Long storageTerm, Long warrantyLandmark, Long importDate, String note) {
            this.licence = licence;
            this.number = number;
            this.value = value;
            this.totalAmount = totalAmount;
            this.supplier = supplier;
            this.serialNum = serialNum;
            this.MAC = MAC;
            this.warrantyPeriod = warrantyPeriod;
            this.storageTerm = storageTerm;
            this.warrantyLandmark = warrantyLandmark;
            this.importDate = importDate;
            this.note = note;
        }
}

package vn.tpsc.it4u.payload.supplies;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.tpsc.it4u.model.audit.UserDateAudit;
import vn.tpsc.it4u.model.supplies.ListSupplies;

@AllArgsConstructor
@Getter
@Setter
public class ImportWarehouseSummary extends UserDateAudit{
    /**
    *
    */
    private static final long serialVersionUID = 1L;

    private Long id;

    private String licence;

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

    private String note;

    private Instant createdAt;
}

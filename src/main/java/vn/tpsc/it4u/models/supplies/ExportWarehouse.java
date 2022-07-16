package vn.tpsc.it4u.models.supplies;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tpsc.it4u.models.Contract;
import vn.tpsc.it4u.models.audit.UserDateAudit;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ExportWarehouse extends UserDateAudit {
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

	private String status;

	private String supplier;

	private String serialNum;

	private String MAC;

	private Long warrantyPeriod;

	private Long storageTerm;

	private Long warrantyLandmark;

	private Long exportDate;

	@OneToOne
	@JoinColumn(name = "contract_id")
	private Contract contract;

	private String note;

	public ExportWarehouse(String licence, Long number, String status, String supplier, String serialNum,
			String MAC, Long warrantyPeriod, Long storageTerm, Long warrantyLandmark, Long exportDate, String note) {
		this.licence = licence;
		this.status = status;
		this.number = number;
		this.supplier = supplier;
		this.serialNum = serialNum;
		this.MAC = MAC;
		this.warrantyPeriod = warrantyPeriod;
		this.storageTerm = storageTerm;
		this.warrantyLandmark = warrantyLandmark;
		this.exportDate = exportDate;
		this.note = note;
	}
}

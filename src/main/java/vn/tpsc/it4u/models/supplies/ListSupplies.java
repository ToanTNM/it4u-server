package vn.tpsc.it4u.models.supplies;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ListSupplies {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String itemCode;

	private String name;

	private String unit;

	private Long number;

	private Long value;

	private String note;

	public ListSupplies(String itemCode, String name, String unit, Long number, Long value, String note) {
		this.itemCode = itemCode;
		this.name = name;
		this.unit = unit;
		this.number = number;
		this.value = value;
		this.note = note;
	}
}

package vn.tpsc.it4u.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_deployment")
public class ServiceDeployment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "contract_id")
	private Contract contract;

	private String receiver;

	private Long firstTime;

	private Long secondTime;

	private Long duration;

	private String result;

	private String completionRate;

	private String verify;

	public ServiceDeployment(String receiver, Long firstTime, Long secondTime, Long duration,
			String result, String completionRate, String verify) {
		this.receiver = receiver;
		this.firstTime = firstTime;
		this.secondTime = secondTime;
		this.duration = duration;
		this.result = result;
		this.completionRate = completionRate;
		this.verify = verify;
	}
}

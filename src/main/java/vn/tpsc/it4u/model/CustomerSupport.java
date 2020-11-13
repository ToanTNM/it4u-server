package vn.tpsc.it4u.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_support")
public class CustomerSupport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    private String request;

    private String receiver;

    private Long firstTime;

    private Long secondTime;

    private Long duration;

    private String result;

    private String status;

    public CustomerSupport(String request, String receiver, Long firstTime, Long secondTime, Long duration,
        String result, String status) {
            this.request = request;
            this.receiver = receiver;
            this.firstTime = firstTime;
            this.secondTime = secondTime;
            this.duration = duration;
            this.result = result;
            this.status = status;
        }
}

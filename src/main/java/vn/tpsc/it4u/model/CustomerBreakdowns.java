package vn.tpsc.it4u.model;

import javax.persistence.Entity;

import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_breakdowns")
public class CustomerBreakdowns {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    private String error;

    private String receiver;

    private Long firstTime;

    private Long secondTime;

    private Long duration;

    private String influenceLevel;

    private String reason;

    private String result;

    private String verify;

    private String status;

    private String classify;

    public CustomerBreakdowns(String error, String receiver, Long firstTime, Long secondTime, Long duration, String influenceLevel, String reason
        , String result, String verify, String status, String classify) {
            this.error = error;
            this.receiver = receiver;
            this.firstTime = firstTime;
            this.secondTime = secondTime;
            this.duration = duration;
            this.influenceLevel = influenceLevel;
            this.reason = reason;
            this.result = result;
            this.verify = verify;
            this.status = status;
            this.classify = classify;
        }
}

package vn.tpsc.it4u.model;

import javax.persistence.Entity;

import javax.persistence.*;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contract", uniqueConstraints = { @UniqueConstraint(columnNames = {"customId"}),
        @UniqueConstraint(columnNames = { "clientName" })})
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 40)
    private String customId;
    
    private String numContract;

    private String clientName;

    private String servicePlans;

    private String street;

    private String phone;

    public Contract(String customId, String numContract, String clientName, String servicePlans, String street, String phone) {
        this.customId = customId;
        this.numContract = numContract;
        this.clientName = clientName;
        this.servicePlans = servicePlans;
        this.street = street;
        this.phone = phone;
    }
}

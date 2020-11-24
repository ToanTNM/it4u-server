package vn.tpsc.it4u.model;

import javax.persistence.Entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @NotEmpty
    @Size(max = 40)
    private String customId;
    
    @NotEmpty
    private String numContract;

    @NotEmpty
    private String clientName;

    @NotEmpty
    private String servicePlans;

    private String street;

    public Contract(String customId, String numContract, String clientName, String servicePlans, String street) {
        this.customId = customId;
        this.numContract = numContract;
        this.clientName = clientName;
        this.servicePlans = servicePlans;
        this.street = street;
    }
}

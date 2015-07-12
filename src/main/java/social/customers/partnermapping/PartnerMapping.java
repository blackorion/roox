package social.customers.partnermapping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import social.customers.Customer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class PartnerMapping implements Serializable {
    @Id
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequenceName", value = "sequence"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
    public long id;
    @Column(name = "partner_id", nullable = false)
    public long partnerId;
    @Basic
    public long accId;
    @Basic
    public String fio;
    @Basic
    public String gravatar;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    public Customer customer;
}

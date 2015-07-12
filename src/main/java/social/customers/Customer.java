package social.customers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import social.customers.partnermapping.PartnerMapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer implements Serializable {
    @Id
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequenceName", value = "sequence"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
    public long id;
    @Basic
    public String fio;
    @Basic
    public BigDecimal balance;
    @Enumerated(EnumType.STRING)
    public ActiveState state = ActiveState.BLOCKED;
    @Column(name = "username", nullable = false, unique = true, length = 64)
    public String username;
    @JsonIgnore
    @NotNull
    @Size(max = 64)
    public String passwordHash;
    @JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    public List<PartnerMapping> mappingList = new ArrayList<>();

    public enum ActiveState {
        ACTIVE, BLOCKED
    }
}

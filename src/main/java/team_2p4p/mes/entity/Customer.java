package team_2p4p.mes.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name="customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(name="customer_address")
    private String customerAddress;

    @Column(nullable = false, name="customer_name")
    private String customerName;

    @Column(name="customer_person_name")
    private String customerPersonName;

    @Column(name="customer_tel")
    private String customerTel;
}

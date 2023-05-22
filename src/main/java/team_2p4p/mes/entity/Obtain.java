package team_2p4p.mes.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "obtain")
public class Obtain {

    @Id
    @Column(name="obtain_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long obtainId;                                  // 수주 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "item_id")
    private Item item;                                     // 제품 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "customer_id")
    private Customer customerId;                          // 고객 id

    @Column(nullable = false, name="obtain_amount")
    private Long obtainAmount;                            // 제품 수량

    @Column(nullable = false, name="obtain_date")
    private LocalDateTime obtainDate;                     // 수주 날짜

    @Column(name="customer_request_date")
    private LocalDateTime customerRequestDate;             // 납기일

    @Column(nullable = false, name="expect_date")
    private LocalDateTime expectDate;                   // 예상 납품일

    @Column(nullable = false, name="obtain_stat")
    private boolean obtainStat;                         // 수주확정

    @Column(name="obtain_stat_date")
    private LocalDateTime obtainStatDate;               // 수주확정일

}

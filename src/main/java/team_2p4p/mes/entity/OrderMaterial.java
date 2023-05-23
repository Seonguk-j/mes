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
@Table(name = "order_material")
public class OrderMaterial {

    @Id
    @Column(name="order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "enterprise_id")
    private Enterprise enterprise;

    @Column(nullable = false, name="order_item_amount")
    private Long orderItemAmount;

    @Column(nullable = false, name="order_date")
    private LocalDateTime orderDate;

    @Column(nullable = false, name="import_expect_date")
    private LocalDateTime ImportExpectDate;

    @Column(nullable = false, name="order_stat")
    private boolean orderStat;
}

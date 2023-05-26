package team_2p4p.mes.entity;

import com.querydsl.core.types.Order;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "lot_log")
public class LotLog {

    @Id
    @Column(name="lot_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotLogId;

    @Column(name="process")
    private String process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderMaterial orderMaterial;

    @Column(name="input_kind")
    private String inputKind;

    @Column(name="lot_stat")
    private boolean lotStat;

    @Column(name="input_time")
    private LocalDateTime inputTime;

    @Column(name="output_time")
    private LocalDateTime outputTime;

    @Column(name="lot")
    private String lot;

    @Column(name="lot_p_log_id")
    private Long lotPLogId1;

    @Column(name="lot_p_log_id2")
    private Long lotPLogId2;
}

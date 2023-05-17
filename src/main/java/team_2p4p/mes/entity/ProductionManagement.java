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
@Table(name = "production_management")
public class ProductionManagement {

    @Id
    @Column(name="production_management_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productionManagementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "obtain_id", referencedColumnName = "obtain_id")
    private Obtain obtain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "process_id", referencedColumnName = "process_id")
    private Process process;

    @Column(nullable = false, name="process_start_time")
    private LocalDateTime processStartTime;

    @Column(nullable = false, name="process_finish_time")
    private LocalDateTime processFinishTime;

    @Column(nullable = false, name="process_amount")
    private Long processAmount;

    @Column(nullable = false, name="process_stat")
    private int processStat;
}

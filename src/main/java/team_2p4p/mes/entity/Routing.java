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
@Table(name = "routing")
public class Routing {

    @Id
    @Column(name="routing_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process1", referencedColumnName = "process_id")
    private Process process1;

    @Column(name="process_time1")
    private int processTime1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process2", referencedColumnName = "process_id")
    private Process process2;

    @Column(name="process_time2")
    private int processTime2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process3", referencedColumnName = "process_id")
    private Process process3;

    @Column(name="process_time3")
    private int processTime3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process4", referencedColumnName = "process_id")
    private Process process4;

    @Column(name="process_time4")
    private int processTime4;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process5", referencedColumnName = "process_id")
    private Process process5;

    @Column(name="process_time5")
    private int processTime5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process6", referencedColumnName = "process_id")
    private Process process6;

    @Column(name="process_time6")
    private int processTime6;
}

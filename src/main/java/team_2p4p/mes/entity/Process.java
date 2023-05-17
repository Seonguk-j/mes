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
@Table(name = "process")
public class Process {
    @Id
    @Column(name="process_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long processId;

    @Column(nullable = false, name="process_name")
    private String processName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material1", referencedColumnName = "item_id")
    private Item item1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material2", referencedColumnName = "item_id")
    private Item item2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material3", referencedColumnName = "item_id")
    private Item item3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material4", referencedColumnName = "item_id")
    private Item item4;

    @Column(name="process_content")
    private String processContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id1", referencedColumnName = "facility_id")
    private Facility facility1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id2", referencedColumnName = "facility_id")
    private Facility facility2;
}

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
@Table(name = "facility")
public class Facility {

    @Id
    @Column(name="facility_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityId;

    @Column(name="facility_name")
    private String facilityName;

    @Column(name="item1")
    private String item1;

    @Column(name="item2")
    private String item2;

    @Column(name="item3")
    private String item3;

    @Column(name="item4")
    private String item4;

    @Column(name="facility_capa")
    private String facilityCapa;

    @Column(name="process_worktime")
    private Long processWorktime;

    @Column(name="process_redaytime")
    private Long processReadytime;
}

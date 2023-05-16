package team_2p4p.mes.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Obtain {

    @Id
    private long obtain_id;

    @Column(nullable = false)
    private long item_id;

    @Column(nullable = false)
    private long customer_id;

    @Column(nullable = false)
    private long obtain_amount;

    @Column(nullable = false)
    private LocalDateTime obtain_date;

    @Column
    private LocalDateTime customer_request_date;

    @Column
    private LocalDateTime expect_date;

    @Column(nullable = false)
    private boolean obtain_stat;

    @Column
    private LocalDateTime obtain_stat_date;




}

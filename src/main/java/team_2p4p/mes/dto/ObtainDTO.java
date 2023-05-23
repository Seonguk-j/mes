package team_2p4p.mes.dto;

import team_2p4p.mes.entity.Customer;
import team_2p4p.mes.entity.Item;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

public class ObtainDTO {

    private Long obtainId;
    private String itemId;
    private String customerId;
    private Long obtainAmount;
    private LocalDateTime obtainDate;
    private LocalDateTime customerRequestDate;
    private LocalDateTime expectDate;
    private boolean obtainStat;
    private LocalDateTime obtainStatDate;
}

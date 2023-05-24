package team_2p4p.mes.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ObtainDTO {

    private Long obtainId;
    private Long itemId;
    private Long customerId;
    private Long obtainAmount;
    private LocalDateTime obtainDate;
    private LocalDateTime customerRequestDate;
    private LocalDateTime expectDate;
    private boolean obtainStat;
    private LocalDateTime obtainStatDate;

}

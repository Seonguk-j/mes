package team_2p4p.mes.util.process;

import lombok.Data;
import team_2p4p.mes.util.calculator.MesAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
public class Measurement {

    public Measurement() {
        confirmList = new ArrayList<>();
    }

    boolean measurementStat = false; // 원료계량 진행중 유무

    //대기 유무

    LocalDateTime inputTime; // 원료계량 투입시간
    LocalDateTime outputTime; // 원료계량 나오는 시간

    List<MesAll> confirmList; //확정 리스트

    double amount; // 투입량
    int itemId;// 투입물종류 1,2,3,4

}

package team_2p4p.mes.util.process;

import team_2p4p.mes.util.calculator.MesAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Measurement { 

    public Measurement(){
        confirmList = new ArrayList<>();
    }
    boolean measurementStat = false; // 원료계량 진행중 유무

    //대기 유무

    LocalDateTime inputTime; // 원료계량 투입시간
    LocalDateTime outputTime; // 원료계량 나오는 시간

    List<MesAll> confirmList; //확정 리스트

    double amount; // 투입량
    int itemId;// 투입물종류 1,2,3,4

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public LocalDateTime getInputTime() {
        return inputTime;
    }

    public void setInputTime(LocalDateTime inputTime) {
        this.inputTime = inputTime;
    }

    public LocalDateTime getOutputTime() {
        return outputTime;
    }

    public void setOutputTime(LocalDateTime outputTime) {
        this.outputTime = outputTime;
    }

    public boolean isMeasurementStat() {
        return measurementStat;
    }

    public void setMeasurementStat(boolean measurementStat) {
        this.measurementStat = measurementStat;
    }


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public List<MesAll> getConfirmList() {
        return confirmList;
    }

    public void setConfirmList(List<MesAll> confirmList) {
        this.confirmList = confirmList;
    }
}

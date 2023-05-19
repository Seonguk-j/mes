package team_2p4p.mes.util.process;

import team_2p4p.mes.util.calculator.MesAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LiquidSystem {

    public LiquidSystem(){
        confirmList = new ArrayList<>();
    }
    boolean preProcessingStat = false; // 액체제조시스템 진행중 유무
    LocalDateTime inputTime; // 액체제조시스템 투입시간
    LocalDateTime outputTime; // 액체제조시스템 완료 시간
    List<MesAll> confirmList; //확정 리스트
    double amount; // 투입량
    long itemId;// 투입물종류 1,2,3,4


    public boolean isPreProcessingStat() {
        return preProcessingStat;
    }

    public void setPreProcessingStat(boolean preProcessingStat) {
        this.preProcessingStat = preProcessingStat;
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

    public List<MesAll> getConfirmList() {
        return confirmList;
    }

    public void setConfirmList(List<MesAll> confirmList) {
        this.confirmList = confirmList;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
}

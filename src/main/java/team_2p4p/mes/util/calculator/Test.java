package team_2p4p.mes.util.calculator;

import lombok.Data;
import org.thymeleaf.spring5.processor.SpringInputCheckboxFieldTagProcessor;
import team_2p4p.mes.util.process.LiquidSystem;
import team_2p4p.mes.util.process.Measurement;
import team_2p4p.mes.util.process.PreProcessing;

import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {

        Measurement measurement = new Measurement();
        PreProcessing preProcessing = new PreProcessing();
        LiquidSystem liquidSystem = new LiquidSystem();

        MesAll mesAll = new MesAll();
        MesAll mesAll2 = new MesAll();
        MesAll mesAll3 = new MesAll();
        Calculator cal = new Calculator();

        mesAll.setItemId(1);
        mesAll.setAmount(5000);
        mesAll.setTime(LocalDateTime.now());

        cal.materialMeasurement(mesAll,measurement);
        cal.preProcessing(mesAll,preProcessing);
        cal.operateLiquidSystem(mesAll,liquidSystem);

        mesAll.infoMeasurement();
        mesAll.infoPreProcessing();
        mesAll.infoLiquidSystem();

        measurement.getConfirmList().add(mesAll);
        preProcessing.getConfirmList().add(mesAll);
        liquidSystem.getConfirmList().add(mesAll);

        mesAll2.setItemId(2);
        mesAll2.setAmount(4700);
        mesAll2.setTime(LocalDateTime.now());

        cal.materialMeasurement(mesAll2,measurement);
        cal.preProcessing(mesAll2,preProcessing);
        cal.operateLiquidSystem(mesAll2,liquidSystem);

        mesAll2.infoMeasurement();
        mesAll2.infoPreProcessing();
        mesAll2.infoLiquidSystem();


        measurement.getConfirmList().add(mesAll2);
        preProcessing.getConfirmList().add(mesAll2);
        liquidSystem.getConfirmList().add(mesAll2);


        mesAll3.setItemId(3);
        mesAll3.setAmount(4300);
        mesAll3.setTime(LocalDateTime.now());

        cal.materialMeasurement(mesAll3,measurement);
        cal.operateLiquidSystem(mesAll3,liquidSystem);

        mesAll3.infoMeasurement();
        mesAll3.infoPreProcessing();
        mesAll3.infoLiquidSystem();



    }

}


package team_2p4p.mes.util.calculator;

import team_2p4p.mes.util.process.LiquidSystem;
import team_2p4p.mes.util.process.Measurement;
import team_2p4p.mes.util.process.PreProcessing;

import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {

        LocalDateTime now = LocalDateTime.now();
        Calculator cal = new Calculator();
        Measurement measurement = new Measurement();
        PreProcessing preProcessing = new PreProcessing();
        LiquidSystem liquidSystem1 = new LiquidSystem();
        LiquidSystem liquidSystem2 = new LiquidSystem();



        MesAll mesAll = CalcOrderMaterial.estimateDate(1L, 3000, now);

        System.out.println(mesAll.getAmount());
        System.out.println(mesAll.getTime());
        cal.materialMeasurement(mesAll,measurement);
        cal.preProcessing(mesAll,preProcessing);
        cal.operateLiquidSystem(mesAll,liquidSystem1,liquidSystem2);


        mesAll.infoMeasurement();
        System.out.println(mesAll.getPreProcessingMapList());
        System.out.println(mesAll.getLiquidSystemMapList());

        measurement.getConfirmList().add(mesAll);
        preProcessing.getConfirmList().add(mesAll);
        liquidSystem1.getConfirmList().add(mesAll);
        liquidSystem2.getConfirmList().add(mesAll);

        MesAll mesAll2 = CalcOrderMaterial.estimateDate(2L, 19000, now);
        cal.materialMeasurement(mesAll2,measurement);
        cal.preProcessing(mesAll2,preProcessing);
        cal.operateLiquidSystem(mesAll2,liquidSystem1,liquidSystem2);

        mesAll2.infoMeasurement();
        System.out.println(mesAll2.getPreProcessingMapList());
        System.out.println(mesAll2.getLiquidSystemMapList());

    }
}

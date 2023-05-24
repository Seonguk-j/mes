package team_2p4p.mes.util.calculator;

import org.hibernate.annotations.Check;
import team_2p4p.mes.util.process.*;

import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        MesAll obtain1 = new MesAll();
        MesAll obtain2 = new MesAll();

        Measurement measurement = new Measurement();
        PreProcessing preProcessing = new PreProcessing();
        LiquidSystem liquidSystem = new LiquidSystem();
        FillPouchProcessing fillPouchProcessing = new FillPouchProcessing();
        FillStickProcessing fillStickProcessing = new FillStickProcessing();
        CheckProcessing checkProcessing = new CheckProcessing();
        Packing packing = new Packing();
        Calculator cal = new Calculator();

        obtain1 = CalcOrderMaterial.estimateDate(1L, 3000, LocalDateTime.now());

        cal.obtain(obtain1,measurement,preProcessing,liquidSystem,fillPouchProcessing,fillStickProcessing,checkProcessing,packing);
        obtain1.infoAll();

    }
}


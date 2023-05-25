package team_2p4p.mes.util.calculator;

import org.hibernate.annotations.Check;
import team_2p4p.mes.util.process.*;

import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        MesAll obtain1 = new MesAll();
        Calculator cal = new Calculator();
        MesAll obtain2 = new MesAll();
        obtain1 = CalcOrderMaterial.estimateDate(1L, 3000, LocalDateTime.now());
        obtain2 = CalcOrderMaterial.estimateDate(1L, 3000, LocalDateTime.now());

        cal.obtain(obtain1);
        cal.confirmObtain(obtain1);
        obtain1.infoAll();
        cal.obtain(obtain2);
        obtain2.infoAll();

    }
}


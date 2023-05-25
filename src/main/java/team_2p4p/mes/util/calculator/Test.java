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
        obtain2 = CalcOrderMaterial.estimateDate(2L, 2000, LocalDateTime.now());
        MesAll obtain3 = CalcOrderMaterial.estimateDate(3L, 2000, LocalDateTime.now());
        MesAll obtain4 = CalcOrderMaterial.estimateDate(4L, 2000, LocalDateTime.now());

        cal.obtain(obtain1);
        cal.confirmObtain(obtain1);
        cal.obtain(obtain2);
        cal.confirmObtain(obtain2);
        cal.obtain(obtain3);
        cal.confirmObtain(obtain3);
        cal.obtain(obtain4);

        obtain1.infoAll();
        obtain2.infoAll();
        obtain3.infoAll();
        obtain4.infoAll();


    }
}


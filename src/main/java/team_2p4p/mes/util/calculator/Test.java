//package team_2p4p.mes.util.calculator;
//
//import lombok.RequiredArgsConstructor;
//import org.hibernate.annotations.Check;
//import org.springframework.beans.factory.annotation.Autowired;
//import team_2p4p.mes.util.process.*;
//
//import java.time.LocalDateTime;
//@RequiredArgsConstructor
//public class Test {
//    private final CalcOrderMaterial calcOrderMaterial;
//
//    public static void main(String[] args) {
//        MesAll obtain1 = new MesAll();
//        Calculator cal = new Calculator();
//        MesAll obtain2 = new MesAll();
//        obtain1 = calcOrderMaterial.estimateDate(1L, 3000, LocalDateTime.now());
//        obtain2 = calcOrderMaterial.estimateDate(1L, 3000, LocalDateTime.now());
//
//        cal.obtain(obtain1);
//        cal.confirmObtain(obtain1);
//        obtain1.infoAll();
//        cal.obtain(obtain2);
//        obtain2.infoAll();
//
//    }
//}
//

package team_2p4p.mes.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team_2p4p.mes.dto.ObtainDTO;
import team_2p4p.mes.entity.Obtain;
import team_2p4p.mes.repository.ObtainRepository;
import team_2p4p.mes.util.calculator.Calculator;
import team_2p4p.mes.util.calculator.MesAll;

import java.util.Optional;

@SpringBootTest
public class ProductionManagementServiceTest {


    @Autowired
    private ProductionManagementService productionManagementService;
    @Autowired LotLogService lotLogService;
    @Autowired ProductService productService;

    Calculator cal = new Calculator();


    @Test
    public void confirm(){ // 생산계획생성후 수주테이블에 확정시간찍기
        ObtainDTO obtainDTO = new ObtainDTO();
        obtainDTO.setObtainId(5L);
        productionManagementService.confirmAndAddProductionManagement(obtainDTO);
    }

    @Test
    public void lot(){
        ObtainDTO obtainDTO = new ObtainDTO();
        obtainDTO.setObtainId(1L);
        lotLogService.recordLot(obtainDTO);
    }

//    @Test
//    public void product(){
//        ObtainDTO obtainDTO = new ObtainDTO();
//        obtainDTO.setObtainId(1L);
//        productService.regProduct(obtainDTO);
//    }

}

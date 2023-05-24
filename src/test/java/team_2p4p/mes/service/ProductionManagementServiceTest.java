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

    Calculator cal = new Calculator();

    @Test
    public void setProductionPlanTest(){

        ObtainDTO obtainDTO = new ObtainDTO();
        obtainDTO.setObtainId(6L);

        productionManagementService.addProductionManagement(obtainDTO);
    }

    @Test
    public void confirmAll(){

    }


}

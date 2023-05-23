package team_2p4p.mes.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team_2p4p.mes.entity.Obtain;
import team_2p4p.mes.repository.ObtainRepository;
import team_2p4p.mes.util.calculator.MesAll;

import java.util.Optional;

@SpringBootTest
public class ProductionManagementServiceTest {


    @Autowired
    private ObtainService obtainService;
    @Autowired
    private ProductionManagementService productionManagementService;
    @Autowired
    private ObtainRepository obtainRepository;


}

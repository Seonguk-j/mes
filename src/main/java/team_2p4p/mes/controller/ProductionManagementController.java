package team_2p4p.mes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team_2p4p.mes.entity.OrderMaterial;
import team_2p4p.mes.entity.ProductionManagement;
import team_2p4p.mes.repository.OrderMaterialRepository;
import team_2p4p.mes.repository.ProductionManagementRepository;
import team_2p4p.mes.service.ProductionManagementService;

import java.util.List;

;


@RestController
@RequestMapping("juicyfresh/")
@RequiredArgsConstructor
public class ProductionManagementController {



    private final ProductionManagementService productionManagementService;


    @GetMapping("/productionschedule/list")
    public List<ProductionManagement> productionManagementList(){
        return productionManagementService.classification0();
    }

}

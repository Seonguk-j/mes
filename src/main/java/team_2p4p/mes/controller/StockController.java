package team_2p4p.mes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team_2p4p.mes.entity.Material;
import team_2p4p.mes.entity.Product;
import team_2p4p.mes.entity.ProductionManagement;
import team_2p4p.mes.repository.MaterialRepository;
import team_2p4p.mes.repository.ProductRepository;
import team_2p4p.mes.service.ProductionManagementService;

import java.util.List;

@RestController
@RequestMapping("juicyfresh/")
@RequiredArgsConstructor
public class StockController {

    private final ProductRepository productRepository;

    private final MaterialRepository materialRepository;

    //완제품 조회
    @GetMapping("/product/list")
    public List<Product> productList(){
        return productRepository.findAll();
    }

    //원자재 조회
    @GetMapping("/material/list")
    public List<Material> materialList(){
        return materialRepository.findAll();
    }

}

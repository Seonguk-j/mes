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
public class    StockController {

    private final ProductRepository productRepository;

    private final MaterialRepository materialRepository;

    //완제품 입출
    @GetMapping("/product/list")
    public List<Product> productList(){
        return productRepository.findAll();
    }

    //원자재 입출
    @GetMapping("/material/list")
    public List<Material> materialList(){
        return materialRepository.findInputOutput();
    }

    @GetMapping("/product/stock")
    public Long[] productStock(){
        Long[] stocks = {0L, 0L, 0L, 0L};
        for (long i = 0; i < 4; i++) {
             List<Product> productList = productRepository.findStock(i);
            if(!productList.isEmpty()) {
                for (Product product : productList) {
                    stocks[(int) i] += product.getProductStock();
                }
            }
        }
        System.out.println("재고 확인용 : " + stocks.toString());
        return stocks;
    }

    @GetMapping("/material/stock")
    public Long[] materialStock(){
        Long[] stocks = {0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L};
        for (long i = 9; i < 13; i++) {
            List<Material> materialList = materialRepository.findStock(i);
            if(!materialList.isEmpty()) {
                for (Material material : materialList) {
                    if(material.getMaterialStat() == 0)
                        stocks[(int) (i - 9)] += material.getMaterialStock();
                    else if (material.getMaterialStat() == 2) {
                        stocks[(int) (i - 9)] -= material.getMaterialStock();
                    }
                }
            }
        }
        for (long i = 14; i < 18; i++) {
            List<Material> materialList = materialRepository.findStock(i);
            if(!materialList.isEmpty()) {
                for (Material material : materialList) {
                    if(material.getMaterialStat() == 0)
                        stocks[(int) (i - 10)] += material.getMaterialStock();
                    else if (material.getMaterialStat() == 2) {
                        stocks[(int) (i - 10)] -= material.getMaterialStock();
                    }
                }
            }
        }
        System.out.println("재고 확인용 : " + stocks.toString());
        return stocks;
    }
}

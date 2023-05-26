package team_2p4p.mes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.entity.Lot;
import team_2p4p.mes.entity.Product;
import team_2p4p.mes.repository.ProductRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ProductService {
    private final ProductRepository productRepository;

    public Long productStock(Long itemId) {
        List<Product> productList = productRepository.findByItemItemId(itemId);
        System.out.println("크기 : " + productList.size());
        Long stock = 0L;
        if(!productList.isEmpty()) {
            for (Product product : productList) {
                if (!product.isExportStat())
                    stock += product.getProductStock();
                else
                    stock -= product.getProductStock();
            }
        }
        System.out.println("양 : " + stock);
        return stock;
    }

    public Product addMinusProductStock(Item item, Long stock, LocalDateTime makeDate, Lot lot) {
        Product product = new Product(null, item, stock, makeDate, true, lot);
        return productRepository.save(product);
    }

    public Product lastStock(Long itemId) {
        List<Product> productList = productRepository.findByItemItemId(itemId);
        return productList.isEmpty()? null :productList.get(productList.size() - 1);
    }
}

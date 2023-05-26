package team_2p4p.mes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import team_2p4p.mes.entity.Material;
import team_2p4p.mes.entity.OrderMaterial;
import team_2p4p.mes.repository.MaterialRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MaterialService {
    private final MaterialRepository materialRepository;

    public Long stockMaterialAmount(Long itemId) {
        List<Material> materialList = materialRepository.findByItemItemId(itemId);
        Long stock = 0L;
        for(Material material : materialList) {
            if(material.getMaterialStat() >= 1) {
                stock -= material.getMaterialStock();
            }
            else {
                stock += material.getMaterialStock();
            }
        }
        return stock;
    }

    // 자재 발주시 재고 확인용
    public Material useStockMaterial(Long itemId, Long stock, LocalDateTime now) {
        Material material = new Material(null, findLatestMaterial(itemId).getOrderMaterial(), findLatestMaterial(itemId).getItem(), stock, now, 1);
        return materialRepository.save(material);
    }

    public Material findLatestMaterial(Long itemId) {
        List<Material> materialList = materialRepository.findByItemItemId(itemId);
        if(materialList.isEmpty())
            return null;
        return materialList.get(materialList.size() - 1);
    }

    public Material addInputMaterial(OrderMaterial orderMaterial){
        Material material = new Material(null, orderMaterial, orderMaterial.getItem(), orderMaterial.getOrderItemAmount(), orderMaterial.getImportExpectDate(),0);
        return materialRepository.save(material);
    }

    // 제품 생산시 재고 소진용
    public  Material useMaterial(){
        Material material;
        if(materialRepository.findByMaterialStatAndItemItemId(1, itemId)){
            for (Material mt : materialRepository.findByMaterialStatAndItemItemId(1, itemId)){
                if (mt.getMaterialStock() == amount) {
                    material = mt;
                    material.updateMaterial(2, LocalDateTime.now());
                    return materialRepository.save(material);
                }
            }
        }
        material = new Material(null, )
    }
}

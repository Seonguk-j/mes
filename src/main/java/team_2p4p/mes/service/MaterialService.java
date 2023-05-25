package team_2p4p.mes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import team_2p4p.mes.entity.Material;
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
            if(material.isMaterialStat()) {
                stock -= material.getMaterialStock();
            }
            else {
                stock += material.getMaterialStock();
            }
        }
        return stock;
    }

    public Material useStockMaterial(Long itemId, Long stock, LocalDateTime now) {
        Material material = new Material(null, findLatestMaterial(itemId).getOrderMaterial(), findLatestMaterial(itemId).getItem(), stock, now, true);
        return materialRepository.save(material);
    }

    public Material findLatestMaterial(Long itemId) {
        List<Material> materialList = materialRepository.findByItemItemId(itemId);
        return materialList.get(materialList.size() - 1);
    }
}

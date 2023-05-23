package team_2p4p.mes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import team_2p4p.mes.entity.OrderMaterial;
import team_2p4p.mes.repository.OrderMaterialRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class OrderMaterialService {
    private final OrderMaterialRepository orderMaterialRepository;

    public List<OrderMaterial> orderMaterialList = orderMaterialRepository.findAll();
}

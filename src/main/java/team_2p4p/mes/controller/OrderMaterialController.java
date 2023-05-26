package team_2p4p.mes.controller;

import lombok.RequiredArgsConstructor;
;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team_2p4p.mes.entity.*;
import team_2p4p.mes.repository.*;
import team_2p4p.mes.dto.OrderMaterialDTO;
import team_2p4p.mes.service.OrderMaterialService;


import java.util.List;


@RestController
@RequestMapping("juicyfresh/")
@RequiredArgsConstructor
public class OrderMaterialController {



    private final OrderMaterialRepository orderMaterialRepository;


    @GetMapping("/OrderMaterial/list")
    public List<OrderMaterial> OrderMaterials(){
        List<OrderMaterial> OrderMaterialList = orderMaterialRepository.findAll();
        return OrderMaterialList;


    }

}

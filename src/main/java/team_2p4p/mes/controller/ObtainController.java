package team_2p4p.mes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team_2p4p.mes.entity.Process;
import team_2p4p.mes.entity.*;
import team_2p4p.mes.repository.*;
import team_2p4p.mes.service.ObtainService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("juicyfresh/")
@RequiredArgsConstructor
public class ObtainController {
    private final ObtainService obtainService;

    private final ObtainRepository obtainRepository;

//    @PostMapping(value = "obtain/add")
//    public List obtainAdd(){
//        List<Obtain> obtainList = obtainRepository.findAll();
//        return obtainList;
//    }
    @GetMapping(value = "obtain/list")
    public List<Obtain> obtains(){
        List<Obtain> obtainList = obtainRepository.findAll();
        return obtainList;
    }
}

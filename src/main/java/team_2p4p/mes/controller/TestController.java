package team_2p4p.mes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team_2p4p.mes.entity.Facility;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.entity.Process;
import team_2p4p.mes.repository.FacilityRepository;
import team_2p4p.mes.repository.ItemRepository;
import team_2p4p.mes.repository.ProcessRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("juicyfresh/")
public class TestController {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    FacilityRepository facilityRepository;

    @Autowired
    ProcessRepository processRepository;

    //juicyfreash
    @GetMapping("test")
    public List<Item> test(){

        Pageable pageable = PageRequest.of(0,50);

        Page<Item> all = itemRepository.findAll(pageable);

        return all.get().collect(Collectors.toList());
    }

    @GetMapping("facility")
    public List<Facility> facility(){

        List<Facility> list = facilityRepository.findAll();
        return list;
    }

    @GetMapping("process")
    public List<Process> process(){

        List<Process> list = processRepository.findAll();
        return list;
    }


}

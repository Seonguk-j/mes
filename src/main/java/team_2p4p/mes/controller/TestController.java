package team_2p4p.mes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.repository.ItemRepository;

import java.util.List;

@RestController
@RequestMapping("juicyfreash/")
public class TestController {

    @Autowired
    ItemRepository itemRepository;

    //juicyfreash
    @GetMapping("test")
    public List<Item> test(){

        List<Item> list = itemRepository.findAll();
        return list;
    }


}

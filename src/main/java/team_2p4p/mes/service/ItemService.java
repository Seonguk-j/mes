package team_2p4p.mes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.repository.ItemRepository;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    public void saveItem(String code, String name, String stat){
        Item item = Item.builder()
                .itemCode(code)
                .itemName(name)
                .itemStat(stat)
                .build();
        System.out.println(item);
        itemRepository.save(item);
    }

    public Item findItem(String name){
    return  itemRepository.findByItemName(name);
    }
}

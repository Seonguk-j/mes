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

    public Item itemFindbyId(Long id){
        return itemRepository.findById(id).orElseThrow(() ->new IllegalArgumentException("아이템 데이터가 존재하지 않습니다"));
    }
}

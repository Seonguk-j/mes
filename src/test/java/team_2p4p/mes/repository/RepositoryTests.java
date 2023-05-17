package team_2p4p.mes.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team_2p4p.mes.entity.Item;

import java.util.List;

@SpringBootTest
public class RepositoryTests {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void test(){
        Item item = Item.builder()
                .itemCode("CP")
                .itemName("흑마늘즙")
                .itemStat(1)
                .build();
        System.out.println(item);
        itemRepository.save(item);
    }

    @Test
    public void test2() {
        itemRepository.deleteByItemCode("CP");
    }

    @Test
    public void findAll() {
        List<Item> list = itemRepository.findAll();
        System.out.println(list);
    }

}

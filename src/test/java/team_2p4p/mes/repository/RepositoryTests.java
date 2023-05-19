package team_2p4p.mes.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.service.FacilityService;
import team_2p4p.mes.service.ItemService;

import java.util.List;

@SpringBootTest
public class RepositoryTests {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private FacilityService facilityService;
    @Test
    public void ItemInsert(){
        itemService.saveItem("CP-001", "양배추즙", 2 );
        itemService.saveItem("CP-002", "흑마늘즙", 2 );
        itemService.saveItem("CP-003", "매실 젤리스틱", 2 );
        itemService.saveItem("CP-004", "매실 젤리스틱", 2 );
        
        itemService.saveItem("SP-001", "양배추 추출액상 반제품", 1);
        itemService.saveItem("SP-002", "흑마늘 추출액상 반제품", 1);
        itemService.saveItem("SP-003", "석류젤리 반제품", 1);
        itemService.saveItem("SP-004", "매실젤리 반제품", 1);

        itemService.saveItem("OP-001", "양배추", 0);
        itemService.saveItem("OP-002", "흑마늘", 0);
        itemService.saveItem("OP-003", "석류농축액", 0);
        itemService.saveItem("OP-004", "매실농축액", 0);
        itemService.saveItem("OP-005", "정제수", 0);
        itemService.saveItem("OP-006", "콜라겐", 0);
        itemService.saveItem("OP-007", "포장지", 0);
        itemService.saveItem("OP-008", "박스", 0);
    }

    @Test
    public void facility(){
    Item item = itemService.findItem("양배추");
//        System.out.println(item.getItemName());
        facilityService.saveFacility("세척탱크, 절단기", "1ton/h", 20,itemService.findItem("양배추"),itemService.findItem("흑마늘"), null, null);
        facilityService.saveFacility("추출기", "2000L", 20, itemService.findItem("양배추"),itemService.findItem("흑마늘"), itemService.findItem("정제수"), null);
        facilityService.saveFacility("추출기", "2000L", 20, itemService.findItem("양배추"),itemService.findItem("흑마늘"), itemService.findItem("정제수"), null);
        facilityService.saveFacility("저장탱크", "2000L", 20, itemService.findItem("석류농축액"),itemService.findItem("매실농축액"), itemService.findItem("정제수"), itemService.findItem("콜라겐"));
        facilityService.saveFacility("저장탱크", "2000L", 20, itemService.findItem("석류농축액"),itemService.findItem("매실농축액"), itemService.findItem("정제수"), itemService.findItem("콜라겐"));
        facilityService.saveFacility("스틱충진기 1열", "1500ea/1h",20,itemService.findItem("포장지"),itemService.findItem("석류젤리 반제품"),itemService.findItem("매실젤리 반제품"),null);
        facilityService.saveFacility("스틱충진기 2열", "1500ea/1h",20,itemService.findItem("포장지"),itemService.findItem("석류젤리 반제품"),itemService.findItem("매실젤리 반제품"),null);
        facilityService.saveFacility("파우치포장 1열", "1750ea/1h",20,itemService.findItem("포장지"),itemService.findItem("양배추 추출액상 반제품"),itemService.findItem("흑마늘 추출액상 반제품"),null);
        facilityService.saveFacility("파우치포장 2열", "1750ea/1h",20,itemService.findItem("포장지"),itemService.findItem("양배추 추출액상 반제품"),itemService.findItem("흑마늘 추출액상 반제품"),null);
        facilityService.saveFacility("금속검출기", "5000/1h",10,itemService.findItem("포장지"),itemService.findItem("양배추 추출액상 반제품"),itemService.findItem("흑마늘 추출액상 반제품"),null);
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

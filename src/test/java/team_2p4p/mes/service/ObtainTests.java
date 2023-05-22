package team_2p4p.mes.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team_2p4p.mes.entity.Customer;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.repository.CustomerRepository;
import team_2p4p.mes.repository.ItemRepository;

import java.time.LocalDateTime;

@SpringBootTest
public class ObtainTests {

    @Autowired
    private ObtainService obtainService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void regObtainTest(){
        Item item = itemRepository.findById(1L).orElseThrow();
        Customer customer = customerRepository.findById(1L).orElseThrow();

        obtainService.registerObtain(item, customer,3000, LocalDateTime.now().plusDays(10));

    }
}

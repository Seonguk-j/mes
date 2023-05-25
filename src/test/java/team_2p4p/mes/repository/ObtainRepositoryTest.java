package team_2p4p.mes.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import team_2p4p.mes.dto.ObtainDTO;
import team_2p4p.mes.entity.Customer;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.entity.Obtain;
import team_2p4p.mes.service.CustomerService;
import team_2p4p.mes.service.ObtainService;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ObtainRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;


    @Autowired
    private ObtainRepository obtainRepository;

    @Autowired
    private ObtainService obtainService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public  void test() {

    }

    @Test void test2(){
        Item item = new Item();
        item.setItemId(1L);
        item.setItemCode("Cp-1");
        item.setItemName("양배추즙");
        item.setItemStat("완제품");
        itemRepository.save(item);
        System.out.println(item);


        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCustomerName("테스트");
        customer.setItem1(item);

        customerRepository.save(customer);

        System.out.println(customer);

        System.out.println(customerRepository.findByItem1OrItem2(item));


        ObtainDTO obtainDTO = new ObtainDTO();
        obtainDTO.setObtainId(1L);
        obtainDTO.setItemId(1L);
        obtainDTO.setItemName("양배추즙");
        obtainDTO.setObtainAmount(1000L);
        obtainDTO.setCustomerRequestDate(LocalDateTime.now());


        obtainDTO.setCustomerId(customer.getCustomerId());
        obtainService.regObtain(obtainDTO);

    }


}

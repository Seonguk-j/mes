package team_2p4p.mes.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team_2p4p.mes.dto.ObtainDTO;
import team_2p4p.mes.entity.Customer;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.entity.Obtain;
import team_2p4p.mes.repository.CustomerRepository;
import team_2p4p.mes.repository.ItemRepository;
import team_2p4p.mes.repository.ObtainRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class ObtainTests {

    @Autowired
    private ObtainService obtainService;
    @Autowired
    private ObtainRepository obtainRepository;

    @Test
    public void regObtainTest(){
        ObtainDTO dto = new ObtainDTO();
        dto.setItemId(1L);
        dto.setCustomerRequestDate(LocalDateTime.now().plusDays(10));
        dto.setObtainAmount(3000L);
        dto.setCustomerId(1L);

        obtainService.regObtain(dto);
    }

    @Test
    public void confirmTest(){
        Optional<Obtain> res = obtainRepository.findById(1L);

        if(res.isPresent()){
            Obtain obtain = res.get();
            obtain.updateStat();
            obtainRepository.save(obtain);
        }


    }


}

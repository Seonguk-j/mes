package team_2p4p.mes.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team_2p4p.mes.dto.ObtainDTO;


import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class ObtainTests {

    @Autowired
    private ObtainService obtainService;

    @Test
    public void regObtainTest(){
        ObtainDTO dto = new ObtainDTO();
        dto.setItemId(2L);
        dto.setCustomerRequestDate(LocalDateTime.now().plusDays(10));
        dto.setObtainAmount(1200L);
        dto.setCustomerId(1L);

        obtainService.regObtain(dto);
    }

    @Test
    public void confirmTest(){
        ObtainDTO dto = new ObtainDTO();
        dto.setObtainId(6L);

        obtainService.confirmObtain(dto);
    }


    @Test
    public void tttest(){
        System.out.println("test");
    }
}

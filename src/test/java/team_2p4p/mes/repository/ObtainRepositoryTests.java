package team_2p4p.mes.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.entity.Obtain;
import team_2p4p.mes.service.*;

import java.util.List;

@SpringBootTest
public class ObtainRepositoryTests {

   @Autowired
   ObtainRepository obtainRepository;

    @Test
    public void obtains(){
        List<Obtain> obtainList = obtainRepository.findAll();
        System.out.println(obtainList);

    }
}

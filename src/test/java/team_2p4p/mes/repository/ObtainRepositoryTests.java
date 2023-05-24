package team_2p4p.mes.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team_2p4p.mes.entity.Obtain;

import java.util.Optional;

@SpringBootTest
public class ObtainRepositoryTests {

    @Autowired
    private ObtainRepository obtainRepository;

    @Test
    public void confirmTest(){

        Obtain obtain = obtainRepository.findById(1L).orElseThrow();
        obtain.updateStat();
        obtain.updateConfirmTime();
        obtainRepository.save(obtain);

    }


}

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

        Optional<Obtain> res = obtainRepository.findById(10L);
        if(res.isPresent()){
            Obtain obtain = res.get();
            obtain.updateStat();
            obtainRepository.save(obtain);
        }
    }



}

package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team_2p4p.mes.entity.Obtain;

import java.util.List;

public interface ObtainRepository extends JpaRepository<Obtain,Long> {

    List<Obtain> findByObtainStat(boolean confirm);
}

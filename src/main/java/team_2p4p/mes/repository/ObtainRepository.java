package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import team_2p4p.mes.entity.Obtain;

public interface ObtainRepository extends JpaRepository<Obtain,Long>,
        QuerydslPredicateExecutor<Obtain>, ObtainRepositoryCustom {

}

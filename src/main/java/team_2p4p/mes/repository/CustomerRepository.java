package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team_2p4p.mes.entity.Customer;
import team_2p4p.mes.entity.Obtain;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

}

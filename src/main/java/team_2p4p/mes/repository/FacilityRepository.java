package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team_2p4p.mes.entity.Facility;
import team_2p4p.mes.entity.Item;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

}

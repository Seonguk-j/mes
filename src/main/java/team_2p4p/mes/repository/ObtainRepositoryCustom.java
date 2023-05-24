package team_2p4p.mes.repository;


import org.springframework.data.domain.Page;
import team_2p4p.mes.dto.SearchDto;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.entity.Obtain;

import java.util.List;

public interface ObtainRepositoryCustom {

    List<Obtain> getObtainList(SearchDto searchDto);
}

package team_2p4p.mes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team_2p4p.mes.dto.ObtainDTO;
import team_2p4p.mes.dto.SearchDTO;
import team_2p4p.mes.entity.Process;
import team_2p4p.mes.entity.*;
import team_2p4p.mes.repository.*;
import team_2p4p.mes.service.ObtainService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("juicyfresh/")
@RequiredArgsConstructor
public class ObtainController {
    private final ObtainService obtainService;

    private final ObtainRepository obtainRepository;

    @PostMapping("/obtain/add")
    public void obtainAdd(@RequestBody ObtainDTO obtainDTO, Model model) {
        try {
            // ObtainDTO에서 필요한 데이터 추출
        obtainService.regObtain(obtainDTO);
            // 필요한 로직 수행


            // 처리 결과 반환

        } catch (IllegalStateException e) {
            // 예외 처리
            // ...
        }

    }
    @GetMapping("/obtain/list")
    public List<Obtain> obtains(){
        List<Obtain> obtainList = obtainRepository.findAll();
        return obtainList;
    }
    @PostMapping("/obtain/search/list")
    public List<Obtain> obtainSearch(@RequestBody SearchDTO searchDTO){
        System.out.println("searchBy; "+ searchDTO.getSearchBy());
        System.out.println("검색 결과:" + obtainService.getObtainList(searchDTO));
        return obtainService.getObtainList(searchDTO);
    }
}


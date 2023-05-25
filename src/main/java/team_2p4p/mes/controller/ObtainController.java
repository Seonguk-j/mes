package team_2p4p.mes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team_2p4p.mes.dto.ObtainDTO;
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
            System.out.println(obtainDTO);

        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

    }
    @GetMapping("/obtain/list")
    public List<Obtain> obtains(){
        List<Obtain> obtainList = obtainRepository.findAll();
        return obtainList;
    }
}

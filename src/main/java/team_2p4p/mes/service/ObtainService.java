package team_2p4p.mes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import team_2p4p.mes.dto.ObtainDTO;
import team_2p4p.mes.entity.Customer;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.entity.Obtain;
import team_2p4p.mes.repository.CustomerRepository;
import team_2p4p.mes.repository.ItemRepository;
import team_2p4p.mes.repository.ObtainRepository;
import team_2p4p.mes.util.calculator.CalcOrderMaterial;
import team_2p4p.mes.util.calculator.Calculator;
import team_2p4p.mes.util.calculator.MesAll;
import team_2p4p.mes.util.process.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ObtainService {

    private final ObtainRepository obtainRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;

    Calculator cal = new Calculator();


    public void getConfirmList(){

        List<Obtain> list = obtainRepository.findByObtainStat(true);
        List<ObtainDTO> dtoList = new ArrayList<>();
        Factory factory = Factory.getInstance();

        for(int i = 0; i < list.size(); i++){
            if(i == 0){
                factory.getMeasurement().getConfirmList().clear();
                factory.getPreProcessing().getConfirmList().clear();
                factory.getLiquidSystem().getConfirmList().clear();
                factory.getFillPouchProcessing().getConfirmList().clear();
                factory.getFillPouchProcessing().getConfirmList().clear();
                factory.getCheckProcessing().getConfirmList().clear();
                factory.getPacking().getConfirmList().clear();
            }
            dtoList.add(entityToDto(list.get(i)));
            MesAll mesAll = CalcOrderMaterial.estimateDate(dtoList.get(i).getItemId(), Math.toIntExact(dtoList.get(i).getObtainAmount()), dtoList.get(i).getObtainStatDate());
            factory.getMeasurement().getConfirmList().add(mesAll);
            factory.getPreProcessing().getConfirmList().add(mesAll);
            factory.getLiquidSystem().getConfirmList().add(mesAll);
            factory.getFillPouchProcessing().getConfirmList().add(mesAll);
            factory.getFillPouchProcessing().getConfirmList().add(mesAll);
            factory.getCheckProcessing().getConfirmList().add(mesAll);
            factory.getPacking().getConfirmList().add(mesAll);
        }
    }

    public void regObtain(ObtainDTO dto){

        getConfirmList();

        LocalDateTime obtainDate = LocalDateTime.now();
        dto.setObtainStat(false);
        dto.setObtainDate(obtainDate);

        MesAll obtainInfo = CalcOrderMaterial.estimateDate(dto.getItemId(), Math.toIntExact(dto.getObtainAmount()), obtainDate);
        cal.obtain(obtainInfo);
        dto.setExpectDate(obtainInfo.getEstimateDate());
        Obtain obtain = dtoToEntity(dto);
        obtainRepository.save(obtain);
    }

    //여기서 부터 하면됨 db에서 값 들고와서 다시 계산하고 수주확정시키기
    public void confirmObtain(ObtainDTO dto){

        //수주확정시 다시계산해야 되고 dto에 확정시간 넣어줘야함
        getConfirmList();

        dto = entityToDto(obtainRepository.findById(dto.getObtainId()).orElseThrow());
        MesAll obtainInfo = CalcOrderMaterial.estimateDate(dto.getItemId(), Math.toIntExact(dto.getObtainAmount()), LocalDateTime.now());
        cal.obtain(obtainInfo);
        dto.setExpectDate(obtainInfo.getEstimateDate());

        Obtain entity = dtoToEntity(dto);
        entity.updateStat();
        entity.updateConfirmTime();

        obtainRepository.save(entity);

    }


    public Obtain dtoToEntity(ObtainDTO dto){



        Obtain entity = Obtain.builder()
                .obtainId(dto.getObtainId())
                .item(itemRepository.findById(dto.getItemId()).orElseThrow())
                .customerId(customerRepository.findById(dto.getCustomerId()).orElseThrow())
                .obtainAmount(dto.getObtainAmount())
                .obtainDate(dto.getObtainDate())
                .customerRequestDate(dto.getCustomerRequestDate())
                .expectDate(dto.getExpectDate())
                .obtainStat(dto.isObtainStat())
                .build();

        return entity;
    }

    public ObtainDTO entityToDto(Obtain entity){

        ObtainDTO dto = new ObtainDTO();
        dto.setObtainId(entity.getObtainId());
        dto.setItemId(entity.getItem().getItemId()); //객체에서 itemId 를 빼야됨
        dto.setCustomerId(entity.getCustomerId().getCustomerId());
        dto.setObtainAmount(entity.getObtainAmount());
        dto.setObtainDate(entity.getObtainDate());
        dto.setCustomerRequestDate(entity.getCustomerRequestDate());
        dto.setExpectDate(entity.getExpectDate());
        dto.setObtainStat(entity.isObtainStat());
        dto.setObtainStatDate(entity.getObtainStatDate());

        return dto;
    }


}

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

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ObtainService {

    private final ObtainRepository obtainRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;

    Measurement measurement = new Measurement();
    PreProcessing preProcessing = new PreProcessing();
    LiquidSystem liquidSystem = new LiquidSystem();
    FillPouchProcessing fillPouchProcessing = new FillPouchProcessing();
    FillStickProcessing fillStickProcessing = new FillStickProcessing();
    CheckProcessing checkProcessing = new CheckProcessing();
    Packing packing = new Packing();
    Calculator cal = new Calculator();



    public void regObtain(ObtainDTO dto){
        LocalDateTime obtainDate = LocalDateTime.now();
        dto.setObtainStat(false);
        dto.setObtainDate(obtainDate);

        MesAll obtainInfo = CalcOrderMaterial.estimateDate(dto.getItemId(), Math.toIntExact(dto.getObtainAmount()), obtainDate);
        cal.obtain(obtainInfo,measurement,preProcessing,liquidSystem,fillPouchProcessing,fillStickProcessing,checkProcessing,packing);
        dto.setExpectDate(obtainInfo.getEstimateDate());
        Obtain obtain = dtoToEntity(dto);
        obtainRepository.save(obtain);
    }

    //여기서 부터 하면됨 db에서 값 들고와서 다시 계산하고 수주확정시키기
    public void confirmObtain(Obtain obtain){
        obtain.updateStat();
        obtain.updateConfirmTime();
        obtainRepository.save(obtain);
    }


    private Obtain dtoToEntity(ObtainDTO dto){

        Obtain obtain = Obtain.builder()
                .item(itemRepository.findById(dto.getItemId()).orElseThrow())
                .customerId(customerRepository.findById(dto.getCustomerId()).orElseThrow())
                .obtainAmount(dto.getObtainAmount())
                .obtainDate(dto.getObtainDate())
                .customerRequestDate(dto.getCustomerRequestDate())
                .expectDate(dto.getExpectDate())
                .obtainStat(dto.isObtainStat())
                .build();

        return obtain;
    }

}

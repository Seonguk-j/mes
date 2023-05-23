package team_2p4p.mes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import team_2p4p.mes.entity.Customer;
import team_2p4p.mes.entity.Item;
import team_2p4p.mes.entity.Obtain;
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

    Measurement measurement = new Measurement();
    PreProcessing preProcessing = new PreProcessing();
    LiquidSystem liquidSystem = new LiquidSystem();
    FillPouchProcessing fillPouchProcessing = new FillPouchProcessing();
    FillStickProcessing fillStickProcessing = new FillStickProcessing();
    CheckProcessing checkProcessing = new CheckProcessing();
    Packing packing = new Packing();
    Calculator cal = new Calculator();

    public void registerObtain(Item item, Customer customer, long amount, LocalDateTime customerReqDate){

        LocalDateTime obtainDate = LocalDateTime.now();
        MesAll obtainInfo = CalcOrderMaterial.estimateDate(item.getItemId(), (int) amount, obtainDate);

        cal.obtain(obtainInfo,measurement,preProcessing,liquidSystem,fillPouchProcessing,fillStickProcessing,checkProcessing,packing);

        Obtain obtain = Obtain.builder()
                .item(item)
                .customerId(customer)
                .obtainAmount(amount)
                .obtainDate(obtainDate)
                .customerRequestDate(customerReqDate)
                .expectDate(obtainInfo.getEstimateDate())
                .obtainStat(false)
                .build();

        obtainRepository.save(obtain);
    }


    public void confirmObtain(Obtain obtain){


    }
}

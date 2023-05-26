package team_2p4p.mes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import team_2p4p.mes.dto.ItemDTO;
import team_2p4p.mes.dto.LotLogDTO;
import team_2p4p.mes.dto.ObtainDTO;
import team_2p4p.mes.entity.LotLog;
import team_2p4p.mes.entity.Obtain;
import team_2p4p.mes.entity.ProductionManagement;
import team_2p4p.mes.repository.ItemRepository;
import team_2p4p.mes.repository.LotLogRepository;
import team_2p4p.mes.repository.ObtainRepository;
import team_2p4p.mes.util.calculator.CalcOrderMaterial;
import team_2p4p.mes.util.calculator.Calculator;
import team_2p4p.mes.util.calculator.MesAll;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class LotLogService {

    private final LotLogRepository lotLogRepository;
    private final ObtainService obtainService;
    private final ObtainRepository obtainRepository;
    private final CalcOrderMaterial calcOrderMaterial;
    private final ItemService itemService;
    Calculator cal = new Calculator();

    public void recordLot(ObtainDTO dto){

        obtainService.getConfirmList();

        dto = obtainService.entityToDto(obtainRepository.findById(dto.getObtainId()).orElseThrow());
        // id로 해당 수주DTO를 찾아온다.
        MesAll mesAll = calcOrderMaterial.estimateDate(dto.getItemId(), Math.toIntExact(dto.getObtainAmount()), LocalDateTime.now());
        cal.obtain(mesAll);
        // mesAll에 db값을 꺼내서 해당 mesAll을 찾아온다.

        String dateString = "";
        // 발주 로트
        //itemid inputkind 안들어갔음
        LotLogDTO orderLot = new LotLogDTO();
        orderLot.setProcess("입고");
        orderLot.setItem(itemService.findItemById(dto.getItemId()));
        orderLot.setInputKind(orderLot.getItem().getItemName());
        orderLot.setItemId(orderLot.getItem().getItemId());
        orderLot.setLotStat(false);
        orderLot.setInputTime(mesAll.getTime());
        orderLot.setOutputTime(mesAll.getInputMeasurementTime().minusMinutes(20));
        dateString = (orderLot.getOutputTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"))).substring(2);
        orderLot.setLot("WH-"+dateString+"-"+(int)(mesAll.getAmount()));
        LotLog lotLog = dtoToEntity(orderLot);
        lotLogRepository.save(lotLog);

//        //1 원료개량
//        LotLogDTO measurementLot = new LotLogDTO();
//        measurementLot.setItem(dto.getItem());
//        measurementLot.setProcess("원료계량");
//        measurementLot.setInputKind(dto.getItemName());
//        measurementLot.setLotStat(false);
//        measurementLot.setInputTime(mesAll.getInputMeasurementTime());
//        measurementLot.setOutputTime(mesAll.getOutputMeasurementTime());
//        dateString = (orderLot.getOutputTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"))).substring(2);
//        measurementLot.setLot("MS-"+dateString+"-"+mesAll.getMeasurementAmount());



//        //2 전처리
//        for (int i = 0; i < mesAll.getPreProcessingCount(); i++) {
//            ProductionManagement preProcessLot = ProductionManagement.builder()
//                    .obtain(obtain)
//                    .processAmount((long) mesAll.getPreProcessingAmountList().get(i))
//                    .processStartTime(mesAll.getInputPreProcessingTimeList().get(i))
//                    .processFinishTime(mesAll.getOutputPreProcessingTimeList().get(i))
//                    .process("전처리")
//                    .build();
//            lotLogRepository.save(preProcessPlan);
//        }
//
//
//        // 3 액체제조 시스템1
//        for (int i = 0; i < mesAll.getLiquidSystemCount1(); i++) {
//            ProductionManagement liquidProcess1Lot = ProductionManagement.builder()
//                    .obtain(obtain)
//                    .processAmount((long) mesAll.getLiquidSystemInputAmountList1().get(i))
//                    .processStartTime(mesAll.getLiquidSystemInputTimeList1().get(i))
//                    .processFinishTime(mesAll.getLiquidSystemOutputTimeList1().get(i))
//                    .process("액체제조 시스템 1")
//                    .build();
//            lotLogRepository.save(liquidProcess1Plan);
//        }
//
//
//        // 4 액체제조 시스템2
//        for (int i = 0; i < mesAll.getLiquidSystemCount2(); i++) {
//            ProductionManagement liquidProcess2Lot = ProductionManagement.builder()
//                    .obtain(obtain)
//                    .processAmount((long) mesAll.getLiquidSystemInputAmountList2().get(i))
//                    .processStartTime(mesAll.getLiquidSystemInputTimeList2().get(i))
//                    .processFinishTime(mesAll.getLiquidSystemOutputTimeList2().get(i))
//                    .process("액체제조 시스템 2")
//                    .build();
//            lotLogRepository.save(liquidProcess2Plan);
//        }
//
//        // 5,6 충진
//        if (mesAll.getItemId() <= 2) {
//            for (int i = 0; i < mesAll.getFillPouchCount(); i++) {
//                ProductionManagement fillPouchProcessLot = ProductionManagement.builder()
//                        .obtain(obtain)
//                        .processAmount((long) mesAll.getFillPouchInputAmountList().get(i))
//                        .processStartTime(mesAll.getFillPouchInputTimeList().get(i))
//                        .processFinishTime(mesAll.getFillPouchOutputTimeList().get(i))
//                        .process("즙 충진기")
//                        .build();
//                lotLogRepository.save(fillPouchProcessPlan);
//            }
//        } else {
//            for (int i = 0; i < mesAll.getFillStickCount(); i++) {
//                ProductionManagement fillStickProcessLot = ProductionManagement.builder()
//                        .obtain(obtain)
//                        .processAmount((long) mesAll.getFillStickInputAmountList().get(i))
//                        .processStartTime(mesAll.getFillStickInputTimeList().get(i))
//                        .processFinishTime(mesAll.getFillStickOutputTimeList().get(i))
//                        .process("스틱 충진기")
//                        .build();
//                lotLogRepository.save(fillStickProcessPlan);
//
//            }
//        }
//
//
//        // 7 검사
//        for (int i = 0; i < mesAll.getCheckCount(); i++) {
//            System.out.println("검사?");
//            ProductionManagement checkProcessLot = ProductionManagement.builder()
//                    .obtain(obtain)
//                    .processAmount((long) mesAll.getCheckInputAmountList().get(i))
//                    .processStartTime(mesAll.getCheckInputTimeList().get(i))
//                    .processFinishTime(mesAll.getCheckOutputTimeList().get(i))
//                    .process("검사")
//                    .build();
//            lotLogRepository.save(checkProcessPlan);
//        }
//
//
//        // 8 포장
//        for (int i = 0; i < mesAll.getPackingCount(); i++) {
//            System.out.println("포장");
//            ProductionManagement packingProcessLot = ProductionManagement.builder()
//                    .obtain(obtain)
//                    .processAmount((long) mesAll.getPackingInputAmountList().get(i))
//                    .processStartTime(mesAll.getPackingInputTimeList().get(i))
//                    .processFinishTime(mesAll.getPackingOutputTimeList().get(i))
//                    .process("포장")
//                    .build();
//            lotLogRepository.save(packingProcessPlan);
//        }

    }


    public LotLog dtoToEntity(LotLogDTO dto){

        LotLog entity = LotLog.builder()
                .lotLogId(dto.getLotLogId())
                .process(dto.getProcess())
                .item(dto.getItem())
                .inputKind(dto.getInputKind())
                .lotStat(dto.isLotStat())
                .inputTime(dto.getInputTime())
                .outputTime(dto.getOutputTime())
                .lot(dto.getLot())
                .lotPLogId1(dto.getLotPLogId1())
                .lotPLogId2(dto.getLotPLogId2())
                .build();

        return entity;
    }

    public LotLogDTO entityToDto(LotLog entity){

        LotLogDTO dto = new LotLogDTO();
        dto.setLotLogId(entity.getLotLogId());
        dto.setProcess(entity.getProcess()); //객체에서 itemId 를 빼야됨
        dto.setItem(entity.getItem());
        dto.setInputKind(entity.getInputKind());
        dto.setLotStat(entity.isLotStat());
        dto.setInputTime(entity.getInputTime());
        dto.setOutputTime(entity.getOutputTime());
        dto.setLot(entity.getLot());
        dto.setLotPLogId1(entity.getLotPLogId1());
        dto.setLotPLogId2(entity.getLotPLogId2());

        return dto;
    }
}

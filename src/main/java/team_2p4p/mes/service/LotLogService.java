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
import team_2p4p.mes.repository.OrderMaterialRepository;
import team_2p4p.mes.util.calculator.CalcOrderMaterial;
import team_2p4p.mes.util.calculator.Calculator;
import team_2p4p.mes.util.calculator.MesAll;
import team_2p4p.mes.util.process.PreProcessing;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        calcOrderMaterial.test(dto.getItemId(), Math.toIntExact(dto.getObtainAmount()));
        cal.obtain(mesAll);
        // mesAll에 db값을 꺼내서 해당 mesAll을 찾아온다.

        String dateString = "";

        LotLog lotLog;

        // 입고 로트
        //itemid inputkind 안들어갔음
        LotLogDTO orderLot = new LotLogDTO();

        orderLot.setProcess("입고");
        orderLot.setItem(itemService.findItemById(dto.getItemId()));
        String wareHouseKind = (orderLot.getItem().getItemId()==1)?"양배추":(orderLot.getItem().getItemId()==2)?"흑마늘":(orderLot.getItem().getItemId()==3)?"석류농축액":"매실농축액";
        orderLot.setInputKind(wareHouseKind);
        orderLot.setItemId(orderLot.getItem().getItemId());
        orderLot.setLotStat(false);
        orderLot.setInputTime(mesAll.getTime());
        orderLot.setOutputTime(mesAll.getInputMeasurementTime().minusMinutes(20));
        dateString = (orderLot.getOutputTime().format(DateTimeFormatter.ofPattern("yyyyMMddhhmm"))).substring(2);
        orderLot.setLot("WH-"+dateString+"-"+(int)(mesAll.getAmount()));
        lotLog = dtoToEntity(orderLot);
        long warehouseId = lotLogRepository.save(lotLog).getLotLogId();




        //1 원료개량 로트
        LotLogDTO measurementLot = new LotLogDTO();
        measurementLot.setProcess("원료계량");
        measurementLot.setItem(itemService.findItemById(dto.getItemId()));
        String measurementKind = (orderLot.getItem().getItemId()==1)?"양배추":(orderLot.getItem().getItemId()==2)?"흑마늘":(orderLot.getItem().getItemId()==3)?"석류농축액":"매실농축액";
        measurementLot.setInputKind(measurementKind);
        measurementLot.setLotStat(false);
        measurementLot.setInputTime(mesAll.getInputMeasurementTime());
        measurementLot.setOutputTime(mesAll.getOutputMeasurementTime());
        dateString = (measurementLot.getOutputTime().format(DateTimeFormatter.ofPattern("yyyyMMddhhmm"))).substring(2);
        measurementLot.setLot("MS-"+dateString+"-"+(int)mesAll.getMeasurementAmount());
        measurementLot.setLotPLogId1(warehouseId);
        lotLog = dtoToEntity(measurementLot);
        long measurementId = lotLogRepository.save(lotLog).getLotLogId();


        List<Long> preProcessingLotIdList = new ArrayList<>();
        //2 전처리 로트
        String preInputKind = (orderLot.getItem().getItemId()==1)?"양배추":(orderLot.getItem().getItemId()==2)?"흑마늘":(orderLot.getItem().getItemId()==3)?"석류농축액":"매실농축액";
        for (int i = 0; i < mesAll.getPreProcessingCount(); i++) {
            LotLogDTO preProcessLot = new LotLogDTO();
            preProcessLot.setProcess("전처리");
            preProcessLot.setItem(itemService.findItemById(dto.getItemId()));
            preProcessLot.setInputKind(preInputKind);
            preProcessLot.setLotStat(false);
            preProcessLot.setInputTime(mesAll.getInputPreProcessingTimeList().get(i));
            preProcessLot.setOutputTime(mesAll.getOutputPreProcessingTimeList().get(i));
            dateString = (preProcessLot.getOutputTime().format(DateTimeFormatter.ofPattern("yyyyMMddhhmm"))).substring(2);
            preProcessLot.setLot("PP-"+dateString+"-"+mesAll.getPreProcessingAmountList().get(i));
            preProcessLot.setLotPLogId1(measurementId);
            lotLog = dtoToEntity(preProcessLot);
            preProcessingLotIdList.add(lotLogRepository.save(lotLog).getLotLogId());
        }


        List<Long> Liquid1LotIdList = new ArrayList<>();
        String liquid1Kind = (orderLot.getItem().getItemId()==1)?"양배추 추출액":(orderLot.getItem().getItemId()==2)?"흑마늘 추출액":(orderLot.getItem().getItemId()==3)?"석류농축액":"매실농축액";

        int liquidJ1 = 0;
        //3 액체제조 시스템1 로트
        for (int i = 0; i < mesAll.getLiquidSystemCount1(); i++) {
            LotLogDTO liquid1Lot = new LotLogDTO();
            liquid1Lot.setProcess("액체제조기1");
            liquid1Lot.setItem(itemService.findItemById(dto.getItemId()));
            liquid1Lot.setInputKind(liquid1Kind);
            liquid1Lot.setLotStat(false);
            liquid1Lot.setInputTime(mesAll.getLiquidSystemInputTimeList1().get(i));
            liquid1Lot.setOutputTime(mesAll.getLiquidSystemOutputTimeList1().get(i));
            dateString = (liquid1Lot.getOutputTime().format(DateTimeFormatter.ofPattern("yyyyMMddhhmm"))).substring(2);
            liquid1Lot.setLot("L1-"+dateString+"-"+(int)mesAll.getLiquidSystemOutputAmountList1().get(i));
            if(mesAll.getWhereList().get(0) == 1){
                //첫번째가 기계 1로 들어갔을때
                liquid1Lot.setLotPLogId1(preProcessingLotIdList.get(liquidJ1));
                liquidJ1 += 2;
            }else{
                liquid1Lot.setLotPLogId1(preProcessingLotIdList.get(liquidJ1+1));
                liquidJ1 += 2;
            }

            lotLog = dtoToEntity(liquid1Lot);
            Liquid1LotIdList.add(lotLogRepository.save(lotLog).getLotLogId());
        }



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

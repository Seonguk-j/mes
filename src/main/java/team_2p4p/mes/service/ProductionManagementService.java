package team_2p4p.mes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team_2p4p.mes.entity.Obtain;
import team_2p4p.mes.entity.ProductionManagement;
import team_2p4p.mes.repository.ProductionManagementRepository;
import team_2p4p.mes.util.calculator.MesAll;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductionManagementService {

    private final ProductionManagementRepository productionManagementRepository;

    void addProductionManagement(MesAll mesAll, Obtain obtain) {

        // 1 원료개량
        // 2 전처리
        // 3 액체제조시스템 1
        // 4 액체제조시스템 2
        // 5 파우치 충진기
        // 6 Stick 충진기
        // 7 검사
        // 8 포장


        //1 원료개량
        ProductionManagement measurementPlan = ProductionManagement.builder()
                .obtain(obtain)
                .processAmount((long) mesAll.getMeasurementAmount())
                .processStartTime(mesAll.getInputMeasurementTime())
                .processFinishTime(mesAll.getOutputMeasurementTime())
                .process(1L)
                .build();

        productionManagementRepository.save(measurementPlan);


        //2 전처리
        for (int i = 0; i < mesAll.getPreProcessingCount(); i++) {
            ProductionManagement preProcessPlan = ProductionManagement.builder()
                    .obtain(obtain)
                    .processAmount((long) mesAll.getPreProcessingAmountList().get(i))
                    .processStartTime(mesAll.getInputPreProcessingTimeList().get(i))
                    .processFinishTime(mesAll.getOutputPreProcessingTimeList().get(i))
                    .process(2L)
                    .build();
            productionManagementRepository.save(preProcessPlan);
        }


        // 3 액체제조 시스템1
        for (int i = 0; i < mesAll.getLiquidSystemCount1(); i++) {
            ProductionManagement liquidProcess1Plan = ProductionManagement.builder()
                    .obtain(obtain)
                    .processAmount((long) mesAll.getLiquidSystemInputAmountList1().get(i))
                    .processStartTime(mesAll.getLiquidSystemInputTimeList1().get(i))
                    .processFinishTime(mesAll.getLiquidSystemOutputTimeList1().get(i))
                    .process(3L)
                    .build();
            productionManagementRepository.save(liquidProcess1Plan);
        }


        // 4 액체제조 시스템2
        for (int i = 0; i < mesAll.getLiquidSystemCount2(); i++) {
            ProductionManagement liquidProcess2Plan = ProductionManagement.builder()
                    .obtain(obtain)
                    .processAmount((long) mesAll.getLiquidSystemInputAmountList2().get(i))
                    .processStartTime(mesAll.getLiquidSystemInputTimeList2().get(i))
                    .processFinishTime(mesAll.getLiquidSystemOutputTimeList2().get(i))
                    .process(4L)
                    .build();
            productionManagementRepository.save(liquidProcess2Plan);
        }

        // 5,6 충진
        if (mesAll.getItemId() <= 2) {
            for (int i = 0; i < mesAll.getFillPouchCount(); i++) {
                ProductionManagement fillPouchProcessPlan = ProductionManagement.builder()
                        .obtain(obtain)
                        .processAmount((long) mesAll.getFillPouchInputAmountList().get(i))
                        .processStartTime(mesAll.getFillPouchInputTimeList().get(i))
                        .processFinishTime(mesAll.getFillPouchOutputTimeList().get(i))
                        .process(5L)
                        .build();
                productionManagementRepository.save(fillPouchProcessPlan);
            }
        } else {
            for (int i = 0; i < mesAll.getFillStickCount(); i++) {
                ProductionManagement fillStickProcessPlan = ProductionManagement.builder()
                        .obtain(obtain)
                        .processAmount((long) mesAll.getFillStickInputAmountList().get(i))
                        .processStartTime(mesAll.getFillStickInputTimeList().get(i))
                        .processFinishTime(mesAll.getFillStickOutputTimeList().get(i))
                        .process(6L)
                        .build();
                productionManagementRepository.save(fillStickProcessPlan);

            }


            // 7 검사
            for (int i = 0; i < mesAll.getCheckCount(); i++) {
                ProductionManagement checkProcessPlan = ProductionManagement.builder()
                        .obtain(obtain)
                        .processAmount((long) mesAll.getCheckInputAmountList().get(i))
                        .processStartTime(mesAll.getCheckInputTimeList().get(i))
                        .processFinishTime(mesAll.getCheckOutputTimeList().get(i))
                        .process(7L)
                        .build();
                productionManagementRepository.save(checkProcessPlan);
            }


            // 8 포장
            for (int i = 0; i < mesAll.getPackingCount(); i++) {
                ProductionManagement packingProcessPlan = ProductionManagement.builder()
                        .obtain(obtain)
                        .processAmount((long) mesAll.getPackingInputAmountList().get(i))
                        .processStartTime(mesAll.getPackingInputTimeList().get(i))
                        .processFinishTime(mesAll.getPackingOutputTimeList().get(i))
                        .process(8L)
                        .build();
                productionManagementRepository.save(packingProcessPlan);
            }

        }

    }
}

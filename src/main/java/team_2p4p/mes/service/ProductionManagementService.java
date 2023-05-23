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

    void addProductionManagement(MesAll mesAll, Obtain obtain){

        // 1 원료개량
        // 2 전처리
        // 3 액체제조시스템
        // 4 충진
        // 5 검사
        // 6 포장

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
        for(int i = 0; i < mesAll.getPreProcessingCount(); i++){
            ProductionManagement preProcessPlan = ProductionManagement.builder()
                    .obtain(obtain)
                    .processAmount((long) mesAll.getPreProcessingAmountList().get(i))
                    .processStartTime(mesAll.getInputPreProcessingTimeList().get(i))
                    .processFinishTime(mesAll.getOutputPreProcessingTimeList().get(i))
                    .process(2L)
                    .build();
            productionManagementRepository.save(preProcessPlan);
        }


    }
}

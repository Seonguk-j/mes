package team_2p4p.mes.util.calculator;

import team_2p4p.mes.util.process.LiquidSystem;
import team_2p4p.mes.util.process.Measurement;
import team_2p4p.mes.util.process.PreProcessing;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {

    int measuremntLeadTime = 20;
    int preprocessingLeadTime = 20;
    int liquidSystemLeadTime = 0;


    //원료계량
    MesAll materialMeasurement(MesAll mesAll, Measurement measurement) {
        mesAll.setMeasurementAmount(mesAll.getAmount());
        if (measurement.getConfirmList().isEmpty()) {
            //리스트가 비었을떄
            mesAll.setInputMeasurementTime(inputTimeCheck(measuremntLeadTime,mesAll.getTime()));
            mesAll.setOutputMeasurementTime(mesAll.getInputMeasurementTime().plusMinutes(30)); // mesAll에 종료시간
        } else {
            //리스트에 무언가가 있을때 마지막 시간을 꺼내온다
            LocalDateTime lastTime = measurement.getConfirmList().get(measurement.getConfirmList().size() - 1).getOutputMeasurementTime();
            mesAll.setInputMeasurementTime(inputTimeCheck(measuremntLeadTime,lastTime));
            mesAll.setOutputMeasurementTime(mesAll.getInputMeasurementTime().plusMinutes(30)); // mesAll에 종료시간
        }
        return mesAll;
    }

    //전처리
    MesAll preProcessing(MesAll mesAll, PreProcessing preProcessing) {
        int preProcessingCount = (int)Math.ceil(mesAll.getMeasurementAmount()/1000.0) == 0 ? 1 : (int)Math.ceil(mesAll.getMeasurementAmount()/1000.0);
        int preProcessingLastAmount = (int) mesAll.getMeasurementAmount()%1000;

        mesAll.setPreProcessingCount(preProcessingCount);

        for(int i = 0; i < preProcessingCount; i++){

            if(mesAll.getItemId() == 1){
                mesAll.getPreProcessingAmountList().add(1000);
            }else{
                if(i == preProcessingCount-1){
                    mesAll.getPreProcessingAmountList().add(preProcessingLastAmount);
                }else{
                    mesAll.getPreProcessingAmountList().add(1000);
                }

            }
            if(i == 0){

                if (preProcessing.getConfirmList().isEmpty()) {
                    //전처리 확정리스트가 비었을때

                    mesAll.getInputPreProcessingTimeList().add(inputTimeCheck(preprocessingLeadTime,mesAll.getOutputMeasurementTime()));
                    //시간계산 1ton/hour  1분당 16.667kg 가능 1kg당 3.6초 걸림

                } else {
                    //전처리 확정리스트에 대기목록이 있을때
                    List<LocalDateTime> list = preProcessing.getConfirmList().get(preProcessing.getConfirmList().size() - 1).getOutputPreProcessingTimeList();
                    LocalDateTime lastTime = list.get(list.size()-1);
                    mesAll.getInputPreProcessingTimeList().add(inputTimeCheck(preprocessingLeadTime,lastTime));
                    //시간계산 1ton/hour  1분당 16.667kg 가능 1kg당 3.6초 걸림
                }
                mesAll.getOutputPreProcessingTimeList().add(mesAll.getInputPreProcessingTimeList().get(i).plusSeconds((long) (3.6 * mesAll.getPreProcessingAmountList().get(i))));

            }else{// i != 0

                //여기서 부터는 시간의 기준이 직전 전처리 끝난 시간 기준으로 리드타임을 부여하면서 작업을 해야된다.
                //고려 해야되는 것은? 직전 전처리가 9시부터 17:40분에 끝났을때 투입(점심시간 아니면) else 일때는 요일 고려해서 다음날 투입(일요일 제외)
                LocalDateTime preEndTime = mesAll.getOutputPreProcessingTimeList().get(i-1);
                mesAll.getInputPreProcessingTimeList().add(inputTimeCheck(preprocessingLeadTime,preEndTime));
                //시간계산 1ton/hour  1분당 16.667kg 가능 1kg당 3.6초 걸림
                mesAll.getOutputPreProcessingTimeList().add(mesAll.getInputPreProcessingTimeList().get(i).plusSeconds((long) (3.6 * mesAll.getPreProcessingAmountList().get(i))));
            } // i != 0
        } //

        //위에 for문에서 list들한테 값을 다 넣어줌
        //여기서 for문 한번 더돌려서 map에 값을 넣어줌
        for(int i = 0; i < mesAll.getPreProcessingCount(); i++){
            Map<String,Object> map = new HashMap<>();

            map.put(i+1 + "번째 투입양", mesAll.getPreProcessingAmountList().get(i));
            map.put(i+1 + "번째 시작시간", mesAll.getInputPreProcessingTimeList().get(i));
            map.put(i+1 + "번째 끝난시간",mesAll.getOutputPreProcessingTimeList().get(i));

            mesAll.getPreProcessingMapList().add(map);
        }
        return mesAll;
    }

    // 액체제조 시스템에서 언제 어떻게 들어가는지 체크
    // 이걸 이게 끝나면 mesAll에 inputTime이 체크가 된다.



    //액체제조
    MesAll operateLiquidSystem(MesAll mesAll, LiquidSystem liquidSystem){
        int amount;
        int leadTime = (mesAll.getItemId()==3)?20:60;
        if(mesAll.getItemId() == 1){
            //양배추일때
            mesAll.setTotalLiquidSystemCount(mesAll.getPreProcessingCount()); //양배추는 전처리회수랑 똑같음
            amount = 1000;

            for(int i = 0; i < mesAll.getTotalLiquidSystemCount(); i++){
                liquidExcpect(mesAll,liquidSystem,leadTime,amount,i);
            }
        }else if(mesAll.getItemId() == 2){
            //흑마늘일때

            mesAll.setTotalLiquidSystemCount((int)Math.ceil(mesAll.getMeasurementAmount()/500.0));

            //투입개수 보정 마지막은 500이 아님
            for(int i = 0; i < mesAll.getTotalLiquidSystemCount(); i++){

                if(i == mesAll.getTotalLiquidSystemCount() -1){
                    //마지막일때는 개수보정
                    amount = (mesAll.getMeasurementAmount()%500==0)?500:(int)(mesAll.getMeasurementAmount() % 500);
                }else {
                    amount = 500;
                }

                liquidExcpect(mesAll,liquidSystem,leadTime,amount,i);
            }

        }else{
            //스틱일때

            mesAll.setTotalLiquidSystemCount((int)Math.ceil(mesAll.getMeasurementAmount()/650.0));
            //마지막 개수 보정

            for(int i = 0; i < mesAll.getTotalLiquidSystemCount(); i++){

                if(i == mesAll.getTotalLiquidSystemCount() -1){
                    //마지막일때는 개수보정
                    amount = (mesAll.getMeasurementAmount()%650==0)?650:(int)(mesAll.getMeasurementAmount() % 650);
                }else {
                    amount = 650;
                }

                liquidExcpect(mesAll,liquidSystem,leadTime,amount,i);
            }

        }

        return mesAll;

    }











    //점심시간 체크
    LocalDateTime lunchCheck(int leadTime, LocalDateTime inputPossible){

        LocalDateTime outputTime;
        LocalTime localTime = inputPossible.toLocalTime();
        LocalTime lunchStartTime = LocalTime.of(12, 00).minusMinutes(leadTime); // 시작 시간: 12시 0분이지만 리드타임을 고려해서 11시 40분으로 계산
        LocalTime lunchEndTime = LocalTime.of(13, 0); // 종료 시간: 13시 0분

        if (localTime.isAfter(lunchStartTime) && localTime.isBefore(lunchEndTime)) {
            // 리턴받은 시간이 점심시간이라면
            outputTime = inputPossible.withHour(13).withMinute(0).withSecond(0).withNano(0).plusMinutes(leadTime); // mesAll에 투입시간

        }else{
            //점심시간이 아닐때
            outputTime = inputPossible.plusMinutes(leadTime); // mesAll에 투입시간
        }
        return outputTime;
    }

    //전공정 끝난 시간을 넣으면 투입할 시간이 나오는 메서드
    LocalDateTime inputTimeCheck(int leadTime, LocalDateTime inputPossible){
        LocalDateTime outputTime;
        LocalTime localTime = inputPossible.toLocalTime();
        LocalTime startTime = LocalTime.of(9, 00); // 업무시간
        LocalTime endTime = LocalTime.of(18, 0).minusMinutes(leadTime); // 종료 시간: 18시 00분 - 리드타임

        if(localTime.isAfter(startTime) && localTime.isBefore(endTime)){
            //9시부터 18:00 - 리드타임에 종료 되었을때
            outputTime = lunchCheck(leadTime,inputPossible);
        }else{
            //원료계량이 9시부터 17 40분과 사이에 종료되지 않았을때
            if(inputPossible.getDayOfWeek().getValue() == 5){
                //금요일 이면
                outputTime = inputPossible.plusDays(3).withHour(9).plusMinutes(leadTime).withSecond(0).withNano(0);
            }else{
                outputTime = inputPossible.plusDays(1).withHour(9).plusMinutes(leadTime).withSecond(0).withNano(0);
            }
        }
        return outputTime;
    }






    int getLiquidSystemOutputAmount(int input,MesAll mesAll){
        int output = 0;

        if(mesAll.getItemId() == 1){
            output = (int)(input * 1.6); //1000 넣으면 1600 나옴
        }else if (mesAll.getItemId() == 2){
            output = (int)(input * 2.4); //500넣으면 1200
        }else{
            output = input * 3;
        }

        return output;
    }



    void inputLiquidMachine1(MesAll mesAll,int leadTime, int i, int amount, LocalDateTime inputTIme){
        mesAll.getWhereList().add(1);
        mesAll.setLiquidSystemCount1(mesAll.getLiquidSystemCount1()+1); //회수 + 1
        mesAll.getLiquidSystemInputAmountList1().add(amount);
        mesAll.getLiquidSystemOutputAmountList1().add(getLiquidSystemOutputAmount(amount,mesAll));
        mesAll.getLiquidSystemInputTimeList1().add(inputTimeCheck(leadTime,inputTIme));

        if(mesAll.getItemId() == 1){
            mesAll.getLiquidSystemOutputTimeList1().add(mesAll.getLiquidSystemInputTimeList1().get(mesAll.getLiquidSystemInputTimeList1().size()-1).plusHours(72));
        }else if(mesAll.getItemId() == 2){
            mesAll.getLiquidSystemOutputTimeList1().add(mesAll.getLiquidSystemInputTimeList1().get(mesAll.getLiquidSystemInputTimeList1().size()-1).plusHours(24).plusSeconds((amount/10) * 3456L));
        }else{
            mesAll.getLiquidSystemOutputTimeList1().add(mesAll.getLiquidSystemInputTimeList1().get(mesAll.getLiquidSystemInputTimeList1().size()-1).plusHours(8));
        }

        mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(leadTime,inputTIme));
        mesAll.getLiquidSystemOutputTimeList().add(mesAll.getLiquidSystemOutputTimeList1().get(mesAll.getLiquidSystemOutputTimeList1().size()-1));
        mesAll.getLiquidSystemInputAmountList().add(amount);
        mesAll.getLiquidSystemOutputAmountList().add(getLiquidSystemOutputAmount(amount,mesAll));
    }

    void inputLiquidMachine2(MesAll mesAll,int leadTime, int i, int amount, LocalDateTime inputTIme){
        mesAll.getWhereList().add(2);
        mesAll.setLiquidSystemCount2(mesAll.getLiquidSystemCount2()+1); //회수 + 1
        mesAll.getLiquidSystemInputAmountList2().add(amount);
        mesAll.getLiquidSystemOutputAmountList2().add(getLiquidSystemOutputAmount(amount,mesAll));
        mesAll.getLiquidSystemInputTimeList2().add(inputTimeCheck(leadTime,inputTIme));

        if(mesAll.getItemId() == 1){
            mesAll.getLiquidSystemOutputTimeList2().add(mesAll.getLiquidSystemInputTimeList2().get(mesAll.getLiquidSystemInputTimeList2().size()-1).plusHours(72));
        }else if(mesAll.getItemId() == 2){
            mesAll.getLiquidSystemOutputTimeList2().add(mesAll.getLiquidSystemInputTimeList2().get(mesAll.getLiquidSystemInputTimeList2().size()-1).plusHours(24).plusSeconds((amount/10) * 3456L));
        }else{
            mesAll.getLiquidSystemOutputTimeList2().add(mesAll.getLiquidSystemInputTimeList2().get(mesAll.getLiquidSystemInputTimeList2().size()-1).plusHours(8));
        }

        mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(leadTime,inputTIme));
        mesAll.getLiquidSystemOutputTimeList().add(mesAll.getLiquidSystemOutputTimeList2().get(mesAll.getLiquidSystemOutputTimeList2().size()-1));
        mesAll.getLiquidSystemInputAmountList().add(amount);
        mesAll.getLiquidSystemOutputAmountList().add(getLiquidSystemOutputAmount(amount,mesAll));

    }


    void liquidExcpect(MesAll mesAll, LiquidSystem liquidSystem, int leadTime, int amount,int i){
        if(mesAll.getItemId() == 3){
            if(i == 0){
                //첫번째 투입일때
                if(liquidSystem.getConfirmList().isEmpty()){
                    //액체제조 1과 2가 전부 비었을때 1에 투입
                    inputLiquidMachine1(mesAll,leadTime,i,amount,mesAll.getOutputMeasurementTime());
                }else {
                    // 확정 스케줄이 있을때
                    if(liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size()-1).getLiquidSystemCount1()==0){
                        // 기계 1이 비었으면 기계1투입 (기계 2는 스케줄이 있는상황)
                        inputLiquidMachine1(mesAll,leadTime,i,amount,mesAll.getOutputMeasurementTime());

                    }else if (liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size()-1).getLiquidSystemCount2()==0){
                        // 기계 2가 비었으면 기계2투입 (기계 1은 스케줄이 있는상황)
                        inputLiquidMachine2(mesAll,leadTime,i,amount,mesAll.getOutputMeasurementTime());
                    }else{
                        //confirmList의 시간을 확인하고 기계1과 1계2의 마지막 시간을 찾아온다.
                        List<LocalDateTime> machine1List = liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size() - 1).getLiquidSystemOutputTimeList1(); //액체제조 확정리스트 맨마지막의 아웃풋 타임리스트
                        List<LocalDateTime> machine2List = liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size() - 1).getLiquidSystemOutputTimeList2(); //액체제조 확정리스트 맨마지막의 아웃풋 타임리스트
                        LocalDateTime machine1LastTime = machine1List.get(machine1List.size()-1);//기계 1 끝나는시간
                        LocalDateTime machine2LastTime = machine2List.get(machine2List.size()-1); //기계 2 끝나는 시간

                        if(machine1LastTime.isBefore(machine2LastTime)) {
                            // 기계1이 먼저끝날때
                            inputLiquidMachine1(mesAll,leadTime,i,amount,machine1LastTime);
                        }else{
                            //기계2가 먼저끝날때
                            inputLiquidMachine2(mesAll,leadTime,i,amount,machine2LastTime);
                        }
                    }
                }
            }else if (i == 1){
                // 첫번째 투입이 아닌 것의 투입 끝나는 시간을 체크해서 input하면 된다.

                if(mesAll.getLiquidSystemCount1() ==  1){
                    // 첫번째 투입이 기계 1일떄
                    if(liquidSystem.getConfirmList().isEmpty()){
                        //액체제조 1과 2가 전부 비어서 첫번째가 자동으로1로 들어가서 2로 들어갈때
                        inputLiquidMachine2(mesAll,leadTime,i,amount,mesAll.getOutputMeasurementTime());
                    }else{

                        if (liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size()-1).getLiquidSystemCount2()==0){
                            //엑체제조 확정리스트에 리스트는 있지만 2번만 비어있는경우
                            inputLiquidMachine2(mesAll,leadTime,i,amount,mesAll.getOutputMeasurementTime());
                        }else{
                            // 기계 2 스케줄 완료 후 투입
                            List<LocalDateTime> machine2List = liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size() - 1).getLiquidSystemOutputTimeList2(); //액체제조 확정리스트 맨마지막의 아웃풋 타임리스트
                            LocalDateTime machine2LastTime = machine2List.get(machine2List.size()-1); //기계 2 끝나는 시간
                            inputLiquidMachine2(mesAll,leadTime,i,amount,machine2LastTime);
                        }
                    }

                }else{
                    // 첫번째 투입이 기계 2일때
                    // 첫번째 기계의 투입이 2라는 말은 무조건 confirmList가 존재하기 떄문에 get이 가능
                    if (liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size()-1).getLiquidSystemCount1()==0){
                        // 기계 1이 비었으면 기계1투입
                        inputLiquidMachine1(mesAll,leadTime,i,amount,mesAll.getOutputMeasurementTime());
                    }else{
                        // 기계 1 스케줄 완료 후 투입
                        List<LocalDateTime> machine1List = liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size() - 1).getLiquidSystemOutputTimeList1(); //액체제조 확정리스트 맨마지막의 아웃풋 타임리스트
                        LocalDateTime machine1LastTime = machine1List.get(machine1List.size()-1); //기계 1 끝나는 시간
                        inputLiquidMachine1(mesAll,leadTime,i,amount,machine1LastTime);
                    }
                }
            }else{
                // - 2 의 끝나는 시간을 input 타임으로 잡으면 된다.
                if(mesAll.getWhereList().get(mesAll.getWhereList().size()-1) == 1){
                    //직전 결과의 투입이 기계1일때
                    inputLiquidMachine2(mesAll,leadTime,i,amount,mesAll.getLiquidSystemOutputTimeList2().get(mesAll.getLiquidSystemOutputTimeList2().size()-1));
                }else{
                    //직전 결과의 투입이 기계2일때
                    inputLiquidMachine1(mesAll,leadTime,i,amount,mesAll.getLiquidSystemOutputTimeList1().get(mesAll.getLiquidSystemOutputTimeList1().size()-1));
                }
            }
        }else{
            // 양배추나 흑마늘일때
            if(i == 0){
                //첫번째 투입일때
                if(liquidSystem.getConfirmList().isEmpty()){
                    //액체제조 1과 2가 전부 비었을때 1에 투입
                    inputLiquidMachine1(mesAll,leadTime,i,amount,mesAll.getOutputPreProcessingTimeList().get(i));
                }else {
                    // 확정 스케줄이 있을때
                    if(liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size()-1).getLiquidSystemCount1()==0){
                        // 기계 1이 비었으면 기계1투입 (기계 2는 스케줄이 있는상황)
                        inputLiquidMachine1(mesAll,leadTime,i,amount,mesAll.getOutputPreProcessingTimeList().get(i));

                    }else if (liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size()-1).getLiquidSystemCount2()==0){
                        // 기계 2가 비었으면 기계2투입 (기계 1은 스케줄이 있는상황)
                        inputLiquidMachine2(mesAll,leadTime,i,amount,mesAll.getOutputPreProcessingTimeList().get(i));
                    }else{
                        //confirmList의 시간을 확인하고 기계1과 1계2의 마지막 시간을 찾아온다.
                        List<LocalDateTime> machine1List = liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size() - 1).getLiquidSystemOutputTimeList1(); //액체제조 확정리스트 맨마지막의 아웃풋 타임리스트
                        List<LocalDateTime> machine2List = liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size() - 1).getLiquidSystemOutputTimeList2(); //액체제조 확정리스트 맨마지막의 아웃풋 타임리스트
                        LocalDateTime machine1LastTime = machine1List.get(machine1List.size()-1);//기계 1 끝나는시간
                        LocalDateTime machine2LastTime = machine2List.get(machine2List.size()-1); //기계 2 끝나는 시간

                        if(machine1LastTime.isBefore(machine2LastTime)) {
                            // 기계1이 먼저끝날때
                            inputLiquidMachine1(mesAll,leadTime,i,amount,machine1LastTime);
                        }else{
                            //기계2가 먼저끝날때
                            inputLiquidMachine2(mesAll,leadTime,i,amount,machine2LastTime);
                        }
                    }
                }
            }else if (i == 1){
                // 첫번째 투입이 아닌 것의 투입 끝나는 시간을 체크해서 input하면 된다.

                if(mesAll.getLiquidSystemCount1() ==  1){
                    // 첫번째 투입이 기계 1일떄
                    if(liquidSystem.getConfirmList().isEmpty()){
                        //액체제조 1과 2가 전부 비어서 첫번째가 자동으로1로 들어가서 2로 들어갈때
                        inputLiquidMachine2(mesAll,leadTime,i,amount,mesAll.getOutputPreProcessingTimeList().get(i));
                    }else{

                        if (liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size()-1).getLiquidSystemCount2()==0){
                            //엑체제조 확정리스트에 리스트는 있지만 2번만 비어있는경우
                            inputLiquidMachine2(mesAll,leadTime,i,amount,mesAll.getOutputPreProcessingTimeList().get(i));
                        }else{
                            // 기계 2 스케줄 완료 후 투입
                            List<LocalDateTime> machine2List = liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size() - 1).getLiquidSystemOutputTimeList2(); //액체제조 확정리스트 맨마지막의 아웃풋 타임리스트
                            LocalDateTime machine2LastTime = machine2List.get(machine2List.size()-1); //기계 2 끝나는 시간
                            inputLiquidMachine2(mesAll,leadTime,i,amount,machine2LastTime);
                        }
                    }

                }else{
                    // 첫번째 투입이 기계 2일때
                    // 첫번째 기계의 투입이 2라는 말은 무조건 confirmList가 존재하기 떄문에 get이 가능
                    if (liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size()-1).getLiquidSystemCount1()==0){
                        // 기계 1이 비었으면 기계1투입
                        inputLiquidMachine1(mesAll,leadTime,i,amount,mesAll.getOutputPreProcessingTimeList().get(i));
                    }else{
                        // 기계 1 스케줄 완료 후 투입
                        List<LocalDateTime> machine1List = liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size() - 1).getLiquidSystemOutputTimeList1(); //액체제조 확정리스트 맨마지막의 아웃풋 타임리스트
                        LocalDateTime machine1LastTime = machine1List.get(machine1List.size()-1); //기계 1 끝나는 시간
                        inputLiquidMachine1(mesAll,leadTime,i,amount,machine1LastTime);
                    }
                }
            }else{
                // - 2 의 끝나는 시간을 input 타임으로 잡으면 된다.
                if(mesAll.getWhereList().get(mesAll.getWhereList().size()-1) == 1){
                    //직전 결과의 투입이 기계1일때
                    inputLiquidMachine2(mesAll,leadTime,i,amount,mesAll.getLiquidSystemOutputTimeList2().get(mesAll.getLiquidSystemOutputTimeList2().size()-1));
                }else{
                    //직전 결과의 투입이 기계2일때
                    inputLiquidMachine1(mesAll,leadTime,i,amount,mesAll.getLiquidSystemOutputTimeList1().get(mesAll.getLiquidSystemOutputTimeList1().size()-1));
                }
            }
        }
    }


}


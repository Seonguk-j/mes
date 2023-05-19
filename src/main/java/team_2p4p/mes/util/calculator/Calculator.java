package team_2p4p.mes.util.calculator;

import net.bytebuddy.asm.Advice;
import org.hibernate.result.Output;
import team_2p4p.mes.util.process.LiquidSystem;
import team_2p4p.mes.util.process.Measurement;
import team_2p4p.mes.util.process.PreProcessing;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    MesAll operateLiquidSystem(MesAll mesAll, LiquidSystem liquidSystem1,LiquidSystem liquidSystem2){

        int inputCount = liquidSystemInputCountCal(mesAll);
        int max = liquidSystemMaxAmount(mesAll);
        int liquidInputAmount = 0;

        for(int i = 0; i < inputCount; i++){

            if(i != inputCount - 1){
                liquidInputAmount = max;
            }else{
                if(mesAll.getMeasurementAmount() % max != 0) {
                    liquidInputAmount = (int) (mesAll.getMeasurementAmount() % max);
                }
                else {
                    liquidInputAmount = max;
                }
            }

            if(liquidSystem1.getConfirmList().isEmpty()){
                //기계 1에 투입
                mesAll.getLiquidSystemInputAmountList1().add(liquidInputAmount);
                mesAll.getLiquidSystemOutputAmountList1().add(getLiquidSystemOutputAmount(liquidInputAmount,mesAll));
            }else if (liquidSystem2.getConfirmList().isEmpty()){
                //기계 2에 투입
                mesAll.getLiquidSystemInputAmountList2().add(liquidInputAmount);
                mesAll.getLiquidSystemOutputAmountList2().add(getLiquidSystemOutputAmount(liquidInputAmount,mesAll));
            }else{
                //빨리 끝나는 것을 찾아서 투입
                //빨리 끝나는 것을 찾는 메서드

            }

            //이전 공정에서 끝난
        } //for




        if(mesAll.getItemId() == 1){
            //양배추일때
            liquidSystemLeadTime = 60;
            mesAll.setLiquidSystemCount(mesAll.getPreProcessingCount());

            for(int i = 0; i < mesAll.getLiquidSystemCount(); i++){
                mesAll.getLiquidSystemInputAmountList().add(mesAll.getPreProcessingAmountList().get(i)); //개수 정하기
                whereWhenInput(mesAll,liquidSystem,i); //들어간 시간 + 1번2번중 어디로 들어갈지 정하기
                LocalDateTime addTime = mesAll.getLiquidSystemInputTimeList().get(i).plusHours(72);//무조건 1000들어가기때문에 들어간 시간에서 72시간더하기
                mesAll.getLiquidSystemOutputTimeList().add(addTime);
                mesAll.getLiquidSystemOutputAmountList().add(1600);
            }

        }else if(mesAll.getItemId() == 2){
            //흑마늘일때
            liquidSystemLeadTime = 60;

            mesAll.setLiquidSystemCount((int)Math.ceil(mesAll.getMeasurementAmount()/500.0));
            //마지막 개수 보정
            int lastInputAmount = 0;

            if(mesAll.getPreProcessingAmountList().get(mesAll.getPreProcessingAmountList().size()-1) > 500){
                lastInputAmount = mesAll.getPreProcessingAmountList().get(mesAll.getPreProcessingAmountList().size()-1) - 500;
            }else{
                lastInputAmount = mesAll.getPreProcessingAmountList().get(mesAll.getPreProcessingAmountList().size()-1);
            }
            
            //회수마다 투입예정개수 input
            for(int i = 0; i < mesAll.getLiquidSystemCount(); i++){
                whereWhenInput(mesAll,liquidSystem,i); //들어간 시간 + 1번2번중 어디로 들어갈지 정하기

                if(i != mesAll.getLiquidSystemCount()-1){
                    mesAll.getLiquidSystemInputAmountList().add(500);
                    LocalDateTime addTime = mesAll.getLiquidSystemInputTimeList().get(i).plusHours(72);//무조건 1000들어가기때문에 들어간 시간에서 72시간더하기
                    mesAll.getLiquidSystemOutputTimeList().add(addTime);
                    mesAll.getLiquidSystemOutputAmountList().add(1200);
                }else{
                    //마지막일때

                    mesAll.getLiquidSystemInputAmountList().add(lastInputAmount);
                    //흑마늘 1g당 345.6초 걸린다
                    //흑마늘 최소주문단위 10당g 3456초
                    int tmp = lastInputAmount/10;
                    LocalDateTime addTime = mesAll.getLiquidSystemInputTimeList().get(i).plusHours(24).plusMinutes(tmp*3456); //걸리는 시간은 10g당 * 3456초 24시간은 혼합 및 추출
                    mesAll.getLiquidSystemOutputTimeList().add(addTime);
                    int lastOutputAmount = (int)(lastInputAmount * 2.4);
                    mesAll.getLiquidSystemOutputAmountList().add(lastOutputAmount);
                }
            }

        }else{
            //스틱일때
            liquidSystemLeadTime = 20;
            mesAll.setLiquidSystemCount((int)Math.ceil(mesAll.getMeasurementAmount()/630.0));
            //마지막 개수 보정
            int lastInputAmount = 0;

            if(mesAll.getPreProcessingAmountList().get(mesAll.getPreProcessingAmountList().size()-1) > 630){
                lastInputAmount = mesAll.getPreProcessingAmountList().get(mesAll.getPreProcessingAmountList().size()-1) - 630;
            }else{
                lastInputAmount = mesAll.getPreProcessingAmountList().get(mesAll.getPreProcessingAmountList().size()-1);
            }

            for(int i = 0; i < mesAll.getLiquidSystemCount(); i++){
                whereWhenInputStick(mesAll,liquidSystem,i); //들어간 시간 + 1번2번중 어디로 들어갈지 정하기

                if(i != mesAll.getLiquidSystemCount()-1){
                    mesAll.getLiquidSystemInputAmountList().add(630);
                    LocalDateTime addTime = mesAll.getLiquidSystemInputTimeList().get(i).plusHours(24);//얼마를 넣던 24시간 고정
                    mesAll.getLiquidSystemOutputTimeList().add(addTime);
                    mesAll.getLiquidSystemOutputAmountList().add(1950);
                }else{
                    //마지막일때

                    mesAll.getLiquidSystemInputAmountList().add(lastInputAmount);

                    int tmp = lastInputAmount;
                    LocalDateTime addTime = mesAll.getLiquidSystemInputTimeList().get(i).plusHours(24); //걸리는 시간은 무조건 24
                    mesAll.getLiquidSystemOutputTimeList().add(addTime);
                    int lastOutputAmount = (int)(lastInputAmount * 3);
                    mesAll.getLiquidSystemOutputAmountList().add(lastOutputAmount);
                }
            }

        }

        for(int i = 0; i < mesAll.getLiquidSystemCount(); i++){
            Map<String,Object> map = new HashMap<>();

            map.put(i+1 + "번째 액체제조 종료시간", mesAll.getLiquidSystemOutputTimeList().get(i));
            map.put(i+1 + "번째 엑체제조 시작시간", mesAll.getLiquidSystemInputTimeList().get(i));
            map.put(i+1 + "번째 액체제조 투입량", mesAll.getLiquidSystemInputAmountList().get(i));
            map.put(i+1 + "번째 액체제조 나오는 양",mesAll.getLiquidSystemOutputAmountList().get(i));
            map.put(i+1 + "번째 액체제조 몇번기계 넣었는지",mesAll.getLiquidSystemWhereList().get(i));
            mesAll.getLiquidSystemMapList().add(map);
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


    void whereWhenInput(MesAll mesAll,LiquidSystem liquidSystem1,LiquidSystem liquidSystem2 int i){
        if(i == 0){
            //첫번째 투입일때
            if(liquidSystem.getConfirmList().isEmpty()){
                //기계 1과 2가 비었을때
                mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,mesAll.getOutputPreProcessingTimeList().get(i))); //전처리 i번 끝난시간 기준으로 시간 고려후 투입
                mesAll.getLiquidSystemWhereList().add(1); // 1번기계 표시
            }else{
                // 스케줄이 1개 이상 있을때
                List<LocalDateTime> list = liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size() - 1).getLiquidSystemOutputTimeList(); //액체제조 확정리스트 맨마지막의 아웃풋 타임리스트
                LocalDateTime lastTime1;//기계 1 끝나는시간
                LocalDateTime lastTime2; //기계 2 끝나는 시간
                List<Integer> whereList = liquidSystem.getConfirmList().get(liquidSystem.getConfirmList().size()-1).getLiquidSystemWhereList();
                
                if(whereList.get(whereList.size()-1) == 1){
                    //스케줄의 마지막이 1번 기계일때
                    lastTime1 = list.get(list.size()-1);
                    lastTime2 = list.get(list.size()-2);

                }else{
                    //스케줄의 마지막이 2번 기계일때
                    lastTime1 = list.get(list.size()-2);
                    lastTime2 = list.get(list.size()-1);
                }


                if(lastTime1.isAfter(lastTime2)){
                    //1번기계 끝나는 시간이 2번기계보다 미래일때 2번기계에 투입
                    //기계 1이 늦게 끝난다면? 기계 2투입마지막시간 기준으로 고려해서 투입

                    System.out.println("여기로 와야돼");
                    System.out.println("시간 1 : " + lastTime1);
                    System.out.println("시간 2 : " + lastTime2);
                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,lastTime2)); //기계가 끝난시간 기준으로 시간 고려후 투입

                    mesAll.getLiquidSystemWhereList().add(2); // 2번기계 표시
                }else {
                    //나머지 경우는 기계1에 투입
                    System.out.println("잘못들어옴");
                    System.out.println("시간 1 : " + lastTime1);
                    System.out.println("시간 2 : " + lastTime2);
                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,lastTime1)); //기계가 끝난시간 기준으로 시간 고려후 투입

                    mesAll.getLiquidSystemWhereList().add(1); // 1번기계 표시
                }
            }

        }else if(i == 1){
            if(mesAll.getLiquidSystemWhereList().get(i-1) == 1){
                //직전 결과의 투입이 기계1일때
                if(liquidSystem.getConfirmList().isEmpty()){
                    //기계2가 비었을때
                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,mesAll.getOutputPreProcessingTimeList().get(i))); //전처리 i번 끝난시간 기준으로 시간 고려후 투입

                }else{


                    //스케줄이있을때
                    List<LocalDateTime> list2 = liquidSystem2.getConfirmList().get(liquidSystem2.getConfirmList().size() - 1).getLiquidSystemOutputTimeList();
                    LocalDateTime lastTime; //기계 2 끝나는 시간

                    if(liquidSystem1.getConfirmList().get(liquidSystem1.getConfirmList().size() - 1).getLiquidSystemWhereList().get(liquidSystem1.getConfirmList().get(liquidSystem1.getConfirmList().size() - 1).getLiquidSystemWhereList().size() - 1) == 1){
                        lastTime = list2.get(list2.size()-2);
                    }else{
                        lastTime = list2.get(list2.size()-1);
                    }

                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,lastTime)); //기계가 끝난시간 기준으로 시간 고려후 투입


                }

                mesAll.getLiquidSystemWhereList().add(2); // 2번기계 표시


            }else{
                //직전 결과의 투입이 기계2일때
                if(liquidSystem1.getConfirmList().isEmpty()){
                    //기계1이 비었을때
                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,mesAll.getOutputPreProcessingTimeList().get(i))); //전처리 i번 끝난시간 기준으로 시간 고려후 투입

                }else{
                    //기계 1에 스케줄이 있을때 투입
                    List<LocalDateTime> list1 = liquidSystem1.getConfirmList().get(liquidSystem2.getConfirmList().size() - 1).getLiquidSystemOutputTimeList();
                    LocalDateTime lastTime; //기계 1 끝나는 시간

                    if(liquidSystem1.getConfirmList().get(liquidSystem1.getConfirmList().size() - 1).getLiquidSystemWhereList().get(liquidSystem1.getConfirmList().get(liquidSystem1.getConfirmList().size() - 1).getLiquidSystemWhereList().size() - 1) == 1){
                        lastTime = list2.get(list2.size()-2);
                    }else{
                        lastTime = list2.get(list2.size()-1);
                    }

                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,lastTime)); //기계가 끝난시간 기준으로 시간 고려후 투입


                }
                mesAll.getLiquidSystemWhereList().add(1); // 1번기계 표시
            }
        }else {
            // i == 2이후부터는 앞에 input 리스트의 output 기준으로 고려
            if(mesAll.getLiquidSystemWhereList().get(i-1) == 1){
                //직전 결과의 투입이 기계1일때
                mesAll.getLiquidSystemWhereList().add(2); // 2번기계 표시

            }else{
                //직전 결과의 투입이 기계2일때
                mesAll.getLiquidSystemWhereList().add(1); // 1번기계 표시
            }
            mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,mesAll.getLiquidSystemOutputTimeList().get(i-2)));

        }
    }


    void whereWhenInputStick(MesAll mesAll,LiquidSystem liquidSystem1,LiquidSystem liquidSystem2, int i){
        if(i == 0){
            //첫번째 투입일때
            if(liquidSystem1.getConfirmList().isEmpty()){
                //기계 1이 비었을때
                mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,mesAll.getOutputMeasurementTime())); // 스틱은 전처리가 없으므로 원료계량 끝난시점이 기준
                mesAll.getLiquidSystemWhereList().add(1); // 1번기계 표시
            }else if(liquidSystem2.getConfirmList().isEmpty()){
                //기계 2가 비었을때
                mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,mesAll.getOutputMeasurementTime())); // 스틱은 전처리가 없으므로 원료계량 끝난시점이 기준
                mesAll.getLiquidSystemWhereList().add(2); // 1번기계 표시
            }else{
                //두 기계다 스케줄이 있을때
                List<LocalDateTime> list1 = liquidSystem1.getConfirmList().get(liquidSystem1.getConfirmList().size() - 1).getLiquidSystemOutputTimeList();
                LocalDateTime lastTime1 = list1.get(list1.size()-1); //기계 1 끝나는시간
                List<LocalDateTime> list2 = liquidSystem2.getConfirmList().get(liquidSystem2.getConfirmList().size() - 1).getLiquidSystemOutputTimeList();
                LocalDateTime lastTime2 = list2.get(list2.size()-1); //기계 2 끝나는 시간

                if(lastTime1.isAfter(lastTime2)){
                    //기계 1이 늦게 끝난다면? 기계 2투입마지막시간 기준으로 고려해서 투입
                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,lastTime2)); //기계가 끝난시간 기준으로 시간 고려후 투입
                    mesAll.getLiquidSystemWhereList().add(2); // 2번기계 표시
                }else {
                    //나머지 경우는 기계1에 투입
                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,lastTime1)); //기계가 끝난시간 기준으로 시간 고려후 투입

                    mesAll.getLiquidSystemWhereList().add(1); // 1번기계 표시
                }
            }

        }else if(i == 1){
            if(mesAll.getLiquidSystemWhereList().get(i-1) == 1){
                //직전 결과의 투입이 기계1일때
                if(liquidSystem2.getConfirmList().isEmpty()){
                    //기계2가 비었을때
                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,mesAll.getOutputMeasurementTime())); //전처리 i번 끝난시간 기준으로 시간 고려후 투입

                }else{
                    //스케줄이있을때
                    List<LocalDateTime> list2 = liquidSystem2.getConfirmList().get(liquidSystem2.getConfirmList().size() - 1).getLiquidSystemOutputTimeList();
                    LocalDateTime lastTime2 = list2.get(list2.size()-1); //기계 2 끝나는 시간
                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,lastTime2)); //기계가 끝난시간 기준으로 시간 고려후 투입


                }

                mesAll.getLiquidSystemWhereList().add(2); // 2번기계 표시


            }else{
                //직전 결과의 투입이 기계2일때
                if(liquidSystem1.getConfirmList().isEmpty()){
                    //기계1이 비었을때
                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,mesAll.getOutputMeasurementTime())); //전처리 i번 끝난시간 기준으로 시간 고려후 투입

                }else{
                    //투입
                    List<LocalDateTime> list1 = liquidSystem1.getConfirmList().get(liquidSystem1.getConfirmList().size() - 1).getLiquidSystemOutputTimeList();
                    LocalDateTime lastTime1 = list1.get(list1.size()-1); //기계 1 끝나는시간
                    mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,lastTime1)); //기계가 끝난시간 기준으로 시간 고려후 투입

                }
                mesAll.getLiquidSystemWhereList().add(1); // 1번기계 표시
            }
        }else {
            // i == 2이후부터는 앞에 input 리스트의 output 기준으로 고려
            if(mesAll.getLiquidSystemWhereList().get(i-1) == 1){
                //직전 결과의 투입이 기계1일때
                mesAll.getLiquidSystemWhereList().add(2); // 2번기계 표시

            }else{
                //직전 결과의 투입이 기계2일때
                mesAll.getLiquidSystemWhereList().add(1); // 1번기계 표시
            }
            mesAll.getLiquidSystemInputTimeList().add(inputTimeCheck(liquidSystemLeadTime,mesAll.getLiquidSystemOutputTimeList().get(i-2)));

        }
    }

    int liquidSystemInputCountCal(MesAll mesAll){
        int count = 0;

        if(mesAll.getItemId() == 1){
            count = mesAll.preProcessingCount;
        }else if (mesAll.getItemId() == 2){
            count = (int)Math.ceil(mesAll.getMeasurementAmount()/500.0);
        }else{
            count = (int)Math.ceil(mesAll.getMeasurementAmount()/650.0);
        }

        return count;
    }

    int liquidSystemMaxAmount(MesAll mesAll){
        int max = 0;

        if(mesAll.getItemId() == 1){
            max = 1000;
        }else if (mesAll.getItemId() == 2){
            max = 500;
        }else{
            max = 650;
        }

        return max;
    }

    int getLiquidSystemOutputAmount(int input,MesAll mesAll){
        int output = 0;

        if(mesAll.getItemId() == 1){
            output = input * 2;
        }else if (mesAll.getItemId() == 2){
            output = input * 4;
        }else{
            output = input * 3;
        }

        return output;
    }

                    mesAll.getLiquidSystemInputTimeList1().add(inputTimeCheck(60,mesAll.outputPreProcessingTimeList.get(i)));


}









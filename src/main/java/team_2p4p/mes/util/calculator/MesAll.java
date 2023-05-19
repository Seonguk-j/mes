package team_2p4p.mes.util.calculator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MesAll {

    MesAll(){
        inputPreProcessingTimeList = new ArrayList<>();
        outputPreProcessingTimeList = new ArrayList<>();
        preProcessingAmountList = new ArrayList<>();
        preProcessingMapList = new ArrayList<>();
        liquidSystemInputTimeList1 = new ArrayList<>(); // 액체제조 1회당 시작시간
        liquidSystemOutputTimeList1 = new ArrayList<>(); //액체제조 1회당 끝난시간
        liquidSystemInputAmountList1 = new ArrayList<>(); //액체제조 1회당 인풋양
        liquidSystemOutputAmountList1 = new ArrayList<>(); //액체제조 1회당 아웃양
        liquidSystemMapList1 = new ArrayList<>(); //액체제조 모든 정보
        liquidSystemInputTimeList2 = new ArrayList<>(); // 액체제조 1회당 시작시간
        liquidSystemOutputTimeList2 = new ArrayList<>(); //액체제조 1회당 끝난시간
        liquidSystemInputAmountList2 = new ArrayList<>(); //액체제조 1회당 인풋양
        liquidSystemOutputAmountList2 = new ArrayList<>(); //액체제조 1회당 아웃양
        liquidSystemMapList2 = new ArrayList<>(); //액체제조 모든 정보

    }

    static int stockCabbage = 0;         // 양배추 재고량(kg) : 1
    static int stockGarlic = 0;            // 흑마늘 재고량(kg) : 2
    static int stockPomegranate = 0;      // 석류액기스 재고량(kg) : 3
    static int stockPlum = 0;            // 매실액기스 재고량(kg) : 4
    static int stockCollagen = 0;         // 콜라겐 재고량(kg) : 5
    static int stockPouch = 0;            // 파우치 재고량(ea) : 6
    static int stockStickPouch = 0;         // 스틱파우치 재고량(ea) : 7
    static int stockBox = 0;            // 박스 재고량(ea) : 8

    static int[] stockProduct = new int[5];   // 완성품 재고량(1 ~ 4)

    static int[][] obtainList = new int[2][10];

    boolean stockEnough;            // 완성품 재고 충분한 경우 : true, 부족한 경우 : false;

    // 발주 주문 종합을 위한 배열
    static int[] orderList = new int[9];
//   static int[] orderList = {0, 0, 100, 300, 50, 0, 0, 0, 0};

    // 금일 발주 진행을 위한 배열
    static int[] todayOrderList = new int[9];


    // uk -> tae
    long itemId; // 원료계량 전에 return 받은 itemId 0,1,2,3
    double amount; // 원료계량 전에 return 받은 amount 원료계량 후에도 이값을 return함
    LocalDateTime time;



    double measurementAmount; // 원료 계량 투입량

    LocalDateTime inputMeasurementTime; // 원료계량 투입시간
    LocalDateTime outputMeasurementTime; // 원료계량 끝나는 시간



    int preProcessingCount; //전처리 몇번해야하는지    원료 계량 투입량/1000
    List<LocalDateTime> inputPreProcessingTimeList; // 전처리 1회당 시작시간
    List<LocalDateTime> outputPreProcessingTimeList; //전처리 1회당 끝난시간
    List<Integer> preProcessingAmountList; //전처리 1회당 양

    List<Map<String, Object>> preProcessingMapList; //전처리 모든 정보



    //액체제조 관련
    int liquidSystemCount1; //액체제조 몇번 돌려야 하는지
    List<LocalDateTime> liquidSystemInputTimeList1; // 액체제조 1회당 시작시간
    List<LocalDateTime> liquidSystemOutputTimeList1; //액체제조 1회당 끝난시간
    List<Integer> liquidSystemInputAmountList1; //액체제조 1회당 인풋양
    List<Integer> liquidSystemOutputAmountList1; //액체제조 1회당 아웃양
    List<Map<String, Object>> liquidSystemMapList1; //액체제조 모든 정보
    int liquidSystemCount2; //액체제조 몇번 돌려야 하는지
    List<LocalDateTime> liquidSystemInputTimeList2; // 액체제조 1회당 시작시간
    List<LocalDateTime> liquidSystemOutputTimeList2; //액체제조 1회당 끝난시간
    List<Integer> liquidSystemInputAmountList2; //액체제조 1회당 인풋양
    List<Integer> liquidSystemOutputAmountList2; //액체제조 1회당 아웃양
    List<Map<String, Object>> liquidSystemMapList2; //액체제조 모든 정보

    








    void infoMeasurement(){
        System.out.println("============MESAll infoMeasurement===============");
        System.out.println("투입량 " + measurementAmount);
        System.out.println("itemid " + itemId);
        System.out.println("리턴받은 시간 " + time);
        System.out.println("투입시간 " + inputMeasurementTime);
        System.out.println("끝나는 시간" + outputMeasurementTime);
        System.out.println("=================================");

    }
    void infoPreProcessing(){
        System.out.println("============MESAll infoPreProcessing===============");
        System.out.println("투입량 " + measurementAmount);
        System.out.println("전처리 투입 대기시작시간 " + outputMeasurementTime);

        for(int i = 0; i < preProcessingCount; i++){
            System.out.println((i+1) +"번째 전처리");
            System.out.println(inputPreProcessingTimeList.get(i));
            System.out.println(outputPreProcessingTimeList.get(i));
            System.out.println(preProcessingAmountList.get(i));
        }

        System.out.println("=================================");

    }


    public List<Map<String, Object>> getPreProcessingMapList() {
        return preProcessingMapList;
    }

    public void setPreProcessingMapList(List<Map<String, Object>> preProcessingMapList) {
        this.preProcessingMapList = preProcessingMapList;
    }


    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getMeasurementAmount() {
        return measurementAmount;
    }

    public void setMeasurementAmount(double measurementAmount) {
        this.measurementAmount = measurementAmount;
    }

    public LocalDateTime getInputMeasurementTime() {
        return inputMeasurementTime;
    }

    public void setInputMeasurementTime(LocalDateTime inputMeasurementTime) {
        this.inputMeasurementTime = inputMeasurementTime;
    }

    public LocalDateTime getOutputMeasurementTime() {
        return outputMeasurementTime;
    }

    public void setOutputMeasurementTime(LocalDateTime outputMeasurementTime) {
        this.outputMeasurementTime = outputMeasurementTime;
    }

    public int getPreProcessingCount() {
        return preProcessingCount;
    }

    public void setPreProcessingCount(int preProcessingCount) {
        this.preProcessingCount = preProcessingCount;
    }

    public List<LocalDateTime> getInputPreProcessingTimeList() {
        return inputPreProcessingTimeList;
    }

    public void setInputPreProcessingTimeList(List<LocalDateTime> inputPreProcessingTimeList) {
        this.inputPreProcessingTimeList = inputPreProcessingTimeList;
    }

    public List<LocalDateTime> getOutputPreProcessingTimeList() {
        return outputPreProcessingTimeList;
    }

    public void setOutputPreProcessingTimeList(List<LocalDateTime> outputPreProcessingTimeList) {
        this.outputPreProcessingTimeList = outputPreProcessingTimeList;
    }

    public List<Integer> getPreProcessingAmountList() {
        return preProcessingAmountList;
    }

    public void setPreProcessingAmountList(List<Integer> preProcessingAmountList) {
        this.preProcessingAmountList = preProcessingAmountList;
    }

    public int getLiquidSystemCount1() {
        return liquidSystemCount1;
    }

    public void setLiquidSystemCount1(int liquidSystemCount1) {
        this.liquidSystemCount1 = liquidSystemCount1;
    }

    public List<LocalDateTime> getLiquidSystemInputTimeList1() {
        return liquidSystemInputTimeList1;
    }

    public void setLiquidSystemInputTimeList1(List<LocalDateTime> liquidSystemInputTimeList1) {
        this.liquidSystemInputTimeList1 = liquidSystemInputTimeList1;
    }

    public List<LocalDateTime> getLiquidSystemOutputTimeList1() {
        return liquidSystemOutputTimeList1;
    }

    public void setLiquidSystemOutputTimeList1(List<LocalDateTime> liquidSystemOutputTimeList1) {
        this.liquidSystemOutputTimeList1 = liquidSystemOutputTimeList1;
    }


    public List<Integer> getLiquidSystemInputAmountList1() {
        return liquidSystemInputAmountList1;
    }

    public void setLiquidSystemInputAmountList1(List<Integer> liquidSystemInputAmountList1) {
        this.liquidSystemInputAmountList1 = liquidSystemInputAmountList1;
    }

    public List<Integer> getLiquidSystemOutputAmountList1() {
        return liquidSystemOutputAmountList1;
    }

    public void setLiquidSystemOutputAmountList1(List<Integer> liquidSystemOutputAmountList1) {
        this.liquidSystemOutputAmountList1 = liquidSystemOutputAmountList1;
    }

    public List<Map<String, Object>> getLiquidSystemMapList1() {
        return liquidSystemMapList1;
    }

    public void setLiquidSystemMapList1(List<Map<String, Object>> liquidSystemMapList1) {
        this.liquidSystemMapList1 = liquidSystemMapList1;
    }

    public int getLiquidSystemCount2() {
        return liquidSystemCount2;
    }

    public void setLiquidSystemCount2(int liquidSystemCount2) {
        this.liquidSystemCount2 = liquidSystemCount2;
    }

    public List<LocalDateTime> getLiquidSystemInputTimeList2() {
        return liquidSystemInputTimeList2;
    }

    public void setLiquidSystemInputTimeList2(List<LocalDateTime> liquidSystemInputTimeList2) {
        this.liquidSystemInputTimeList2 = liquidSystemInputTimeList2;
    }

    public List<LocalDateTime> getLiquidSystemOutputTimeList2() {
        return liquidSystemOutputTimeList2;
    }

    public void setLiquidSystemOutputTimeList2(List<LocalDateTime> liquidSystemOutputTimeList2) {
        this.liquidSystemOutputTimeList2 = liquidSystemOutputTimeList2;
    }


    public List<Integer> getLiquidSystemInputAmountList2() {
        return liquidSystemInputAmountList2;
    }

    public void setLiquidSystemInputAmountList2(List<Integer> liquidSystemInputAmountList2) {
        this.liquidSystemInputAmountList2 = liquidSystemInputAmountList2;
    }

    public List<Integer> getLiquidSystemOutputAmountList2() {
        return liquidSystemOutputAmountList2;
    }

    public void setLiquidSystemOutputAmountList2(List<Integer> liquidSystemOutputAmountList2) {
        this.liquidSystemOutputAmountList2 = liquidSystemOutputAmountList2;
    }

    public List<Map<String, Object>> getLiquidSystemMapList2() {
        return liquidSystemMapList2;
    }

    public void setLiquidSystemMapList2(List<Map<String, Object>> liquidSystemMapList2) {
        this.liquidSystemMapList2 = liquidSystemMapList2;
    }
}

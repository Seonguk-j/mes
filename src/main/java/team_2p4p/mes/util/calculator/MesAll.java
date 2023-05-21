package team_2p4p.mes.util.calculator;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
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
        liquidSystemInputTimeList2 = new ArrayList<>(); // 액체제조 1회당 시작시간
        liquidSystemOutputTimeList2 = new ArrayList<>(); //액체제조 1회당 끝난시간
        liquidSystemInputAmountList2 = new ArrayList<>(); //액체제조 1회당 인풋양
        liquidSystemOutputAmountList2 = new ArrayList<>(); //액체제조 1회당 아웃양
        whereList = new ArrayList<>();
        liquidSystemInputTimeList = new ArrayList<>(); // 액체제조 1회당 시작시간
        liquidSystemOutputTimeList = new ArrayList<>(); //액체제조 1회당 끝난시간
        liquidSystemInputAmountList = new ArrayList<>(); //액체제조 1회당 인풋양
        liquidSystemOutputAmountList = new ArrayList<>(); //액체제조 1회당 아웃양

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



    private double measurementAmount; // 원료 계량 투입량

    private LocalDateTime inputMeasurementTime; // 원료계량 투입시간
    private  LocalDateTime outputMeasurementTime; // 원료계량 끝나는 시간



    private  int preProcessingCount; //전처리 몇번해야하는지    원료 계량 투입량/1000
    private List<LocalDateTime> inputPreProcessingTimeList; // 전처리 1회당 시작시간
    private List<LocalDateTime> outputPreProcessingTimeList; //전처리 1회당 끝난시간
    private List<Integer> preProcessingAmountList; //전처리 1회당 양

    private List<Map<String, Object>> preProcessingMapList; //전처리 모든 정보



    //액체제조 관련
    private int totalLiquidSystemCount; // 액체제조 총 몇번 돌리는지
    private  int liquidSystemCount1; //액체제조1에 몇번 돌려야 하는지
    private List<LocalDateTime> liquidSystemInputTimeList1; // 액체제조 1회당 시작시간
    private List<LocalDateTime> liquidSystemOutputTimeList1; //액체제조 1회당 끝난시간
    private List<Integer> liquidSystemInputAmountList1; //액체제조 1회당 인풋양
    private List<Integer> liquidSystemOutputAmountList1; //액체제조 1회당 아웃양
    private int liquidSystemCount2; //액체제조2에 몇번 돌려야 하는지
    private List<LocalDateTime> liquidSystemInputTimeList2; // 액체제조2 1회당 시작시간
    private List<LocalDateTime> liquidSystemOutputTimeList2; //액체제조2 1회당 끝난시간
    private List<Integer> liquidSystemInputAmountList2; //액체제조2 1회당 인풋양
    private List<Integer> liquidSystemOutputAmountList2; //액체제조2 1회당 아웃양
    private List<Integer> whereList;//어디로 갔는지
    private List<LocalDateTime> liquidSystemInputTimeList; // 액체제조 1회당 시작시간
    private List<LocalDateTime> liquidSystemOutputTimeList; //액체제조 1회당 끝난시간
    private List<Integer> liquidSystemInputAmountList; //액체제조 1회당 인풋양
    private List<Integer> liquidSystemOutputAmountList; //액체제조 1회당 아웃양
    








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


    void infoLiquidSystem(){
        System.out.println("1번기계 투입회수" + liquidSystemCount1);
        System.out.println("2번기계 투입회수" + liquidSystemCount2);
        System.out.println("1번기계 투입량 리스트" + liquidSystemInputAmountList1);
        System.out.println("2번기계 투입량 리스트" + liquidSystemInputAmountList2);
        System.out.println("1번기계 생산량 리스트" + liquidSystemOutputAmountList1);
        System.out.println("2번기계 생산량 리스트" + liquidSystemOutputAmountList2);
        System.out.println("1번기계 투입시간 리스트" + liquidSystemInputTimeList1);
        System.out.println("2번기계 투입시간 리스트" + liquidSystemInputTimeList2);
        System.out.println("1번기계 완료시간 리스트" + liquidSystemOutputTimeList1);
        System.out.println("2번기계 완료시간 리스트" + liquidSystemOutputTimeList2);
    }
}

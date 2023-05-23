package team_2p4p.mes.util.calculator;

/*
 * 1. 제품에 따른 필요 자재량 확인
 * 2. 재고량과 비교하여 부족 수량 확인
 * 3. 부족 수량에 따른 최소 주문 단위로 주문
 * 4. 주문시 도착 시간 계산
 */


import team_2p4p.mes.entity.Item;
import team_2p4p.mes.repository.ItemRepository;
import team_2p4p.mes.service.ProductService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CalcOrderMaterial {
    long itemId;            // 제품 id
    int amount;               // 주문수량(box)
    static MesAll mesAll;

    private static ProductService productService;
    private static ItemRepository itemRepository;

    CalcOrderMaterial(long itemId, int amount) {

        this.itemId = itemId;
        this.amount = amount;
        mesAll = new MesAll();
        LocalDateTime now = LocalDateTime.now();   // 수주시간
        LocalDateTime time;

        mesAll.itemId = itemId;

        // 수주량과 완성품 재고량 비교
        int comparedAmount = compareStockedProduct(itemId, amount);
        // 재고량이 충분한 경우 comparedAmount < 0
        if (comparedAmount < 0) {
            mesAll.stockEnough = true;
            // mesAll에 대한 값이 tae에게 넘어갈 필요가 없음.
            // 이부분에 대한 부분 추가 고려 필요
            // 23.05.23 db에 재고에서 수주량 차감
            Item item = itemRepository.findById(itemId).orElseThrow(()-> new IllegalArgumentException("아이템이 존재하지 않습니다."));
//            productService.addMinusProductStock(item, amount, );
        }
        // 수주량이 재고량보다 많은 경우 am > 0
        else {
            // 재고수량 0으로 만들어줌 -> 출하로 넘겨야 함
            MesAll.stockProduct[(int) itemId] = 0;
            // 부족한 만큼에 대한 재료들이 발주 필요 데이터베이스(현재 orderList)에 저장
            saveOrderList(itemId, comparedAmount);

            // 도착예정일 계산하는 매소드가 들어가야함.
            time = estimateDate(itemId, comparedAmount, now).time;
            mesAll.time = time;
        }

        // 12시 이전 주문제품
        for (int i = 1; i <= 2; i++) {
            if (MesAll.orderList[i] != 0) {      // 주문이 필요 한 경우
                orderItem(i);               // 주문 매서드 실행
            }
        }

        // 15시 이전 주문제품
        for (int i = 3; i <= 4; i++) {
            if (MesAll.orderList[i] != 0) {
                orderItem(i);
            }
        }
        // 주문한 메인재료를 기준으로 부자재 주문 수량 조정
        adjustItems();
        // 15시 이전 부자재 주문
        for (int i = 5; i <= 8; i++) {
            if (MesAll.orderList[i] != 0) {
                orderItem(i);
            }
        }
        mesAll.amount = MesAll.todayOrderList[(int) itemId];
    }

    MesAll sendTae() {
        return mesAll;
    }

    // 수주 확정 전 날짜 예측을 위한 매소드
    public static MesAll estimateDate(Long itemId, int amount, LocalDateTime now) {
        MesAll mesAll = new MesAll();
        mesAll.itemId = itemId;

        int comparedAmount = compareStockedProduct(itemId, amount);

        if (comparedAmount < 0) {
            mesAll.time = now;
            mesAll.amount = 0;
            mesAll.stockEnough = true;

        } else {
            // 수주량에서 재고량을 뺀 양만큼이 amount 가 되어야함
            amount = comparedAmount;

            // 수주받은 제품 메인 재료 원자재 량 계산
            if (itemId == 1) {
                amount *= 1.5;
            } else if (itemId == 2) {
                amount /= 4;
            } else {
                amount /= 8;
            }

//         amount = calcEstimateAmount(itemId, amount);

            // obatainList에 동일 아이템이 이미 존재하는 경우(obtainList[0] : itemId, obtainList[1] : 아이템의 원자재 량)
            //  -> 발주db에서 받아와야함
            // already에 메인 원자재량을 추가해줌

            int already = 0;
            int maxOrder = minMaxOrder(itemId)[1];

            for (int i = 0; i < MesAll.obtainList[0].length; i++) {
                if (MesAll.obtainList[0][i] == itemId) {
                    already += MesAll.obtainList[1][i];
//               System.out.println(MesAll.obtainList[1][i]);
                }
            }

            int totalOrderAmount = already + amount;

            // 추가 주문이 필요한 경우(ex 기존 1000kg 주문해야하는데 2000kg을 주문해야하는경우)
            //  -> mesAll 추가로 발송
            // 추가 주문이 필요 없는 경우(ex 기존 750kg만 필요해서 최소 1000kg을 맞추기 위해 1000kg을 주문했으나, 250kg치가 추가로 수주된 경우)
            //  -> mesAll 추가발송 x or amount를 0으로 발송
            int beforeAmount = calcEstimateAmount(itemId, already);
            int afterAmount = calcEstimateAmount(itemId, totalOrderAmount);
            int sendAmount = afterAmount - beforeAmount;
            mesAll.amount = sendAmount;

            System.out.println("수주받아져 있는 제품의 총 양 : " + already);
            System.out.println("추가 주문 양 : " + amount);
            System.out.println("총 주문 양 : " + totalOrderAmount);
            System.out.println("수주받아져 있는 제품을 발주시 양 : " + beforeAmount);
            System.out.println("총 발주시 양 : " + afterAmount);
            System.out.println("태영에게 보내야 할 양 : " + sendAmount);

            if (totalOrderAmount <= maxOrder) {
                mesAll.time = calcEstimateDate(itemId, now);
            } else {
                if (totalOrderAmount % maxOrder == 0) {
                    now = calcWeekend(now, totalOrderAmount / maxOrder);
                    mesAll.time = calcEstimateDate(itemId, now);
                } else {
                    now = calcWeekend(now, totalOrderAmount / maxOrder + 1);
                    mesAll.time = calcEstimateDate(itemId, now);
                }
            }
        }

        return mesAll;
    }

    static int calcEstimateAmount(Long itemId, int amount) {
        int minAmount = minMaxOrder(itemId)[0];
        if (amount % minAmount != 0) {
            amount = (amount / minAmount + 1) * minAmount;
        }
        return amount;
    }

    static LocalDateTime calcWeekend(LocalDateTime date, int plusDays) {
        if (plusDays < 5) {
            date = date.plusDays(plusDays);
            if (date.getDayOfWeek().getValue() > 5) {
                date.plusDays(2);
            }
            return date;
        } else {
            date = date.plusDays(7);
            return calcWeekend(date, plusDays - 5);
        }
    }

    static LocalDateTime calcEstimateDate(Long itemId, LocalDateTime date) {
        LocalDateTime expectDate;

        if (itemId < 3) {
            if (date.getHour() < 12) {
                expectDate = calcImportExpectDate(itemId, date);
            } else {
                date = calcWeekend(date, 1);
                expectDate = calcImportExpectDate(itemId, date);
            }
        } else {
            if (date.getHour() < 15) {
                expectDate = calcImportExpectDate(itemId, date);
            } else {
                date = calcWeekend(date, 1);
                expectDate = calcImportExpectDate(itemId, date);
            }
        }

        return expectDate;
    }

    void adjustItems() {
        MesAll.orderList[5] = (MesAll.todayOrderList[3] + MesAll.todayOrderList[4]) * 2 / 5 - MesAll.stockCollagen;
        MesAll.orderList[6] = 20 * MesAll.todayOrderList[1] + 120 * MesAll.todayOrderList[2] - MesAll.stockPouch;
        MesAll.orderList[7] = (MesAll.todayOrderList[3] + MesAll.todayOrderList[4]) * 200 - MesAll.stockStickPouch;
        MesAll.orderList[8] = MesAll.todayOrderList[1] * 2 / 3 + MesAll.todayOrderList[2] * 4 + (MesAll.todayOrderList[3] + MesAll.todayOrderList[4]) * 8 - MesAll.stockBox;
        if (MesAll.orderList[5] < 0) {
            MesAll.stockCollagen -= (MesAll.todayOrderList[3] + MesAll.todayOrderList[4]) * 2 / 5;
            MesAll.orderList[5] = 0;
        }
        if (MesAll.orderList[6] < 0) {
            MesAll.stockPouch -= 20 * MesAll.todayOrderList[1] + 120 * MesAll.todayOrderList[2];
            MesAll.orderList[6] = 0;
        }
        if (MesAll.orderList[7] < 0) {
            MesAll.stockStickPouch -= (MesAll.todayOrderList[3] + MesAll.todayOrderList[4]) * 200;
            MesAll.orderList[7] = 0;
        }
        if (MesAll.orderList[8] < 0) {
            MesAll.stockBox -= MesAll.todayOrderList[1] * 2 / 3 + MesAll.todayOrderList[2] * 4 + (MesAll.todayOrderList[3] + MesAll.todayOrderList[4]) * 8;
            MesAll.orderList[8] = 0;
        }
    }

    // 완제품 재고와 주문량 비교 매소드
    static int compareStockedProduct(long itemId, int amount) {
        // 23.05.23 db에서 가져오는 내용 추가
        Long stock = productService.productStock(itemId);

        return (int) (amount - stock);
//      // 완제품 재고가 부족한 경우
//      if(MesAll.stockProduct[(int) itemId] < amount) {
//         // 부족한 만큼에 대한 재료들이 발주 필요 데이터베이스(현재 orderList)에 저장
//         saveOrderList(itemId, amount - MesAll.stockProduct[(int) itemId]);
//      }
//      // 완제품 재고가 충분한 경우
//      else {
//         // 완제품 재고에서 수주량 차감.
//         MesAll.stockProduct[(int) itemId] -= amount;
//      }
    }

    // 부족한 원자재 발주 필요 데이터베이스에 저장하는 매소드
    void saveOrderList(long itemId, int amount) {
        // 발주 필요량 계산
        Map<String, Object> needItem = needItem(itemId, amount);
        MesAll.orderList[(int) itemId] += Integer.parseInt(String.valueOf(needItem.get("material")));
        MesAll.orderList[8] += Integer.parseInt(String.valueOf(needItem.get("box")));

        if (itemId <= 2) {
            MesAll.orderList[6] += Integer.parseInt(String.valueOf(needItem.get("pouch")));
        } else {
            MesAll.orderList[5] += Integer.parseInt(String.valueOf(needItem.get("collagen")));
            MesAll.orderList[7] += Integer.parseInt(String.valueOf(needItem.get("pouch")));
        }
    }

    // 제품을 생산하기 위해 필요한 자재와 그에 따른 양 계산 매소드
    static Map<String, Object> needItem(long itemId, int amount) {
        int material = 0;            // 필요한 메인 원자재 양(kg)
        int pouch = 0;               // 필요한 포장재 개수(ea)
        int collagen = 0;            // 필요한 콜라겐 양(kg)
        int box = amount;            // 필요한 박스 개수(ea)

        Map<String, Object> needItem = new HashMap<>();

        if (itemId == 1) {
            material = (int) (amount * 1.5);         // 1box 당 양배추 1.5kg 필요
            pouch = amount * 30;               // 1box 당 파우치 30ea 필요
        } else if (itemId == 2) {
            material = (int) (amount * 0.25);      // 1box 당 흑마늘 1.5kg 필요
            pouch = amount * 30;               // 1box 당 파우치 30ea 필요
        } else {
            material = (int) (amount * 0.125);      // 1box 당 젤리 0.125kg 필요
            pouch = amount * 25;               // 1box 당 스틱파우치 25ea 필요
            collagen = (int) (amount * 0.05);      // 1box 당 콜라겐 0.05kg 필요
            needItem.put("collagen", collagen);
        }
        needItem.put("material", material);
        needItem.put("pouch", pouch);
        needItem.put("box", box);

        return needItem;
    }

    // 재고와 필요 자재 량을 비교하여 주문 해야하는 양을 계산하는 매소드
    // 재고가 부족한 경우 mesAll.orderList에 (필요량 - 재고량)을 추가
    // 재고가 충분한 경우 재고량 소진(추후 다른 아이템을 수주 받을 경우 재고 혼동 방지를 위함)
    void compareStockedItem(long itemId, int amount) {
        Map<String, Object> needItem = needItem(itemId, amount);
        int material = Integer.parseInt(String.valueOf(needItem.get("material")));
        int pouch = Integer.parseInt(String.valueOf(needItem.get("pouch")));
        int collagen;
        int box = Integer.parseInt(String.valueOf(needItem.get("box")));

//      System.out.println("material : " + material);
        if (itemId == 1) {
            MesAll.orderList[1] += material;
            if (MesAll.stockPouch < pouch) {      // 파우치재고가 부족한 경우
                MesAll.orderList[6] += (pouch - MesAll.stockPouch);
            } else
                MesAll.stockPouch -= pouch;
        } else if (itemId == 2) {
            MesAll.orderList[2] += material;
            if (MesAll.stockPouch < pouch) {      // 파우치재고가 부족한 경우
                MesAll.orderList[6] += (pouch - MesAll.stockPouch);
            } else
                MesAll.stockPouch -= pouch;
        } else if (itemId == 3) {
            MesAll.orderList[3] += material;
        } else {
            MesAll.orderList[4] += material;
        }

        if (itemId > 2) {
            collagen = Integer.parseInt(String.valueOf(needItem.get("collagen")));
            if (MesAll.stockCollagen < collagen) {      // 콜라겐재고가 부족한 경우
                MesAll.orderList[5] += collagen - MesAll.stockCollagen;
            } else
                MesAll.stockCollagen -= collagen;
            if (MesAll.stockStickPouch < pouch) {      // 파우치스틱재고가 부족한 경우
                MesAll.orderList[7] += pouch - MesAll.stockStickPouch;
            } else
                MesAll.stockStickPouch -= pouch;
        }

        if (MesAll.stockBox < box) {               // 박스재고가 부족한 경우
            MesAll.orderList[8] += box - MesAll.stockBox;
        } else
            MesAll.stockBox -= box;
    }

    // 각 자재별 최소, 최대 주문량
    static int[] minMaxOrder(long itemId) {
        int[] range = new int[2];         // range[0] : 최소 주문량, range[1] : 최대 주문량

        if (itemId == 1) {         // 양배추
            range[0] = 1000;
            range[1] = 5000;
        } else if (itemId == 2) {      // 흑마늘
            range[0] = 10;
            range[1] = 5000;
        } else if (itemId == 3) {      // 석류액기스
            range[0] = 5;
            range[1] = 500;
        } else if (itemId == 4) {      // 매실액기스
            range[0] = 5;
            range[1] = 500;
        } else if (itemId == 5) {      // 콜라겐
            range[0] = 5;
            range[1] = 500;
        } else if (itemId == 6) {      // 파우치
            range[0] = 1000;
            range[1] = 1000000;
        } else if (itemId == 7) {      // 스틱파우치
            range[0] = 1000;
            range[1] = 1000000;
        } else {                  // 포장box
            range[0] = 500;
            range[1] = 10000;
        }

        return range;
    }

    // 재고와 비교하여 필요한 원자재 주문 매소드(재고와 비교는 이전 과정에서 이미 진행됨)
    // 주문량이 최대 주문량을 넘어설 경우, 금일 시점으로 최대주문량만 주문 후 나머지 양은 데이터베이스에 저장해 둬야함.
    // (현재 mesAll.orderList를 주문필요량을 저장하는 데이터 베이스로 가정)
    void orderItem(long itemId) {
        LocalDateTime time = LocalDateTime.now();      // 발주시간
        int amount = MesAll.orderList[(int) itemId];   // 주문량

        int[] range = minMaxOrder(itemId);            // range[0] : 최소 주문량, range[1] : 최대 주문량

        if (amount < range[1]) {
            if (amount % range[0] == 0) {
                MesAll.todayOrderList[(int) itemId] = amount;
            } else {
                MesAll.todayOrderList[(int) itemId] = (amount / range[0] + 1) * range[0];
            }
            MesAll.orderList[(int) itemId] = 0;
        } else {
            // 필요주문량(orderList) 에 금일 주문량(todayOrderList) 빼줌.
            MesAll.todayOrderList[(int) itemId] = range[1];
            MesAll.orderList[(int) itemId] -= range[1];
        }

        // db(발주관리)저장 필요
        // 현재 메인재료에 대해서만 넘겨줌.
        if (itemId == mesAll.itemId) {
            LocalDateTime importExpectDate = calcImportExpectDate(itemId, time);
            mesAll.time = importExpectDate;
        }
    }

    // 원자재 주문은 11:59, 14:59에 일괄주문으로 지정
    // 원자재 주문은 평일만 가능
    static LocalDateTime calcImportExpectDate(long itemId, LocalDateTime time) {

        int plusDays = 2;
        if (itemId > 2 && itemId < 6) {
            plusDays = 3;
        }

        time = time.plusDays(plusDays);                  // 배송시간 추가

        // getDayOfWeek 1 ~ 7 : 월 ~ 일
        int day = time.getDayOfWeek().getValue();
        if (day < 4) {
            time = time.plusDays(3 - day);
        } else if (day < 6) {
            time = time.plusDays(5 - day);
        } else if (day == 6) {
            time = time.plusDays(2);
        } else {
            time = time.plusDays(3);
        }

        time = time.withHour(10);
        time = time.withMinute(0);
        time = time.withSecond(0);
        time = time.withNano(0);

        return time;
    }
}
